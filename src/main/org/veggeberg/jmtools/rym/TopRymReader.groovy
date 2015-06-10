package org.veggeberg.jmtools.rym

import java.util.List;

import org.cyberneko.html.parsers.SAXParser
import org.veggeberg.jmtools.domain.TopProgAlbum;

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

@CompileStatic
class TopRymReader {
	private static final URL_PREFIX = "http://rateyourmusic.com"
	
	@CompileStatic(TypeCheckingMode.SKIP)
	def findDataTable(def html) {
		def tables = html.'**'.findAll{it.name() == 'TABLE'}
		def table = null
		for (t in tables) {
			if (t.TR.size() > 30) {
				table = t
				break;
			}
		}
		return table;
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	static TopProgAlbum getData(List header, def tr) {
		final trString = tr.toString()
		if ( ! trString.contains("Avg rating:")) return null
		
		final map = [:]
		tr.'**'.findAll { it.attributes().keySet().contains('class') }.each {
			final k = it."@class".toString()
			final v = it.text().toString()
			if (k == "genre") {
				map.get('genres', []) << v
			}
			else if (k == "artist") {
				map.get('artist', []) << v
			}
			else {
				map[k.toString()] = v
			}
			if (k == 'artist') map.artistUrl = URL_PREFIX + it.@href.toString()
			if (k == 'album') map.albumUrl = URL_PREFIX + it.@href.toString()
		}
//		println "map = ${map}"
		TopProgAlbum data = new TopProgAlbum()	
		data.artist = map.artist.join(' & ');
		data.title = map.album;
		final syear = map.mediumg
		if (syear.length() > 2) 
			data.year = map.mediumg.substring(1, 5).toInteger()
		data.genres = map.genres
		data.avgRating = extractField(trString, "^.*Avg rating:").toFloat()
		data.qwr = data.avgRating
		data.artistUrl = map.artistUrl
		data.albumUrl = map.albumUrl
		data.nofRatings = extractField(trString, "^.*Ratings:").replaceAll(',','').toFloat()
		//data.nofReviews = extractField(trString, "^.*Reviews.*:").replaceAll(',','').toFloat()
//		println "data = $data"
		return data
	}
	
	static String extractField(String str, String pattern) {
		final s = str.replaceFirst(pattern, "")
		final ind = s.indexOf("|")
		return s.substring(0, ind-1)
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	List<TopProgAlbum> getAlbums(String url)
	{
		//println "url = $url"
		SAXParser parser = new SAXParser()
		def html = new XmlSlurper(parser).parse(url)
		def table = findDataTable(html);
		//println table.TR[0];
		def header;
		def albums = []
		if (table) {
			for (i in 0..table.TR.size()-1) {
				def tr = table.TR[i]
//				println "tr = $tr"
				final data = getData(header, tr)
				if (data == null) break
				albums << data;
//				if (i > 3) break
			}
		}
		return albums
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	List<TopProgAlbum> getAlbums(List<String> urls)
	{
		final List<?> ret = []
		for (url in urls) {
			ret.addAll(getAlbums(url))
		}
		println "${ret.size()} albums before deduplication"
		ret = deduplicate(ret)
		
		// Calculate qwr
		int totalNofRatings = 0
		int sumRatings = 0
		for (album in ret) {
			totalNofRatings += album.nofRatings
			sumRatings += album.avgRating
		}
		final totalAvgRating = (double)sumRatings/(double)ret.size()
		println "totalAvgRating = $totalAvgRating"
		println "totalNofRatings = $totalNofRatings"
		final weights = ret.collect { TopProgAlbum it ->
			final r = it.avgRating/totalAvgRating/0.9
			Math.log10(it.nofRatings)*it.avgRating*r/Math.log10(totalNofRatings) 
		}
		ret.eachWithIndex { TopProgAlbum it, int i ->
			it.qwr = weights[i]
		}
		
		// Sort albums and change the rank
		ret = ret.sort { TopProgAlbum a, TopProgAlbum b -> b.qwr <=> a.qwr }
		ret.eachWithIndex { TopProgAlbum it, int i -> it.rank = i+1 }
		
		return ret
	}
	
	List<TopProgAlbum> getAlbums() {
		final urls = []
		println System.getProperty("user.dir")
		def dir = new File("data/RateYourMusic")
		if (! dir.exists()) {
			dir = new File("../${dir}")
		}
		dir.eachFileRecurse { File file ->
			if (file.name.endsWith(".html")) {
				urls << file.toString()
			}
		}
		def albums = getAlbums(urls)
//		albums = albums.findAll { TopProgAlbum it ->
//			it.nofRatings >= 25 /*&& it.qwr.toFloat() >= 3.5f*/ }.toList()
		return albums
	}
	
	static List<TopProgAlbum> deduplicate(List<TopProgAlbum> albums) {
		final ret = albums.groupBy { TopProgAlbum it -> 
			it.artist + " " + it.title
		}
		.collect { name, List<TopProgAlbum> list ->
			int vmax=0, imax=0
			list*.nofRatings.eachWithIndex { int n, int i ->
				if (n > vmax) {
					vmax = n
					imax = i
				}
			}
			return list[imax]
		}
		println "${ret.size()} albums after deduplication"
		return ret
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	static main(args) {
		final reader = new TopRymReader()
		def albums = reader.getAlbums()
		for (album in albums.subList(0, 100)) { println album }
		println "${albums.size()} albums"
		final genres = new TreeSet();
		albums*.genres.each { genres.addAll(it) }
		println "${genres.size()} unique genres"
		//genres.each { println it }
		final artists = albums*.artist.unique().sort()
		println "${artists.size()} unique artists"
	}

}

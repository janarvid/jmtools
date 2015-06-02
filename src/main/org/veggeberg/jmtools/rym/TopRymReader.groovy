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
			else {
				map[k.toString()] = v
			}
			if (k == 'artist') map.artistUrl = URL_PREFIX + it.@href.toString()
			if (k == 'album') map.albumUrl = URL_PREFIX + it.@href.toString()
		}
//		println "map = ${map}"
		TopProgAlbum data = new TopProgAlbum()	
		data.artist = map.artist;
		data.title = map.album;
		data.year = map.mediumg.substring(1, 5).toInteger()
		data.genre = map.genres[0]
		data.qwr = extractField(trString, "^.*Avg rating:").toFloat()
		data.artistUrl = map.artistUrl
		data.albumUrl = map.albumUrl
		//data.nofRatings = extractField(trString, "^.*Ratings:").replaceAll(',','').toFloat()
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
		
		// UTF-8 is encoded in the file and it's wrong.  Just ignore the char set and it will default to ISO-8859-1
		parser.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", true)
		
		def html = new XmlSlurper(parser).parse(url)
		def table = findDataTable(html);
		//println table.TR[0];
		def header;
		def albums = []
		for (i in 0..table.TR.size()-1) {
			def tr = table.TR[i]
//			println "tr = $tr"
			final data = getData(header, tr)
			if (data == null) break
			albums << data;
//			if (i > 3) break
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
		
		// Sort albums and change the rank
		ret = ret.sort { TopProgAlbum a, TopProgAlbum b -> b.qwr <=> a.qwr }
		for (i in 0..<ret.size()) {
			ret[i].rank = i+1
			//if (i>10) break
		}
		
		return ret
	}
	
	List<TopProgAlbum> getAlbums() {
		final urls = []
		println System.getProperty("user.dir")
		def dir = new File("data/RateYourMusic")
		if (! dir.exists()) {
			dir = new File("../${dir}")
		}
		dir.eachFile { File file ->
			if (file.name.startsWith("top-rym-albums-")) {
				urls << file.toString()
			}
		}
		return getAlbums(urls)
	}

	static main(args) {
		final reader = new TopRymReader()
		def albums = reader.getAlbums()
		for (album in albums) {
			println album
		}
		final genres = albums*.genre.unique().sort()
		println genres
		println "${genres.size()} unique genres"
		final artists = albums*.artist.unique().sort()
		println "${artists.size()} unique artists"
	}

}

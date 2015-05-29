package org.veggeberg.jmtools.progarchives

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

import org.cyberneko.html.parsers.SAXParser
import org.veggeberg.jmtools.domain.TopProgAlbum;

@CompileStatic
class TopProgAlbumsReader
{	
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
	def getHeader(def tr) {
		def header = [];
		def replaceMap = ['covers':'cover']
		tr.TD.each {
			def attr = it.toString().toLowerCase().replaceAll(' ','')
			attr = (replaceMap.get(attr)) ? replaceMap.get(attr) : attr
			header << attr
		}
		return header;
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	static TopProgAlbum getData(List header, def tr) {
		final data = new TopProgAlbum()
		def i = 0;
		tr.TD.each {
			def attr = header[i++];
			switch (attr) {
			case "cover":
				data.cover = it.IMG.@src
				break;
			case "titleartist":
				def title = it.A.STRONG.toString().trim()
				data.title = title
				String aText = it.A.text().trim()
				data.artist = aText[title.length()..aText.length()-1].trim()
				data.albumUrl = 'http://www.progarchives.com/' + it.A[0].@href
				data.artistUrl = 'http://www.progarchives.com/' + it.A[1].@href
				break;
			case "ratings":
				def text = it.text().trim()
				//println "ratings text = ${text}"
				def indq = text.indexOf('QWR = ')
				//assert (indq > 0)
				indq += 6
				def indGen = text.indexOf("generateQuickRatingStarbox")
				//assert (indGen > 0)
				data.qwr = text.substring(indq, indGen-1).trim()
				//println "qwr = '${data.qwr}'"
				break;
			case "genre(recordtype)":
				//println it.text()
				def genre = it.STRONG.toString()
				data.genre = genre
				def indComma = it.toString().indexOf(',')
				def year = Integer.valueOf(it.toString().substring(indComma+1).trim());
				data.year = year					
				data.albumType = it.toString().replaceAll(genre, '').trim()			
				break;
			case "buy":
				break // Just ignore this one
			case "rank":
				data.rank = it.toString().toInteger()
				break
			default:
				data."$attr" = it.toString().trim()
			}
		}
		return data
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
			if (i == 0) {
				header = getHeader(tr)
				//println "header = $header"
			}
			else {
				def data = getData(header, tr)
				//println "data = $data"
//                if (data.album_type != 'DVD / Video' && data.rating >= 3.5) {
//                	albums << data;
//                }
				albums << data;
			}
		}
		return albums
	}
	
	List<TopProgAlbum> getAlbums(List<String> urls)
	{
		final List<TopProgAlbum> ret = []
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
		def dir = new File("data/progarchives")
		if (! dir.exists()) {
			dir = new File("../${dir}")
		}
		dir.eachFile { File file ->
			if (file.name.startsWith("top-prog-albums-")) {
				urls << file.toString()
			}
		}
		return getAlbums(urls)
	}

//	@CompileStatic(TypeCheckingMode.SKIP)
	static main(args) {
//		def urls = []
//		new File("test/resources/progarchives").eachFile { File file ->
//			if (file.name.startsWith("top-prog-albums-")) {
//				urls << file.toString()
//			}
//		}
//		urls << 'test/resources/progarchives/top-prog-albums-2010.html'
//		urls << 'test/resources/progarchives/top-prog-albums-2011.html'
//		urls = ['test/resources/progarchives/top-prog-albums-2013.html']
//		urls << 'test/resources/progarchives/top-prog-albums-2012.html'
		TopProgAlbumsReader reader = new TopProgAlbumsReader()
//		def albums = reader.getAlbums(urls)
		def albums = reader.getAlbums()
		for (album in albums) {
			println album
		}
		final genres = albums*.genre.unique().sort()
		println genres
		final artists = albums*.artist.unique().sort()
		println "${artists.size()} artists"
	}
}

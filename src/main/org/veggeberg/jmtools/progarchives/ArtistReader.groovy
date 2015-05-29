package org.veggeberg.jmtools.progarchives

import java.util.List;

import org.blinkenlights.jid3.ID3Tag;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.cyberneko.html.parsers.SAXParser;

class ArtistReader {
	
	static findDataTable(def html) {
		def tables = html.'**'.findAll{it.name() == 'TABLE'}
		def table = null
		for (t in tables) {
			if (t.TR.size() > 50) {
				table = t
				break;
			}
		}
		return table;
	}
	
	static getHeader(tr) {
		List header = [];
		def replaceMap = [bands:'artist', style: 'genre']
		tr.TD.each {
			def attr = it.toString().toLowerCase().replaceAll(' ','')
			attr = replaceMap.get(attr, attr)
			header << attr
		}
		return header;
	}
	
	static getData(header, tr) {
		def data = [:]
        def i = 0;
        tr.TD.each {
			def attr = header[i++]
			//println it
			if (attr == 'artist') {
				data.id = it.A.@href.toString().replaceAll('.*=','')
			}
			data[attr] = it.text()
        }
		return data
	}
	
	static readArtists(String url)
	{
		println "url = $url"
		def html = new XmlSlurper(new SAXParser()).parse(url)
		def table = findDataTable(html);
		//println table.TR[0];
		def header;
		List artists = []
		for (i in 0..table.TR.size()-1) {
			def tr = table.TR[i]
			if (i == 0) {
				header = getHeader(tr)
				//println "header = $header"
			}
			else {
				def artist = getData(header, tr)
				//println "artist = $artist"
				artists << artist
			}
			//if (i>10) break
		}
		return artists
	}
	
	static readArtistDirs(File topDir) {
		def ret = []
		for (dir in topDir.listFiles()) {
			println dir
			ret << dir
		}
		return ret
	}
	
	static void adjustGenreForArtists(dirs, artists) {
		for (dir in dirs) {
			def artistDir = dir.name
			def artist = artists.find { it.artist == artistDir.toString().toUpperCase()}
			println "${artistDir} ${artist}"
			if (artist) {
				adjustGenreForArtist(dir, artist.genre)
			}
		}
	}
	
	static void adjustGenreForArtist(dir, genre) {
		dir.eachFileRecurse { file ->
			if ( ! file.toString().endsWith('.mp3') ) return
			println "  ${file}"
			
			// create an MP3File object representing our chosen file
			MediaFile oMediaFile = new MP3File(file);
			println oMediaFile
			ID3Tag[] aoID3Tags = oMediaFile.getTags();
			for (tag in aoID3Tags) {
				println tag
				print tag.getClass() 
				println tag.genre.getClass()
			}
		}
	}
	
	static void main(args) {
		println "heia"
		/*
		def artists = readArtists('src/main/resources/progarchives/artists.html')
		//def dirs = readArtistDirs(new File('/media/Nokia N900/.sounds/Music'))
		def dirs = readArtistDirs(new File('/home/audio/Music'))
		adjustGenreForArtists(dirs, artists)
		*/
		
		adjustGenreForArtist(new File('/home/audio/Music/Mastodon'), 'Tech/Extreme Prog Metal')
	}
}

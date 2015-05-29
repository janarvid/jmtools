package org.veggeberg.jmtools.progarchives

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

//@CompileStatic
class AlbumMatcher extends BaseMatcher
{
	static Map<String,String> albumReplaceMap
	private artistDir
	private albumDirs
	private additionalEncodings
	
	static {
		albumReplaceMap = new LinkedHashMap(replaceMap)
		albumReplaceMap.putAll([
			'pami\\?\\?': 'pami', // SBB
			'\\?': 'question mark', // e.g. Neal Morse ?
		])
	}
	
	AlbumMatcher(File artistDir) {
		this.artistDir = artistDir
		additionalEncodings = []
	}
	
	void setAdditionalEncodings(additionalEncodings) {
		this.additionalEncodings = additionalEncodings
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	public getAlbumDirs() {
		if (albumDirs == null) {
			albumDirs = []
			artistDir.eachDir{ albumDirs << it }
		}
		return albumDirs
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	File findAlbumDir(album) {
		def title = album.title.toLowerCase()
		def ret = null
		for (albumDir in getAlbumDirs()) {
			def dir = albumDir.name.toLowerCase();
			def year = dir.replaceFirst(/[- ].*$/, '').trim()
			if (year.isInteger()) year = year.toInteger()
			dir = dir.replaceFirst(/^[12][0-9][0-9][0-9][ -]*/,'')
			dir = dir.replaceFirst(/\(ogg\)/, '').trim()
			for (enc in additionalEncodings) {
				dir = dir.replaceFirst("\\(${enc}\\)", '').trim()
			}
			dir = dir.trim()
			if (title == dir) {
				ret = albumDir; break;
			}
			for (repl in albumReplaceMap) {
				dir = dir.replaceAll(repl.key, repl.value)
				title = title.replaceAll(repl.key, repl.value)
				if (title == dir) {
					ret = albumDir; break;
				}
			}
			dir = dir.trim()
			title = title.trim()
			String pat = /[:\-_ \.'?,\(\)]/
			dir = dir.replaceAll(pat, '')
			title = title.replaceAll(pat, '')
			//println "d=$d, title=$title"
			if (title == dir) {
				ret = albumDir; break;
			}
		}
		
		if (ret == null) { // Try to match the year
			def albumDirsByYearMap = [:]
			for (albumDir in getAlbumDirs()) {
				def dir = albumDir.name.toLowerCase();
				def year = dir.replaceFirst(/[- ].*$/, '').trim()
				if (year.isInteger()) year = year.toInteger()
				def albumDirsThatYear = albumDirsByYearMap.get(year, [])
				albumDirsThatYear << albumDir
			}
			def albumDirsForYear = albumDirsByYearMap[album.year]
			if (albumDirsForYear?.size() == 1){
				ret = albumDirsForYear[0]
			}
		}
		return ret
	}
}

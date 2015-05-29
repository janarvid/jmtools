package org.veggeberg.jmtools.collection

import java.io.File;
import java.util.List;
import java.util.Map;

import org.veggeberg.jmtools.domain.TopProgAlbum;
import org.veggeberg.jmtools.progarchives.AlbumMatcher;

import groovy.transform.CompileStatic;

@CompileStatic
class AbstractCollectionBuilder 
{
	// Constraints
	Map<String, List<String>> excludeArtists = [:]
	List<String> includeArtists = []
	Integer maxNoOfAlbums;
	List<StorageFolder> storageFolders
	Map<String, Long> dirSizeByArtist = new TreeMap()
	static final long GB = 1024L * 1024L * 1024L
	static final long MB = 1024L * 1024L
	
	List<?> missingAlbums = []
	Map<String,List<?>> missingArtists = [:]
	File topDir
	
	File findAlbumDir(File artistDir, TopProgAlbum album) {
		def albumMatcher = new AlbumMatcher(artistDir)
		return albumMatcher.findAlbumDir(album);
	}
	
	List<String> findLetterLimits() {
		//def sizeByLetter = sizeLimits.collectEntries { [] }
		List<Map<String,Long>> ret = []
		ret.add([:])
		int currentLimit = 0
		dirSizeByArtist.each { String artist, long dirSize ->
			Map<String,Long> artistMap = ret[currentLimit]
			final dirSizes = (Long)artistMap.values().sum()
			final sizeLimit = (currentLimit<storageFolders.size()) ? storageFolders[currentLimit].sizeLimit : Long.MAX_VALUE
			if (dirSizes > sizeLimit) {
				currentLimit++
				ret.add([(artistMap.keySet().last()):artistMap.values().last()])
				artistMap.remove(artistMap.keySet().last())
				artistMap = ret[currentLimit]
			}
			final z = artistMap.get(artist, 0L)
			artistMap[artist] = z + dirSize
		}
		return ret
	}
	
	Long getSizeLimitTotal() {
		final sizeLimits = storageFolders*.sizeLimit
		return (Long)(sizeLimits) ? sizeLimits.sum() : null
	}

	public AbstractCollectionBuilder() {
		// TODO Auto-generated constructor stub
	}
}

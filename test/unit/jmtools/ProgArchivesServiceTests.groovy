package jmtools

import jmtools.test.MyGriffonUnitTC;
import griffon.test.*

class ProgArchivesServiceTests extends MyGriffonUnitTC 
{
	ProgArchivesService cut;
	
    protected void setUp() {
        super.setUp()
		cut = new ProgArchivesService();
		cut.app = app
		cut.jmtoolsConfigService = new JmtoolsConfigService()
		cut.jmtoolsConfigService.app = app
		cut.serviceInit()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void tstGetTopAlbums() {
		def tbeg = System.currentTimeMillis()
		final topAlbums = cut.getTopAlbums()
		final dt = System.currentTimeMillis()-tbeg
		println "After ${topAlbums.size()}"
		for (a in topAlbums.subList(0, 10)) {
			println a
		}
		println "Elapsed time = $dt"
	}

    void testGetTopAlbumsWithPath() {
		def tbeg = System.currentTimeMillis()
        def topAlbums = cut.getTopAlbumsWithPath(10)
		topAlbums.each { album ->
//			println "${album.artist} ${album.year} ${album.title} ${album.artistPath} ${album.albumPath}"
		}
		final dt = System.currentTimeMillis()-tbeg
		println "Elapsed time = $dt"
		
		println "Before ${topAlbums.size()}"
		topAlbums = cut.getTopAlbums()
		topAlbums
		.findAll { album -> album.artist.contains(/mpano/) }
		.each { album ->
			println "${album.artist} ${album.year} ${album.title} ${album.artistPath} ${album.albumPath}"
		}
		println "After ${topAlbums.size()}"
//		println topAlbums
		
		tbeg = System.currentTimeMillis()
		topAlbums = cut.getTopAlbumsWithPath(250)
		topAlbums
		.findAll { album -> album.artist.contains(/mpano/) }
		.each { album ->
//			println "${album.artist} ${album.year} ${album.title} ${album.artistPath} ${album.albumPath}"
		}
		dt = System.currentTimeMillis()-tbeg
		println "Elapsed time = $dt"
    }
	
	void testGetMissingArtistsAndAlbums() {
		final topAlbums = cut.getTopAlbumsWithPath(300)
		final albumsByArtist = cut.getMissingArtistsAndAlbums(topAlbums)
		albumsByArtist.each {
			println it
		}
	}
	
	void testFormatIsSupported() {
		final fileNameByFlagMap = [
			"Yes/1971 - Close to the edge": true,
			"Yes/1971 - Close to the edge(FLAC)": false
		]
		fileNameByFlagMap.each { fn, flag ->
			assertEquals("Should have been $flag for '$fn'", flag, cut.formatIsSupported(fn))
		}
	}
}

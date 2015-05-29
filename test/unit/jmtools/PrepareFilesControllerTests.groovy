package jmtools

import org.veggeberg.jmtools.domain.Album;

import jmtools.test.MyGriffonUnitTC;
import griffon.core.MVCGroup;
import griffon.test.*

class PrepareFilesControllerTests extends MyGriffonUnitTC {
	PrepareFilesController cut
    protected void setUp() {
        super.setUp()
		MVCGroup mvcg = app.buildMVCGroup("prepareFiles")
		cut = mvcg.controller
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
		ProgArchivesService progArchivesService = new ProgArchivesService();
		progArchivesService.app = app
		progArchivesService.jmtoolsConfigService = new JmtoolsConfigService()
		progArchivesService.jmtoolsConfigService.app = app
		progArchivesService.serviceInit()
        final topAlbums = progArchivesService.getTopAlbums()
		final artist = 'Miriodor'
		final albums = topAlbums.findAll { it.artist == artist }.collect { new Album(name: it.title, year: it.year) }
		final workAreaTopDir = progArchivesService.jmtoolsConfigService.workAreaTopDir
		println "workAreaTopDir=$workAreaTopDir"
		final dirs = new File("${workAreaTopDir}/$artist").list().collect { [name: it] }
		//cut.autoSetAlbumDirs(artist, albums, dirs)
		//dirs.each { println it }
    }
}

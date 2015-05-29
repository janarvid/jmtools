package jmtools

import java.util.Collection;

import javax.swing.JTable;

import groovy.transform.CompileStatic
import jmtools.controllers.AbstractJMToolsController

import org.apache.commons.io.FileUtils
import org.veggeberg.jmtools.Global
import org.veggeberg.jmtools.domain.Album;

class FindFilesController extends AbstractJMToolsController {
    // these will be injected by Griffon
    FindFilesModel model
    FindFilesView view
	JmtoolsConfigService jmtoolsConfigService

    void mvcGroupInit(Map args) {
        
    }

    // void mvcGroupDestroy() {
    //    // this method is called when the group is destroyed
    // }

    /*
        Remember that actions will be called outside of the UI thread
        by default. You can change this setting of course.
        Please read chapter 9 of the Griffon Guide to know more.
       
    def action = { evt = null ->
    }
    */
	
	def refreshWorkDirs = { evt = null ->
		app.event(Global.EVENT_REFRESH_WORK_DIRS)
		doRefreshWorkDirs()
	}
	
	void doRefreshWorkDirs() {
		println "Info doRefreshWorkDirs()"
		doRefreshDir(new File(model.workDirPath), model.workDirAlbums)
		autoSetAlbumDirs(model.currentArtist.name, model.albums, model.workDirAlbums)
		((JTable)view.workDirAlbumTable).model.fireTableDataChanged()
	}
	
	def findOnDisk = { evt = null ->
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ['Searching...'])
			doSearch()
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ['Searching finished.'])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	
	def onChangeSelectedArtist = { artistName ->
		println "${getClass().name}.onChangeSelectedArtist msg = $artistName"
		setSearchPattern(artistName)
		model.findResults.clear()
		doRefreshWorkDirs()
	}
	
	def onRefreshWorkDirs = {
		doRefreshWorkDirs()
	}
	
	@CompileStatic
	void setSearchPattern(String artistName) {
		model.searchPattern = artistName.replaceAll(/[ -][ -]*/, /\.\.*/)
		.replaceAll(/[']/, /\.*/)
	}
	
	@CompileStatic
	private doSearch() {
		model.findResults.clear()
		final dirs = [jmtoolsConfigService.sourceArchivesTopDir, jmtoolsConfigService.workAreaTopDir]
		final searchPattern = model.searchPattern.toLowerCase()
		for (dir in dirs) {
			dir.eachFile { File file ->
				if (file.toString().toLowerCase() =~ searchPattern) {
					model.findResults << toFoundEntry(file)
					if (file.isDirectory()) {
						file.eachFile { File subFile ->
							model.findResults << toFoundEntry(subFile)
						}
					}
				}
			}
		}
		jmtoolsConfigService.collectionTopDir.eachFile { File letterDir ->
			if (!letterDir.isDirectory()) return
			final letter = letterDir.name
			if (searchPattern.contains(letter) || letter == 't') { // t for the
//				println "searching for letter $letterDir"
				letterDir.eachDir { File artist ->
					if (!artist.isDirectory()) return
					if (artist.toString().toLowerCase() =~ searchPattern) {
						model.findResults << toFoundEntry(artist)
						artist.eachDir { File album ->
							model.findResults << toFoundEntry(album)
						}
					}
				}
			}
		}
	}
	
	@CompileStatic
	private toFoundEntry(File file) {
		final z = (file.isDirectory()) ? 'dir' : file.length()
		final ret = [name: file.toString(), size:z, date: file.lastModified()]
		return ret
	}
	
	void moveToWorkDir(evt = null) {
		int numOfItemsMoved = 0
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Moving data to workDir..."])
			final selRows = getSelectedRows(view.findResultsTable)
			for (i in selRows) {
				final arkFile = new File(model.findResults[i].name)
				final toDir = new File(model.workDirPath)
				if ( ! toDir.exists() ) {
					assert toDir.mkdir()
				}
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["Moving '$arkFile' --> '${toDir}'..."])
				FileUtils.moveFileToDirectory(arkFile, toDir, false)
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["   Done.\n"])
				numOfItemsMoved++
			}
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Updating work dir table..."])
			doSearch()
			app.event(Global.EVENT_REFRESH_WORK_DIRS)
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["${numOfItemsMoved} items moved."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	
	// TODO Move to services
	@CompileStatic
	static void autoSetAlbumDirs(String artist, Collection<Album> albums, Collection<Map<String,?>> albumFileEntries) {
		String artistLower = artist.toLowerCase()
		final sortedAlbums = albums.findAll { Album it -> it.name != artist }
		sortedAlbums.sort { Album a, Album b -> b.name.length() <=> a.name.length() }
		final selfTitled = albums.find { Album it -> it.name == artist }
		if (selfTitled) sortedAlbums << selfTitled
		for (Map<String,?> afe in albumFileEntries) {
			String afeName = ((String)afe['name']).toLowerCase()
			List<Album> albumMatches = []
			for (album in sortedAlbums) {
				String pattern = toSearchPattern(album.name)
				println "pattern=$pattern"
				if (afeName =~ pattern) {
					albumMatches << album
				}
			}
			if (albumMatches.size() >= 1) {
				afe.albumDir = "${albumMatches[0].year} - ${albumMatches[0].name}"
			}
		}
//		if (afeName.startsWith(artistLower)) {
//			afeName = afeName.replaceFirst(artistLower, "")
//		}
	}
	
	@CompileStatic
	static String toSearchPattern(String albumName) {
		final ret = albumName.replaceAll(/[ -\.][ -\.]*/, /\.\.*/)
		.replaceAll(/['!]/, /\.*/)
		.toLowerCase()
		return ret
	}
}

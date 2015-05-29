package jmtools

import griffon.core.GriffonApplication
import griffon.transform.Threading
import groovy.transform.CompileStatic
import groovy.transform.Synchronized
import groovy.transform.TypeCheckingMode

import java.awt.Desktop
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

import jmtools.controllers.AbstractJMToolsController

import org.apache.commons.io.FileUtils
import org.veggeberg.jmtools.Global
import org.veggeberg.jmtools.domain.Artist

//@CompileStatic
class TopProgMissingController extends AbstractJMToolsController {
    // these will be injected by Griffon
	GriffonApplication app
    TopProgMissingModel model
    TopProgMissingView view
	ProgArchivesService progArchivesService
	JmtoolsConfigService jmtoolsConfigService
	boolean refresingArtistTable = false
	final numOfUniqArtists = 300

	//@CompileStatic(TypeCheckingMode.SKIP)
    //void mvcGroupInit(Map args) {
		//populateArtistTable() // Calling populateArtistTable crashed
		/*
		final topAlbums = progArchivesService.getTopAlbumsWithPath(numOfUniqArtists)
		final missingArtistsAndAlbums = progArchivesService.getMissingArtistsAndAlbums(topAlbums)
		for (artist in missingArtistsAndAlbums) {
			model.artists << artist
		}
		*/
    //}

	@CompileStatic
	void setSearchPattern(String artistName) {
		model.searchPattern = artistName.replaceAll(/[ -][ -]*/, /\.\.*/)
		.replaceAll(/[']/, /\.*/)
	}
	
	@Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
	def refreshArtistTable = { evt = null ->
		refresingArtistTable = true
//		model.artistTable.selectionModel.removeListSelectionListener(artistSelect)
		populateArtistTable()
		refresingArtistTable = false
//		model.artistTable.selectionModel.addListSelectionListener(artistSelect)
	}
	
	void populateArtistTable() {
		model.artists.clear()
		final topAlbums = progArchivesService.getTopAlbumsWithPath(numOfUniqArtists)
		final missingArtistsAndAlbums = progArchivesService.getMissingArtistsAndAlbums(topAlbums)
		for (artist in missingArtistsAndAlbums) {
			println "artist = $artist"
			model.artists << artist
		}
	}
	
	def artistSelect = { ListSelectionEvent evt = null ->
		if (refresingArtistTable) return
		final selRows = getSelectedRows(model.artistTable)
		if (selRows.size() == 0) return
		
		final selRow = selRows[0]
//		println "selected: ${evt.toString()}"
		Artist artist = model.artists[selRow]
		if (artist == null) return
		model.currentArtist = artist 
		final albums = artist.albums
		model.albums.clear()
		for (album in albums) {
			model.albums.add(album)
		} 
		setSearchPattern(artist.name)
		app.event(Global.EVENT_CHANGE_SELECTED_ARTIST, [artist.name])
		app.event(Global.EVENT_REFRESH_WORK_DIRS)
	} as ListSelectionListener

	void setCurrentArtistDir() {
		model.currentArtistDir = model.currentArtist.name 
	}
	
	def deleteFindResults = { event=null ->
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Deleting..."])
			int ndel = 0
			final selRows = getMinOneSelectedRows(model.findResultsTable)
			for (selRow in selRows) {
				final item = model.findResults[selRow]
				println "item=$item"
				final file = new File("${item.name}")
				println "file = $file"
				if (file.isDirectory()) {
					FileUtils.deleteDirectory(file)
				}
				else {
					assert file.delete()
				}
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["${file} deleted.\n"])
				ndel++
			}
			doSearch()
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["${ndel} entries deleted."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}

	def doubleClickArtist = {
//		println "it=$it"
		MouseEvent me = (MouseEvent)it
		if (me.clickCount == (int)2 && me.ID == MouseEvent.MOUSE_CLICKED) {
//			println "mouse was double clicked"
			final selRow = model.artistTable.selectedRows[0]
//			println "selRow = $selRow"
			final url = new URL((String)model.artists[selRow]["url"])
			Desktop.getDesktop().browse(url.toURI())
		}
	} as MouseListener

	def findOnDisk = { evt = null ->
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Searching..."])
			doSearch()
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Searching finished."])
		} 
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	/*
	def moveToWorkDir = { evt = null ->
		int numOfItemsMoved = 0
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Moving data to workDir..."])
//			def selRows
//			edt {
//				selRows = model.findResultsTable.selectedRows
//			}
			final selRows = getSelectedRows(model.findResultsTable)
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
*/
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
		final z = (file.isDirectory()) ? "dir" : file.length()
		final ret = [name: file.toString(), size:z, date: file.lastModified()]
		return ret
	}
	
	private static Exception lastCaughtException
	@Synchronized
	@CompileStatic
	void onUncaughtExceptionThrown(Exception e) {
		//if (e.message == lastCaughtException?.message) return
		if (e == lastCaughtException) return
		//log.info "Into onUncaughtExceptionThrown.  e = $e, lastCaughtException=${lastCaughtException}"
		lastCaughtException = e
		//log.info "lastCaughtException=${lastCaughtException}"
		app.event(Global.EVENT_SET_ERROR_MESSAGE, [e.message])
	}
}

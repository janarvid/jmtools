package jmtools

import griffon.core.GriffonApplication
import griffon.transform.Threading
import groovy.transform.CompileStatic;

import java.awt.Desktop
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

import jmtools.controllers.AbstractJMToolsController

import org.veggeberg.jmtools.Global
import org.veggeberg.jmtools.domain.Album;
import org.veggeberg.jmtools.domain.Artist

class CollectionManagerController extends AbstractJMToolsController {
	// these will be injected by Griffon
	final static String ALL_GENRES = "-- All --"
	CollectionManagerModel model
	CollectionManagerView view
	GriffonApplication app
	ProgArchivesService progArchivesService
	JmtoolsConfigService jmtoolsConfigService

	boolean refresingArtistTable = false
	final numOfUniqArtists = 333

	void mvcGroupInit(Map args) {
		app.log.info("Into mvcGroupInit($args)")
		populateGenresCB();
		populateArtistTable() // Calling populateArtistTable crashed
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

	@Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
	def refreshArtistTable = { evt = null ->
		refresingArtistTable = true
		//		model.artistTable.selectionModel.removeListSelectionListener(artistSelect)
		populateArtistTable()
		refresingArtistTable = false
		//		model.artistTable.selectionModel.addListSelectionListener(artistSelect)
	}

	@CompileStatic	
	void populateGenresCB() {
		final genres = progArchivesService.getAllGenres()
		model.genres.addAll(ALL_GENRES);
		model.genres.addAll(genres);
		model.selectedGenre = ALL_GENRES
	}

	void populateArtistTable() {
		app.log.info("populateArtistTable()")
		model.artists.clear()
		final genres = (model.selectedGenre in [ALL_GENRES,null]) ? null : [model.selectedGenre]
		final topAlbums = progArchivesService.getTopAlbumsWithPath(numOfUniqArtists, genres)
		final missingArtistsAndAlbums = progArchivesService.getMissingArtistsAndAlbums(topAlbums)
		model.artists.addAll(missingArtistsAndAlbums)
		//		for (artist in missingArtistsAndAlbums) {
		//			//println "artist = $artist"
		//			model.artists << artist
		//		}
	}

	def artistSelect = { ListSelectionEvent evt = null ->
		if (refresingArtistTable) return
			final selRows = getSelectedRows(view.artistTable)
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
		app.event(Global.EVENT_CHANGE_SELECTED_ARTIST, [artist.name])
	} as ListSelectionListener

	@Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
	def selectGenre = {
		println "Into selectGenre()"
//		refresingArtistTable = true
//		populateArtistTable()
//		refresingArtistTable = false
	}

	def albumSelect = { ListSelectionEvent evt = null ->
		if (refresingArtistTable) return
		final selRows = getSelectedRows(view.albumTable)
		if (selRows.size() == 0) return

		final selRow = selRows[0]
		//		println "selected: ${evt.toString()}"
		Album album = model.albums[selRow]
		if (album == null) return
		model.currentAlbum = album
		app.event(Global.EVENT_CHANGE_SELECTED_ALBUM, [album])
	} as ListSelectionListener

	def doubleClickArtist = {
		//		println "it=$it"
		MouseEvent me = (MouseEvent)it
		if (me.clickCount == (int)2 && me.ID == MouseEvent.MOUSE_CLICKED) {
			//			println "mouse was double clicked"
			final selRow = getSelectedRows(view.artistTable)[0]
			//			println "selRow = $selRow"
			final url = new URL((String)model.artists[selRow]["url"])
			Desktop.getDesktop().browse(url.toURI())
		}
	} as MouseListener

	def doubleClickAlbum = {
		//		println "it=$it"
		MouseEvent me = (MouseEvent)it
		if (me.clickCount == (int)2 && me.ID == MouseEvent.MOUSE_CLICKED) {
			//			println "mouse was double clicked"
			final selRow = getSelectedRows(view.albumTable)[0]
			//			println "selRow = $selRow"
			final url = model.albums[selRow].url
			Desktop.getDesktop().browse(url.toURI())
		}
	} as MouseListener
}

package jmtools

import java.util.Collection;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.io.FileUtils;
import org.veggeberg.jmtools.Global;
import org.veggeberg.jmtools.MyFileUtils;
import org.veggeberg.jmtools.domain.Album;
import org.veggeberg.jmtools.domain.TagInfo;

import griffon.transform.Threading;
import groovy.transform.CompileStatic;
import groovy.transform.Synchronized;
import jmtools.controllers.AbstractJMToolsController;

class WorkAreaController extends AbstractJMToolsController {
    // these will be injected by Griffon
    WorkAreaModel model
    WorkAreaView view
	JmtoolsConfigService jmtoolsConfigService
	ExternalProcessService externalProcessService
	PrepareFilesService prepareFilesService
	TaggerService taggerService

    void mvcGroupInit(Map args) {
        // this method is called after model and view are injected
		println "Into mvcGroupInit($args)"
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
	}

	def moveToCollectionDir = { evt = null ->
		int numOfItemsMoved = 0
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Moving data to Collection Dir..."])
			final selRows = getMinOneSelectedRows(view.workDirAlbumTable,
				"Please select at least 1 row from the work dir table")
			final toDir = model.collectionDirPath
			if ( ! toDir.exists() ) {
				assert toDir.mkdir()
			}
			for (i in selRows) {
				final fromDir = new File("${model.workDirPath}${File.separator}${model.workDirAlbums[i].name}")
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["Moving '$fromDir' --> '${toDir}'..."])
				FileUtils.moveDirectoryToDirectory(fromDir, toDir, false)
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["   Done.\n"])
				numOfItemsMoved++
			}
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Updating tables..."])
			doRefreshWorkDirs()
			doRefreshDir(model.collectionDirPath, model.collectionDirs)
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["${numOfItemsMoved} items moved."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	
	def refreshCollectionDirs = { evt = null ->
		doRefreshDir(model.collectionDirPath, model.collectionDirs)
	}
		
	@Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
	def setAlbumDirInRow = { evt = null ->
		println "Into setAlbumDirInRow"
//		final selRows = model.workDirAlbumTable.selectedRows
		final selRows = getSelectedRows(view.workDirAlbumTable)
		if (selRows.size() > 0) {
			final selRow = selRows[0]
			final albumFileEntry = model.workDirAlbums[selRow]
			albumFileEntry.albumDir = model.albumDir
		}
		view.workDirAlbumTable.model.fireTableDataChanged()
		edt {
			for (row in selRows) {
				view.workDirAlbumTable.getSelectionModel().setSelectionInterval(row, row)
			}
		}
	}
	
	//@CompileStatic
	void unpackArchives(event=null) {
		final selRows = getMinOneSelectedRows((JTable)view.workDirAlbumTable,
			"Please select at least 1 row from the work area table")
		if (selRows == 0) return
//		def selRows
//		edt {
//			selRows = model.workDirAlbumTable.selectedRows
//			if (selRows.size() == 0) {
//				JOptionPane.showMessageDialog(null, "Please select at least 1 row from the work area table")
//				return
//			}
//		}
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Unpacking..."])
			for (selRow in selRows) {
				final wda = model.workDirAlbums[selRow]
						println "wda=$wda"
						final srcArk = new File("${model.workDirPath}/${wda.name}")
				final destDir = new File("${model.workDirPath}/${wda.albumDir}")
				app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Unpacking ${wda.name}..."])
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["Unpacking '${wda.name}' into '$destDir'...\n"])
				prepareFilesService.unpackArchive(srcArk, destDir)
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["Unpack of '${wda.name}' finished."])
			}
			app.event(Global.EVENT_REFRESH_WORK_DIRS)
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Unpack finished..."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	
	@CompileStatic
	void launchTagger(evt = null) {
		final et = "C:\\Program Files (x86)\\EasyTAG\\bin\\easytag.exe"
		final path = new File(model.workDirPath.toString())
		List<String> cmd = [et, path.toString()]
		externalProcessService.executeCommand(path, cmd, model)
	}
	
	void deleteWorkDirEnties(event=null) {
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Deleting..."])
			int ndel = 0
			final selRows = getSelectedRows(view.workDirAlbumTable)
			for (selRow in selRows) {
				final wda = model.workDirAlbums[selRow]
						println "wda=$wda"
						final file = new File("${model.workDirPath}/${wda.name}")
				if (file.isDirectory()) {
					FileUtils.deleteDirectory(file)
				}
				else {
					assert file.delete()
				}
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["Deleted '$file'.\n"])
				ndel++
			}
			app.event(Global.EVENT_REFRESH_WORK_DIRS)
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["${ndel} entries deleted."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	
	void renameWorkDirEnties(event=null) {
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Renaming..."])
			int nren = 0
			final selRows = getSelectedRows(view.workDirAlbumTable)
			app.log.info "model.workDirAlbums=${model.workDirAlbums}"
			if (selRows.collect { model.workDirAlbums[it].albumDir }.contains(null)) {
				JOptionPane.showMessageDialog(null, "All entries for renaming must have AlbumDir set")
				return
			}
			for (selRow in selRows) {
				final wda = model.workDirAlbums[selRow]
				println "wda=$wda"
				final fromFile = new File("${model.workDirPath}/${wda.name}")
				final toFile = new File("${model.workDirPath}/${wda.albumDir}")
				assert fromFile.renameTo(toFile)
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["Renamed '$fromFile' --> '${toFile}'.\n"])
				nren++
			}
			app.event(Global.EVENT_REFRESH_WORK_DIRS)
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["${nren} entries renamed."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	
	def albumSelect = { ListSelectionEvent evt = null ->
		final selRows = getSelectedRows(view.workDirAlbumTable)
		println "selRows = ${selRows}"
		if (selRows.size() > 0) {
			final selRow = model.workDirAlbums[selRows[0]]
			println "selRow = ${selRow}"
			if (selRow) {
				final selAlbums = getSelectedRows(view.albumTable)
				if (selAlbums.size() > 0) {
					Album album = model.collectionManagerModel.albums[selAlbums[0]]
					setAlbumDir(album)
				}
				final albumPath = new File("${model.workDirPath}${File.separator}${selRow.name}")
				if (albumPath.isDirectory()) {
					model.tags.clear()
					taggerService.fillAlbumTags(albumPath, model.tags)
					model.tags.each {
						println it
					}
				}
			}
		}
	} as ListSelectionListener

	def onChangeSelectedAlbum = { album ->
		println "onChangeSelectedAlbum.  album=$album"
		setAlbumDir(album)
	}

	void setAlbumDir(Album album) {
		if (album) {
			model.albumDir = MyFileUtils.sanitize("${album.year} - ${album.name}")
			File ad = new File(model.albumDir)
			model.tags.clear()
			if (ad.exists() && ad.isDirectory()) {
				taggerService.fillAlbumTags(ad, model.tags)
			}
		}
	}
	
	void correctTags(e=null) {
		final selectedRows = getSelectedRows(view.tagTable)
		for (selRow in selectedRows) {
			TagInfo tagInfo = model.tags[selRow]
			tagInfo.artist = model.currentArtist.name
			tagInfo.genre = model.currentArtist.genre
			tagInfo.album = model.currentAlbum.name
			tagInfo.year = model.currentAlbum.year
			println tagInfo
			taggerService.writeAlbumTag(tagInfo)
			view.tagTable.model.fireTableDataChanged()
		}
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

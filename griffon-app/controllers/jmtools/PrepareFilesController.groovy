package jmtools

import griffon.core.GriffonApplication
import griffon.core.MVCGroup
import griffon.transform.Threading
import groovy.transform.CompileStatic
import groovy.transform.Synchronized

import javax.swing.JOptionPane
import javax.swing.JTable
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

import jmtools.controllers.AbstractJMToolsController

import org.apache.commons.io.FileUtils
import org.veggeberg.jmtools.Global
import org.veggeberg.jmtools.domain.Album

class PrepareFilesController extends AbstractJMToolsController {
    // these will be injected by Griffon
    PrepareFilesModel model
    PrepareFilesView view
	GriffonApplication app
	JmtoolsConfigService jmtoolsConfigService
	ArchiveUnpackerService archiveUnpackerService
	PrepareFilesService prepareFilesService
	ExternalProcessService externalProcessService

//    void mvcGroupInit(Map args) {
//		MVCGroup mvc = app.mvcGroupManager.findGroup('topProgMissing')	
//    }

    // void mvcGroupDestroy() {
    //    // this method is called when the group is destroyed
    // }

    /*
        Remember that actions will be called outside of the UI thread
        by default. You can change this setting of course.
        Please read chapter 9 of the Griffon Guide to know more.
       
     */	
	/*	
	def unpackArchives = { event=null ->
		final selRows = getMinOneSelectedRows(model.workDirAlbumTable,
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
			doRefreshWorkDirs()
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Unpack finished..."])
		} 
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	
	def launchTagger = { evt = null ->
		final et = "C:\\Program Files (x86)\\EasyTAG\\bin\\easytag.exe"
		def cmd = [et, model.workDirPath.toString()]
		externalProcessService.executeCommand(model.workDirPath, cmd, model)
	}
	def deleteWorkDirEnties = { event=null ->
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Deleting..."])
			int ndel = 0
			final selRows = getSelectedRows(model.workDirAlbumTable)
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
			doRefreshWorkDirs()
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["${ndel} entries deleted."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
	
	def renameWorkDirEnties = { event=null ->
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Renaming..."])
			int nren = 0
			final selRows = getSelectedRows(model.workDirAlbumTable)
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
			doRefreshWorkDirs()
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["${nren} entries renamed."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
*/
    /*
	def renameCollectionDirEnties = { event=null ->
		// TODO
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Renaming..."])
			int nren = 0
			final selRows = getMinOneSelectedRows(model.workDirAlbumTable)
			for (selRow in selRows) {
				final wda = model.collectionDirs[selRow]
				println "wda=$wda"
				final fromFile = new File("${model.collectionDirPath}/${wda.name}")
				final toFile = new File("${model.workDirPath}/${wda.albumDir}")
				assert fromFile.renameTo(toFile)
				app.event(Global.EVENT_APPEND_MESSAGE_LOG, ["Renamed '$fromFile' --> '${toFile}'.\n"])
				nren++
			}
			doRefreshWorkDirs()
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["${nren} entries renamed."])
		}
		finally {
			app.event(Global.EVENT_PROGRESS_BAR_DEACTIVATE, [])
		}
	}
     */
	
	@Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
	def MoveToWorkArea = { event=null ->
		//File targetDir = new File("${jmtoolsConfigService.sourceArchivesTopDir}/${model.workArea}")
		model.appendLog "Moving entries from '${model.sourceArchives}' to work area '${model.workAreaDir}'"	
		FileUtils.moveFileToDirectory(model.sourceArchives, model.workAreaDir, true)
		model.appendLog "Done."
	}
	
//	@Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
	def albumSelect = { ListSelectionEvent evt = null ->
		final selRows = getSelectedRows(model.albumTable)
		if (selRows.size() > 0) {
			final selRow = model.albumTable.selectedRows[0]
			println "selected: ${evt.toString()}"
			Album album = model.topProgMissingModel.albums[selRow]
			model.currentAlbum = album
			setAlbumDir(album)
		} 
	} as ListSelectionListener

	void setAlbumDir(Album album) {
		if (album) {
			model.albumDir = "${album.year} - ${album.name}"
		}
	}
	
	def refreshWorkDirs = { evt = null ->
		doRefreshWorkDirs()
	}
	
	def onRefreshWorkDirs = { msg ->
		doRefreshWorkDirs()
	}
	
//	def onChangeSelectedArtist = { msg ->
//		println "PrepareFilesController.onChangeSelectedArtist msg = $msg"
//		doRefreshDir(model.collectionDirPath, model.collectionDirs)
//	}
	
	void doRefreshWorkDirs() {
		doRefreshDir(model.workDirPath, model.workDirAlbums)
		autoSetAlbumDirs(model.currentArtist.name, model.topProgMissingModel.albums, model.workDirAlbums)
		model.workDirAlbumTable.model.fireTableDataChanged()
	}
	
	def refreshCollectionDirs = { evt = null ->
		doRefreshDir(model.collectionDirPath, model.collectionDirs)
	}
	
	/*
	@CompileStatic
	static void doRefreshDir(File dir, List list) {
		list.clear()
		if (dir?.exists()) {
			dir.eachFile { File file ->
				def z = file.length()
				if (file.isDirectory()) {
					final zz = FileUtils.sizeOfDirectory(file)
					zz /= 1024*1024
					z = "dir[${zz}M]"
				}
				list << [name: file.name.toString(), size:z, date: file.lastModified()]
			}
		}
	}
	*/
	
	@Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
	def setAlbumDirInRow = { evt = null ->
		println "Into setAlbumDirInRow"
//		final selRows = model.workDirAlbumTable.selectedRows
		final selRows = getSelectedRows(model.workDirAlbumTable)
		if (selRows.size() > 0) {
			final selRow = selRows[0]
			final albumFileEntry = model.workDirAlbums[selRow]
			albumFileEntry.albumDir = model.albumDir
		}
		model.workDirAlbumTable.model.fireTableDataChanged()
		edt {
			for (row in selRows) {
				model.workDirAlbumTable.getSelectionModel().setSelectionInterval(row, row)
			}
		}
	}
	
	def moveToCollectionDir = { evt = null ->
		int numOfItemsMoved = 0
		try {
			app.event(Global.EVENT_PROGRESS_BAR_ACTIVATE, ["Moving data to Collection Dir..."])
			final selRows = getMinOneSelectedRows(model.workDirAlbumTable,
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
	
	/*
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
	*/
}

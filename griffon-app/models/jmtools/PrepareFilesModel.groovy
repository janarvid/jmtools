package jmtools

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.SortedList;

import org.codehaus.griffon.runtime.core.AbstractGriffonModel;
import org.veggeberg.jmtools.Global;
import org.veggeberg.jmtools.domain.Album;
import org.veggeberg.jmtools.domain.Artist;
import org.veggeberg.jmtools.progarchives.ArtistMatcher;

import ca.odell.glazedlists.EventList;
import griffon.core.GriffonApplication;
import griffon.core.MVCGroup;
import groovy.beans.Bindable
import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

import javax.swing.JTable


//@CompileStatic
class PrepareFilesModel extends AbstractGriffonModel {
	GriffonApplication app
	JmtoolsConfigService jmtoolsConfigService
	
	JTable workDirAlbumTable, albumTable
	@Bindable File sourceArchives
	@Bindable File workAreaDir
	@Bindable String logAreaText
	@Bindable String albumDir
	@Bindable Album currentAlbum
	EventList workDirAlbums = new SortedList( new BasicEventList(),
		{a, b -> a.name <=> b.name} as Comparator)
	EventList collectionDirs = new SortedList( new BasicEventList(),
		{a, b -> a.name <=> b.name} as Comparator)
	ArtistMatcher artistMatcher
	
	@CompileStatic(TypeCheckingMode.SKIP)
	TopProgMissingModel getTopProgMissingModel() {
		MVCGroup mvc = app.mvcGroupManager.findGroup("topProgMissing")
		return mvc.model
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	TopProgMissingModel getCollectionManager() {
		MVCGroup mvc = app.mvcGroupManager.findGroup("collectionManager")
		return mvc.model
	}
	
	File getWorkDirPath() {
		return (topProgMissingModel.workDirPath) ? new File(topProgMissingModel.workDirPath) : null
	}
	
	def getCurrentArtistDir() {
		return topProgMissingModel.currentArtistDir
	}
	
	Artist getCurrentArtist() {
		return getCollectionManager().currentArtist
	}
	
	ArtistMatcher getArtistMatcher() {
		if (artistMatcher == null) {
			artistMatcher = new ArtistMatcher(jmtoolsConfigService.collectionTopDir)
		}
		return artistMatcher
	}
	
	@CompileStatic
	File getCollectionDirPath() {
		File dir = getArtistMatcher().findArtistDir(currentArtist.name)
		if (dir == null && currentArtistDir) {
			final letter = currentArtistDir.toString().charAt(0).toLowerCase()
			dir = new File("${jmtoolsConfigService.collectionTopDir}/$letter/${currentArtistDir}")
		}
		return dir
	}
}
package jmtools

import java.io.File;

import jmtools.model.AbstractJMToolsModel;

import org.codehaus.griffon.runtime.core.AbstractGriffonModel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import groovy.beans.Bindable

class WorkAreaModel extends AbstractJMToolsModel {
	JmtoolsConfigService jmtoolsConfigService
	
	EventList tags = new BasicEventList()
	
	@Bindable String albumDir
	EventList collectionDirs = new BasicEventList()
	
	EventList getWorkDirAlbums() {
		return findFilesModel.workDirAlbums
	}
	
	File getCollectionDirPath() {
		return getPrepareFilesModel().getCollectionDirPath()
	}
	
	String getWorkDirPath() {
		return "${jmtoolsConfigService.workAreaTopDir}${File.separator}${collectionManagerModel.currentArtistDir}"
	}
}

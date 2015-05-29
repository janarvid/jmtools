package jmtools

import java.io.File;

import jmtools.model.AbstractJMToolsModel;
import griffon.core.MVCGroup
import groovy.beans.Bindable
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

import org.codehaus.griffon.runtime.core.AbstractGriffonModel
import org.veggeberg.jmtools.Global;
import org.veggeberg.jmtools.domain.Album;
import org.veggeberg.jmtools.domain.Artist;

import ca.odell.glazedlists.BasicEventList
import ca.odell.glazedlists.EventList

@CompileStatic
class FindFilesModel extends AbstractJMToolsModel {
	JmtoolsConfigService jmtoolsConfigService
	
	EventList findResults = new BasicEventList()
	@Bindable String searchPattern
	EventList<Map<String,?>> workDirAlbums = new BasicEventList()
	
	String getWorkDirPath() {
		return "${jmtoolsConfigService.workAreaTopDir}${File.separator}${collectionManagerModel.currentArtistDir}"
	}
	
	EventList<Album> getAlbums() {
		return collectionManagerModel.albums
	}
}
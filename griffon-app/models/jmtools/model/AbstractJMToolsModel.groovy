package jmtools.model

import java.io.IOException;

import jmtools.CollectionManagerModel;
import jmtools.FindFilesModel;
import jmtools.PrepareFilesModel;
import griffon.core.MVCGroup;
import groovy.transform.CompileStatic;

import org.codehaus.griffon.runtime.core.AbstractGriffonModel
import org.veggeberg.jmtools.Global;
import org.veggeberg.jmtools.domain.Album;
import org.veggeberg.jmtools.domain.Artist;

@CompileStatic
class AbstractJMToolsModel extends AbstractGriffonModel implements Appendable {
	FindFilesModel getFindFilesModel() {
		MVCGroup mvc = findExistingGroup(Global.MVC_FIND_FILES)
		return (FindFilesModel)mvc.model
	}
	
	CollectionManagerModel getCollectionManagerModel() {
		MVCGroup mvc = findExistingGroup(Global.MVC_COLLECTION_MANAGER)
		return (CollectionManagerModel)mvc.model
	}
	
	PrepareFilesModel getPrepareFilesModel() {
		MVCGroup mvc = findExistingGroup(Global.MVC_PREPARE_FILES)
		return (PrepareFilesModel)mvc.model
	}
	
	Artist getCurrentArtist() {
		return collectionManagerModel.currentArtist
	}
	
	Album getCurrentAlbum() {
		return collectionManagerModel.currentAlbum
	}
	
	MVCGroup findExistingGroup(String name) {
		MVCGroup mvc = app.mvcGroupManager.findGroup(name)
		assert mvc
		return mvc
	}
	
	void appendLog(msg) {
		app.event(Global.EVENT_APPEND_MESSAGE_LOG, [msg])
	}

	@Override
	public Appendable append(CharSequence csq) throws IOException {
		appendLog(csq)
		return this;
	}

	@Override
	public Appendable append(CharSequence csq, int start, int end) throws IOException {
		throw new UnsupportedOperationException()
	}

	@Override
	public Appendable append(char c) throws IOException {
		throw new UnsupportedOperationException()
	}
}

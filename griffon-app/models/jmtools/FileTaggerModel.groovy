package jmtools

import jmtools.model.AbstractJMToolsModel;

import org.codehaus.griffon.runtime.core.AbstractGriffonModel;

import griffon.core.GriffonApplication;
import griffon.core.MVCGroup;
import groovy.beans.Bindable

class FileTaggerModel extends AbstractJMToolsModel {
	GriffonApplication app
	@Bindable String logAreaText
	
	MVCGroup getPrepareFilesMVC() {
		MVCGroup mvc = app.getMvcGroupManager().findGroup("prepareFiles")
		return mvc
	}
} 
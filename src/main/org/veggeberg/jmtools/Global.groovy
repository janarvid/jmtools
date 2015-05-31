package org.veggeberg.jmtools

import java.awt.Font;

import groovy.transform.CompileStatic;

@CompileStatic
class Global {
//	static final Font PLAIN_FONT = new Font("Courier", Font.PLAIN, 12)

	// Events in alphabetical order
	static final String EVENT_APPEND_MESSAGE_LOG = "AppendMessageLog"
	static final String EVENT_PROGRESS_BAR_ACTIVATE = "ProgressBarActivate"
	static final String EVENT_PROGRESS_BAR_DEACTIVATE = "ProgressBarDeactivate"
	static final String EVENT_REFRESH_WORK_DIRS = "RefreshWorkDirs"
	static final String EVENT_CHANGE_SELECTED_ARTIST = "ChangeSelectedArtist"
	static final String EVENT_CHANGE_SELECTED_ALBUM = "ChangeSelectedAlbum"
	static final String EVENT_SET_ERROR_MESSAGE = "SetErrorMessage"
	static final String EVENT_SET_STATUS_MESSAGE = "SetStatusMessage"
	
	// MVC Groups
	static final String MVC_COLLECTION_MANAGER = 'collectionManager'
	static final String MVC_FIND_FILES = 'findFiles'
	static final String MVC_PREPARE_FILES = 'prepareFiles'
	static final String MVC_WORK_AREA = 'workArea'
}

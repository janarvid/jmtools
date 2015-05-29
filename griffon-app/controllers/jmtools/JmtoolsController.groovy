package jmtools

import javax.swing.JOptionPane;

import jmtools.controllers.AbstractJMToolsController;

import org.veggeberg.jmtools.Global;

import griffon.core.GriffonApplication;
import groovy.transform.CompileStatic;

//@CompileStatic
class JmtoolsController extends AbstractJMToolsController {
    // these will be injected by Griffon
    JmtoolsModel model
    JmtoolsView view
	GriffonApplication app

    // void mvcGroupInit(Map args) {
    //    // this method is called after model and view are injected
    // }

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
	
	def clearMessageLog = {
		model.messageLog = ''
		app.event(Global.EVENT_SET_STATUS_MESSAGE, ["Message log cleared."])
	}
	
	def onProgressBarActivate = { msg ->
		log.debug "onProgressBarActivate msg=$msg"
		view.progressBar?.setIndeterminate(true)
		if (msg) {
			model.statusMessage = msg
		}
	}
	
	def onProgressBarDeactivate = { msg ->
		log.debug "onProgressBarDeactivate msg=$msg"
		view.progressBar?.setIndeterminate(false)
		if (msg) {
			model.statusMessage = msg
		}
	}
	
	def onAppendMessageLog = { msg ->
		model.messageLog += msg.toString()
	}
	
	def onSetStatusMessage = { msg ->
		log.debug "Into GSelect.onSetStatusMessage.  msg=$msg"
		model.statusMessage = msg
	}
	
	def onSetErrorMessage = { msg ->
		log.debug "Into GSelect.onSetErrorMessage.  msg=$msg"
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE)
	}
}

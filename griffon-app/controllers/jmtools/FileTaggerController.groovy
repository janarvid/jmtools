package jmtools

import jmtools.controllers.AbstractJMToolsController;

class FileTaggerController extends AbstractJMToolsController {
    // these will be injected by Griffon
    FileTaggerModel model
    FileTaggerView view
	ExternalProcessService externalProcessService 

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
       
     */
    def launchTagger = { evt = null ->
		def cmd = ['easytag', model.prepareFilesMVC.model.workAreaDir.toString()]
		externalProcessService.executeCommand(model.prepareFilesMVC.model.workAreaDir, cmd, model.logAreaText)
    }
}

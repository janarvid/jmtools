package jmtools

import jmtools.test.MyGriffonUnitTC;
import griffon.core.MVCGroup;
import griffon.swing.SwingApplication;
import griffon.test.TestFor

import org.junit.Test

import static org.junit.Assert.fail

//@TestFor
class CollectionManagerControllerTests extends MyGriffonUnitTC 
{
    CollectionManagerController cut
	CollectionManagerModel model
	CollectionManagerView view
	JmtoolsConfigService jmtoolsConfigService
	ProgArchivesService progArchivesService
	
    protected void setUp() {
        super.setUp()
		jmtoolsConfigService = newInstance(JmtoolsConfigService.class)
		progArchivesService = newInstance(ProgArchivesService.class)
		progArchivesService.jmtoolsConfigService = jmtoolsConfigService
		progArchivesService.serviceInit()
		final args = [jmtoolsConfigService: jmtoolsConfigService, progArchivesService: progArchivesService]
		app = new MockSwingApplication(args)
		app.initialize()
		//app.startup()
		//app.buildMVCGroup("workArea", args)
		MVCGroup mvc = app.buildMVCGroup("collectionManager", args)
		model = mvc.members.model
		cut = mvc.members.controller
		view = mvc.members.view
		println "view = $view"
		//mvc.builder.build(view)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testInit() {
		println "view.panel = ${view.panel}"
    }
}

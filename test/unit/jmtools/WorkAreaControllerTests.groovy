package jmtools

import javax.swing.JTable;

import jmtools.test.MyGriffonUnitTC;
import griffon.core.MVCGroup;
import griffon.test.*

class WorkAreaControllerTests extends MyGriffonUnitTC 
{
	WorkAreaController cut
	WorkAreaModel model
	WorkAreaView view
	JmtoolsConfigService jmtoolsConfigService
	ProgArchivesService progArchivesService
	ExternalProcessService externalProcessService
	
    protected void setUp() {
        super.setUp()
		jmtoolsConfigService = newInstance(JmtoolsConfigService.class)
		progArchivesService = newInstance(ProgArchivesService.class)
		externalProcessService = [:] as ExternalProcessService
		progArchivesService.jmtoolsConfigService = jmtoolsConfigService
		progArchivesService.serviceInit()
		final args = [jmtoolsConfigService: jmtoolsConfigService, progArchivesService: progArchivesService,
			externalProcessService: externalProcessService,]
		app = new MockSwingApplication(args)
		app.initialize()
		app.buildMVCGroup("collectionManager")
		app.buildMVCGroup("findFiles")
		MVCGroup mvcGroup = app.buildMVCGroup("workArea")
		model = mvcGroup.members.model
		cut = mvcGroup.members.controller
		view = mvcGroup.members.view
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testInit() {
		assertNotNull(view.workDirAlbumTable)
    }
	
	void testLaunchTagger() {
		def flag = false
		cut.externalProcessService = [
			executeCommand: { path, cmd, model -> flag = true }
		] as ExternalProcessService
		cut.launchTagger()
		assertTrue(flag)
	}
}

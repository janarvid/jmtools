package jmtools

import javax.swing.JTable;

import jmtools.test.MyGriffonUnitTC;
import griffon.core.MVCGroup;
import griffon.test.TestFor

import org.junit.Test
import org.veggeberg.jmtools.Global;

import static org.junit.Assert.fail

//@TestFor
class FindFilesControllerTests extends MyGriffonUnitTC 
{
	FindFilesController cut
	FindFilesModel model
	FindFilesView view
	JmtoolsConfigService jmtoolsConfigService
	
	protected void setUp() {
		super.setUp()
		jmtoolsConfigService = newInstance(JmtoolsConfigService.class)
		final args = [jmtoolsConfigService: jmtoolsConfigService]
		app = new MockSwingApplication(args)
		app.initialize()
		MVCGroup mvcGroup = app.buildMVCGroup(Global.MVC_FIND_FILES)
		model = mvcGroup.members.model
		cut = mvcGroup.members.controller
		view = mvcGroup.members.view
	}

	protected void tearDown() {
		super.tearDown()
	}
	
    @Test
    void testSomething() {
        
    }
}

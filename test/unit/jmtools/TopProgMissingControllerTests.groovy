package jmtools

import jmtools.test.MyGriffonUnitTC;
import griffon.core.MVCGroup;
import griffon.test.TestFor
import org.junit.Test
import static org.junit.Assert.fail

//@TestFor
class TopProgMissingControllerTests extends MyGriffonUnitTC 
{
	TopProgMissingController cut
	TopProgMissingModel model
	
	protected void setUp() {
		super.setUp()
//		app = new MockSwingApplication()
//        app.initialize()
		MVCGroup mvcGroup = app.buildMVCGroup("topProgMissing")
		model = mvcGroup.members.model
		cut = mvcGroup.members.controller
		/*
		*/
	}

	protected void tearDown() {
		super.tearDown()
	}
	
    @Test
    void testSomething() {
        //fail('Not implemented!')
    }
}

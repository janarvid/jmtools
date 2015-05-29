package jmtools

import jmtools.test.MyGriffonUnitTC;
import griffon.core.MVCGroup;
import griffon.test.*

class FileTaggerControllerTests extends MyGriffonUnitTC  
{
	FileTaggerController cut
	FileTaggerModel model
	
    protected void setUp() {
        super.setUp()
		MVCGroup mvcGroup = app.buildMVCGroup("fileTagger")
		model = mvcGroup.members.model
		cut = mvcGroup.members.controller
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
        //fail('Not implemented!')
    }
}

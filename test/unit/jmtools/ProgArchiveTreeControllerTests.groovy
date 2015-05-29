package jmtools

import jmtools.test.MyGriffonUnitTC;
import griffon.core.MVCGroup;
import griffon.test.*

class ProgArchiveTreeControllerTests extends MyGriffonUnitTC 
{
	ProgArchiveTreeController cut
	
    protected void setUp() {
        super.setUp()
		MVCGroup mvc = app.buildMVCGroup("progArchiveTree")
		cut = mvc.controller
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
        //fail('Not implemented!')
    }
}

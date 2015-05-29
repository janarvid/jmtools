package jmtools

import jmtools.test.MyGriffonUnitTC;
import griffon.core.MVCGroup;
import griffon.test.*

class JmtoolsControllerTests extends MyGriffonUnitTC
{
    protected void setUp() {
        super.setUp()
		MVCGroup mvcg = app.buildMVCGroup("jmtools")
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
        //fail('Not implemented!')
    }
}

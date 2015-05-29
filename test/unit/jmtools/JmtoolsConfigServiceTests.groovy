package jmtools

import jmtools.test.MyGriffonUnitTC;
import griffon.test.*

public class JmtoolsConfigServiceTests extends MyGriffonUnitTC 
{
	JmtoolsConfigService cut
	
    void setUp() {
        super.setUp()
		cut = newInstance(JmtoolsConfigService.class)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetConfigValues() {
        assertEquals(app.config.sourceArchivesTopDir.toString(), cut.sourceArchivesTopDir.toString())
		assertEquals(app.config.workAreaTopDir.toString(), cut.workAreaTopDir.toString())
		assertEquals(app.config.collectionTopDir.toString(), cut.collectionTopDir.toString())
    }
}

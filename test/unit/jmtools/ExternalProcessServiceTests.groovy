package jmtools

import griffon.test.*

class ExternalProcessServiceTests extends GriffonUnitTestCase {
	ExternalProcessService cut
	
	protected void setUp() {
        super.setUp()
		cut = new ExternalProcessService()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRunCommand() {
		final sb = new StringBuilder()
		final dir = File.createTempDir()
		final file = 'myfile.txt'
		final path = new File("$dir/${file}")
		path.text = 'bla bla'
        cut.executeCommand(dir, ['c:\\cygwin64\\bin\\ls.exe'], sb)
		println sb
		assertEquals(file, sb.toString().trim())
    }
}

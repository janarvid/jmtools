package jmtools

import griffon.test.TestFor

import org.junit.Before;
import org.junit.Test

import static org.junit.Assert.fail

//@TestFor
class PrepareFilesServiceTests {
	PrepareFilesService cut
	
	@Before
	void setUp() {
		cut = new PrepareFilesService()
	}
	
    @Test
    void testSomething() {
//        fail('Not implemented!')
    }
}

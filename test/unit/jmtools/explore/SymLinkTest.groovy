package jmtools.explore;

import static org.junit.Assert.*;

import java.nio.file.Files;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class SymLinkTest 
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		final link = File.createTempFile("link", ".suff")
		assert link.delete()
		final target = File.createTempFile("target", ".suff")
		//Files.createSymbolicLink(link.toPath(), target.toPath())
	}
}

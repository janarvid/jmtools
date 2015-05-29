package org.veggeberg.jmtools.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class TopProgAlbumTest
{
	TopProgAlbum cut
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		cut = new TopProgAlbum()
		cut.artist = "Genesis"
	}

	@Test
	public void testClone() {
		final clone = (TopProgAlbum)cut.clone()
		assertNotSame(cut, clone)
		assertEquals(cut.artist, clone.artist)
	}
}

package org.veggeberg.jmtools.progarchives;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class AlbumMatcherTest 
{	
	static final ALBUM_DIRS_BY_ARTIST = [
		Genesis: ['1969 - Genesis To Revelation', '1970 - Trespass', '1971 - Besides the Silent Mirror (Bootleg)',
			'1971 - Nursery Cryme', '1972 - Foxtrot', '1973 - Genesis Live', '1973 - Selling England By The Pound',
			'1974 - The Lamb Lies Down on Broadway', '1975 - Live in London', '1976 - A Trick Of The Tail',
			'1976 - Wind & Wuthering', '1977 - Seconds Out', '1977 - Spot The Pigeon'],
		'Maudlin Of The Well': ['2001-Bath', '2001-Leaving Your Body Map',  '2009-Part The Second'],
		'Neal Morse': ['2005 - Question Mark', '2005 - Inner Circle (disc1)', '2007 - Sola scriptura', '2011 - Testimony 2'],
		'SBB': ['1974 - SBB', '1975 - PAMI', '1975 - NOWY HORYZONT'],
		'Tempano': ['1979 - Atabal Temal', '2008 - Selective Memory']
	]
	static private File artistTopDir

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		artistTopDir = File.createTempFile("Artists", "")
		artistTopDir.delete()
		assertTrue(artistTopDir.mkdir())
		for (artist in ALBUM_DIRS_BY_ARTIST.keySet()) {
			def artistDir = new File("${artistTopDir}/${artist}")
			assertTrue(artistDir.mkdir())
			for (album in ALBUM_DIRS_BY_ARTIST[artist]) {
				def albumDir = new File("${artistDir}/${album}")
				assertTrue(albumDir.mkdir())
			}
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// TODO Remove artistTopDir directory tree
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindAlbumDirExactMatch() {
		def astistDir = new File("${artistTopDir}/Genesis")
		def am = new AlbumMatcher(astistDir)
		def albums = [[title: 'Foxtrot', year: 1972], [title: 'Nursery Cryme', year: 1971], [year: 1971, title: 'Besides the Silent Mirror (Bootleg)']]
		for (album in albums) {
			def exp = new File("${astistDir}/${album.year} - ${album.title}")
			def act = am.findAlbumDir(album)
			assertEquals("Album ${album} not found", exp, act)
		}
	}
	
	@Test
	public void testFindAlbumDirFuzzyMatchOnYear() {
		def astistDir = new File("${artistTopDir}/Genesis")
		def am = new AlbumMatcher(astistDir)
		def album = [title: 'Foxtrat', year: 1972]
		def exp = new File("${astistDir}/1972 - Foxtrot")
		def act = am.findAlbumDir(album)
		assertEquals("Album ${album} not found", exp, act)
	}
	
	@Test
	public void testFindAlbumDirNoSpaceOnYear() {
		def astistDir = new File("${artistTopDir}/Maudlin Of The Well")
		def am = new AlbumMatcher(astistDir)
		def album = [title: 'Bath', year: 2001]
		def exp = new File("${astistDir}/2001-Bath")
		def act = am.findAlbumDir(album)
		assertEquals("Album ${album} not found", exp, act)
	}
	
	@Test
	public void testFindAlbumDirSpecialReplace1() {
		def astistDir = new File("${artistTopDir}/Neal Morse")
		def am = new AlbumMatcher(astistDir)
		def album = [title: '?', year: 2005]
		def exp = new File("${astistDir}/2005 - Question Mark")
		def act = am.findAlbumDir(album)
		println "exp=$exp, act=$act"
		assertEquals("Album ${album} not found", exp, act)
	}

	@Test
	public void testFindAlbumDirSpecialReplace2() {
		def astistDir = new File("${artistTopDir}/SBB")
		def am = new AlbumMatcher(astistDir)
		def album = [title: 'Pami??', year: 1975]
		def exp = new File("${astistDir}/1975 - PAMI")
		def act = am.findAlbumDir(album)
		println "exp=$exp, act=$act"
		assertEquals("Album ${album} not found", exp, act)
	}
	
	@Test
	public void testFindAlbumDirTempano() {
		def astistDir = new File("${artistTopDir}/Tempano")
		def am = new AlbumMatcher(astistDir)
		def album = [title: 'Selective Memory', year: 2008]
		def exp = new File("${astistDir}/2008 - Selective Memory")
		def act = am.findAlbumDir(album)
		println "exp=$exp, act=$act"
		assertEquals("Album ${album} not found", exp, act)
	}
}

package org.veggeberg.jmtools.progarchives;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class ArtistMatcherTest 
{
	private static FS = File.separator
	private ArtistMatcher cut
	static final ARTISTS_BY_LETTER = [
		'0-9': ['4-3 De Trio'],
		a: ['Anglagard'],
		b: ['Blue Effect (Modry Efekt  M. Efekt)'],
		d: ['Dun'],
		e: ['Emerson Lake and Palmer'],
		g:['Genesis'],
		q: ['Queensryche'],
		t: ['Tempano']
	]
	private static File lettersTopDir

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		lettersTopDir = File.createTempFile("lettersTopDir", "")
		assertTrue(lettersTopDir.delete())
		ARTISTS_BY_LETTER.each { letter, artists ->
			for (artist in artists) {
				def dir = new File("${lettersTopDir}${FS}${letter}${FS}${artist}")
				assertTrue(dir.mkdirs())
			}
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FileUtils.deleteDirectory(lettersTopDir)
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFindArtistDir() {
		cut = new ArtistMatcher(lettersTopDir)
		['4/3 De Trio': "0-9${FS}4-3 De Trio",
			'Änglagård': "a${FS}Anglagard",
			 'DÃ¼n': "d${FS}Dun", "Dün": "d${FS}Dun",
			 Genesis: "g${FS}Genesis",
			 "Emerson Lake & Palmer": "e${FS}Emerson Lake and Palmer",
			 "Queensrÿche": "q${FS}Queensryche", 
		'Blue Effect (ModrÃ½ Efekt; M. Efekt)': "b${FS}Blue Effect (Modry Efekt  M. Efekt)" ,
		'Témpano': "t${FS}Tempano"
		].each { paArtist, dir ->		
			def exp = "${lettersTopDir}${FS}${dir}".toString()
			def gotDir = cut.findArtistDir(paArtist)?.toString()
			println "exp=$exp, gotDir=$gotDir"
			assertEquals(exp, gotDir)
		}
	}

}

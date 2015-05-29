package org.veggeberg.jmtools.explore;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class ArtistMatcherTest 
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFindArtistDir() {
		final artistDir = new File("m:\\Music\\g\\Genesis")
		artistDir.eachFile { File albumDir ->
			if (!albumDir.toString().contains('Selling')) return
			println albumDir
			albumDir.eachFile { File track ->
				if (! track.toString().endsWith('.mp3')) return
				println "  $track"
				MP3File f      = (MP3File)AudioFileIO.read(track);
				Tag tag        = f.getTag();
				println "tag = $tag"
//				ID3v1Tag         v1Tag  = (ID3v1Tag)tag;
//				AbstractID3v2Tag v2tag  = f.getID3v2Tag()
				ID3v24Tag        v24tag = (AbstractID3v2Tag)f.getID3v2TagAsv24();
				final a = v24tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST)
				final g = v24tag.getFirst(ID3v24Frames.FRAME_ID_GENRE)
				println "    v24tag=$v24tag, a=$a g=$g"
			}
		}
	}
}

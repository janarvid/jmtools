package jmtools

import griffon.test.TestFor
import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

import org.apache.commons.io.FileUtils;
import org.farng.mp3.MP3File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test
import org.veggeberg.jmtools.domain.TagInfo;

import com.sun.xml.internal.ws.encoding.TagInfoset;

import static org.junit.Assert.*

//@TestFor
@CompileStatic
class TaggerServiceTests {
	TaggerService cut
	static File AUDIO_SAMPLES_SOURCE_DIR
	static File AUDIO_SAMPLES_TARGET_DIR

	@Before
	void setUp() {
		cut = new TaggerService()
		AUDIO_SAMPLES_SOURCE_DIR = new File('test/resources/audio_samples')
		AUDIO_SAMPLES_TARGET_DIR = File.createTempDir()
		int n = 0
		AUDIO_SAMPLES_SOURCE_DIR.eachFile { File srcFile ->
			println srcFile
			final trgFile = new File("$AUDIO_SAMPLES_TARGET_DIR/${srcFile.name}")
			if (n == 1)
				FileUtils.copyFile(srcFile, trgFile)
			n++
		}
	}
	
	@After void cleanup() {
		FileUtils.deleteDirectory(AUDIO_SAMPLES_TARGET_DIR)
	}
		
    //@Test
	public void testFindArtistDir() {
		final artistDir = new File("m:\\Music\\g\\Genesis")
		final tagInfos = []
		artistDir.eachFile { File albumDir ->
			if (!albumDir.toString().contains('Selling')) return
			cut.fillAlbumTags(albumDir, tagInfos)
			tagInfos.each {
				println it
			}
		}
	}
	
	@Test 
	// TODO
	@CompileStatic(TypeCheckingMode.SKIP)
	void testWriteFillTagInfos() {
		List<TagInfo> tagInfos = []
		final artist = "The Artist"
		final genre = "My Genre"
		final year = "2013"
		final album = "My Album"
		cut.fillAlbumTags(AUDIO_SAMPLES_TARGET_DIR, tagInfos)
		assertEquals(AUDIO_SAMPLES_TARGET_DIR.list().size(), tagInfos.size())
		for (TagInfo ti in tagInfos) {
			assertNotEquals(artist, ti.artist)
			assertNotEquals(genre, ti.genre)
			assertNotEquals(year, ti.year)
			assertNotEquals(album, ti.album)
		}
		for (i in 0..<tagInfos.size()) {
			final ti = tagInfos[i]
			println ti
			ti.artist = artist
			ti.genre = genre
			ti.year = year
			ti.album = album
			ti.track = "${i+1}"
			ti.title = "My track no ${i+1}"
		}
		println "\n\n\nWriting tags"
		cut.writeAlbumTags(tagInfos)
		final gotTagInfos = []
		println "\n\n\nFilling tags"
		cut.fillAlbumTags(AUDIO_SAMPLES_TARGET_DIR, gotTagInfos)
		assertEquals(tagInfos.size(), gotTagInfos.size())
		for (i in 0..<tagInfos.size()) {
			for (field in tagInfos[0].getFieldNames()) {
				assertEquals(field, tagInfos[i]."$field", gotTagInfos[i]."$field")
			}
		}
	}
	
	@Test
	@CompileStatic(TypeCheckingMode.SKIP)
	void readWriteTagInfo() {
		final file = new File("${AUDIO_SAMPLES_TARGET_DIR}/${AUDIO_SAMPLES_TARGET_DIR.list()[0]}")
//		println "file=$file"
		final expTagInfo = newTagInfo(file, 66)
		cut.writeAlbumTag(expTagInfo)
		final gotTagInfo = cut.readFileTag(file)
//		println "expTagInfo=$expTagInfo\ngotTagInfo=$gotTagInfo"
		for (fn in expTagInfo.fieldNames) {
			assertEquals(fn, expTagInfo."$fn", gotTagInfo."$fn")
		}
		assertEquals(file, gotTagInfo.file)
	}
	
	TagInfo newTagInfo(File file, int trackNo) {
		new TagInfo(
			artist: "My artist",
			title: "Title of track $trackNo",
			track: trackNo.toString(),
			genre: "My genre",
			year: "2014",
			album: "My album",
			file: file)
	}
}

package jmtools

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.neo4j.kernel.impl.transaction.ReadTransactionLogWritingTest;
import org.veggeberg.jmtools.domain.TagInfo;

import com.sun.xml.internal.ws.encoding.TagInfoset;

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

@CompileStatic
class TaggerService {
	// void serviceInit() {
	//    // this method is called after the model is instantiated
	// }

	// void serviceDestroy() {
	//    // this method is called when the service is destroyed
	// }

	void fillArtistTags(File artistDir, List<Map<String,?>> tagInfos) {
		artistDir.eachFile { File albumDir -> println albumDir }
	}

	void fillAlbumTags(File albumDir, List<TagInfo> tagInfos) {
		final List<File> files = []
		albumDir.eachFileRecurse { File track ->
			if (! track.toString().endsWith('.mp3')) return
			//			println "  $track"
			files << track
		}
		sortByFileName(files)
		for (file in files) {
			final trackInfo = readFileTag(file)
			tagInfos << trackInfo
		}
	}
	
	TagInfo readFileTag(File file) {
		MP3File mp3File = (MP3File)AudioFileIO.read(file);
		ID3v24Tag v24tag = mp3File.getID3v2TagAsv24();
		new TagInfo(
			artist: v24tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST),
			genre: v24tag.getFirst(ID3v24Frames.FRAME_ID_GENRE),
			year: v24tag.getFirst(ID3v24Frames.FRAME_ID_YEAR),
			album: v24tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM),
			track: v24tag.getFirst(ID3v24Frames.FRAME_ID_TRACK),
			title: v24tag.getFirst(ID3v24Frames.FRAME_ID_TITLE),
			file: file
		)
	}

	void writeAlbumTags(List<TagInfo> tagInfos) {
		tagInfos.each { TagInfo ti ->
			final file = (File)ti.file
			writeAlbumTag(ti)
		}
	}

	@CompileStatic(TypeCheckingMode.SKIP)
	void writeAlbumTag(TagInfo tagInfo) {
		MP3File mp3File = (MP3File)AudioFileIO.read(tagInfo.file);
		println "hasID3v1Tag = ${mp3File.hasID3v1Tag()}"
		println "hasID3v2Tag = ${mp3File.hasID3v2Tag()}"
		AbstractID3v2Tag v2tag = mp3File.getID3v2Tag()
		println "v2tag = $v2tag"
		//ID3v24Tag v24tag = mp3File.getID3v2TagAsv24();
		for (fn in tagInfo.fieldNames) {
			final key = FieldKey."${fn.toUpperCase()}"
			v2tag.setField(key, tagInfo."$fn");
		}
		mp3File.setTag(v2tag)
		mp3File.commit()
	}

	void sortByFileName(List<File> list) {
		list.sort { File a, File b -> a <=> b }
	}
}

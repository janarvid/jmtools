package jmtools

import groovy.transform.CompileStatic;

import java.util.Collection;
import java.util.Map;

import org.veggeberg.jmtools.domain.Album;

@CompileStatic
class CollectionService {
    // void serviceInit() {
    //    // this method is called after the model is instantiated
    // }

    // void serviceDestroy() {
    //    // this method is called when the service is destroyed
    // }

	@CompileStatic
	static void autoSetAlbumDirs(String artist, Collection<Album> albums, Collection<Map<String,?>> albumFileEntries) {
		String artistLower = artist.toLowerCase()
		final sortedAlbums = albums.findAll { Album it -> it.name != artist }
		sortedAlbums.sort { Album a, Album b -> b.name.length() <=> a.name.length() }
		final selfTitled = albums.find { Album it -> it.name == artist }
		if (selfTitled) sortedAlbums << selfTitled
		for (Map<String,?> afe in albumFileEntries) {
			String afeName = ((String)afe['name']).toLowerCase()
			List<Album> albumMatches = []
			for (album in sortedAlbums) {
				String pattern = toSearchPattern(album.name)
				println "pattern=$pattern"
				if (afeName =~ pattern) {
					albumMatches << album
				}
			}
			if (albumMatches.size() >= 1) {
				afe.albumDir = "${albumMatches[0].year} - ${albumMatches[0].name}"
			}
		}
	}
	
	@CompileStatic
	static String toSearchPattern(String albumName) {
		final ret = albumName.replaceAll(/[ -\.][ -\.]*/, /\.\.*/)
		.replaceAll(/['!]/, /\.*/)
		.toLowerCase()
		return ret
	}
}

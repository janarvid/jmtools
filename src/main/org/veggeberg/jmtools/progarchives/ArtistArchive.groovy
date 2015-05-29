package org.veggeberg.jmtools.progarchives;

// Contains information about all Artists
public class ArtistArchive 
{
	Map<String,Collection<?>> artistMap = [:]
	
	void addAlbums(Collection<?> albums) {
		for (album in albums) {
			def albumMap = artistMap.get(album.artist, [:])
			albumMap[album.title] = album
		}
	}
}

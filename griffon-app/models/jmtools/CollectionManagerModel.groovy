package jmtools

import groovy.beans.Bindable

import org.codehaus.griffon.runtime.core.AbstractGriffonModel
import org.veggeberg.jmtools.domain.Album
import org.veggeberg.jmtools.domain.Artist

import ca.odell.glazedlists.BasicEventList
import ca.odell.glazedlists.EventList

class CollectionManagerModel extends AbstractGriffonModel {
	EventList<Artist> artists = new BasicEventList()
	EventList<Album> albums = new BasicEventList()
	@Bindable Artist currentArtist
	EventList<String> genres = new BasicEventList()
	@Bindable String selectedGenre
	@Bindable Album currentAlbum
	@Bindable currentArtistDir
	
	void setCurrentArtist(Artist artist) {
		this.currentArtist = artist
		setCurrentArtistDir(artist.name)
	}
}

package jmtools

import javax.swing.JTable;

import org.codehaus.griffon.runtime.core.AbstractGriffonModel;
import org.veggeberg.jmtools.domain.Artist;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import groovy.beans.Bindable
import groovy.transform.CompileStatic;

//@CompileStatic
class TopProgMissingModel extends AbstractGriffonModel {
	JmtoolsConfigService jmtoolsConfigService
	
	JTable artistTable, findResultsTable
//	EventList<Artist> artists = new SortedList( new BasicEventList(), {a, b -> a.rank <=> b.rank} as Comparator)
	EventList<Artist> artists = new BasicEventList()
	EventList albums = new SortedList( new BasicEventList(), { a, b -> a.rank <=> b.rank } as Comparator)
	EventList findResults = new BasicEventList()
	@Bindable String searchPattern
	@Bindable Artist currentArtist
	@Bindable currentArtistDir
	
	String getWorkDirPath() {
		"${jmtoolsConfigService.workAreaTopDir}${File.separator}${currentArtistDir}"
	}
	
	void setCurrentArtist(Artist artist) {
		this.currentArtist = artist
		setCurrentArtistDir(artist.name)
	}
}
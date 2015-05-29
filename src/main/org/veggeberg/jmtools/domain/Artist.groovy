package org.veggeberg.jmtools.domain

import groovy.transform.CompileStatic;
import groovy.transform.ToString;

@CompileStatic
@ToString(includePackage=false, ignoreNulls=true, includeNames=true)
class Artist {
	String name
	String genre
	String country
	List<Album> albums
	File path
	URL url
	
	int getRank() {
		List<Integer> ranks = albums*.rank
		return ranks.min()
	}
	
	@Override
	Object clone() throws CloneNotSupportedException {
		final Artist ret = new Artist(
			name:name, genre:genre, country:country, albums:albums, path:path
		)
		return ret
	}
}

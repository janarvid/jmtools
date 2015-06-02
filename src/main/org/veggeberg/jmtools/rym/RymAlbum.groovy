package org.veggeberg.jmtools.rym

import groovy.transform.CompileStatic;
import groovy.transform.ToString;

@CompileStatic
@ToString(includePackage=false, includeNames=true)
class RymAlbum {
	String artist
	String album
	String year
	List<String> genres
	float avgRating
	int nofRatings
	int nofReviews
}

package org.veggeberg.jmtools.domain

import groovy.transform.CompileStatic;
import groovy.transform.ToString;

@CompileStatic
@ToString(includePackage=false, ignoreNulls=true, includeNames=true)
class Album {
	String name
	Integer year
	Integer rank
	File path
	URL url

	@Override
	Object clone() throws CloneNotSupportedException {
		final ret = new Album(
			name:name, year:year, rank:rank, path:path, url:url
		)
		return ret
	}
}

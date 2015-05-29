package org.veggeberg.jmtools.domain

import java.lang.reflect.Field;
import java.util.List;

import groovy.transform.CompileStatic;
import groovy.transform.Memoized;
import groovy.transform.ToString;

@ToString(includePackage=false, ignoreNulls=true, includeNames=true)
@CompileStatic
class TagInfo extends DomainObjectABC {
	String artist
	String genre
	String year
	String album
	String track
	String title
	File file
	@Override
//	@Memoized
	public Collection<String> getFieldNames() {
		super.getFieldNames().findAll { String name -> name != 'file' }
	}
}

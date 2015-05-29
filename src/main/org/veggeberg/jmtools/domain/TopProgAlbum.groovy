package org.veggeberg.jmtools.domain

import groovy.transform.CompileStatic;
import groovy.transform.ToString;
import groovy.transform.TypeCheckingMode;

import java.lang.reflect.Field;
import java.util.List;

@CompileStatic
@ToString(includePackage=false, ignoreNulls=true, includeNames=true)
class TopProgAlbum implements Serializable
{
	String artist
	Integer year
	String title
	String genre
	String qwr
	Integer rank
	String cover
	String albumUrl
	String artistUrl
	String albumType
	File artistPath
	Long artistPathLastModified
	File albumPath
	
	void setArtistPath(File artistPath) {
		this.artistPath = artistPath
		if (artistPath) {
			this.artistPathLastModified = artistPath.lastModified()
		}
	}
	private static Collection<String> fieldNames
	private static Collection<String> getFieldNames() {
		if (fieldNames == null) {
			fieldNames = TopProgAlbum.class.declaredFields
				.findAll { Field it -> !(it.name.contains('$') || it.name == 'metaClass') }
				.collect { Field it -> it.name }
		}
		return fieldNames
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.is(obj))
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopProgAlbum other = (TopProgAlbum) obj;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	@CompileStatic(TypeCheckingMode.SKIP)
	Object clone() throws CloneNotSupportedException {
		final ret = new TopProgAlbum()
		for (field in getFieldNames()) {
			//println field
			ret."$field" = this."$field"
		}
		return ret
	}
/*
	@Override
	public int compareTo(Object o) {
		TopProgAlbum other = (TopProgAlbum)o
		return this.rank.compareTo(other.rank)
	}
	*/
}

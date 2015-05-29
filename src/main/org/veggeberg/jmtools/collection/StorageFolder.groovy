package org.veggeberg.jmtools.collection

import groovy.transform.CompileStatic;

import java.util.Map;

@CompileStatic
class StorageFolder 
{
	String name
	Long sizeLimit
	Map<String, Long> dirSizeByArtist
}

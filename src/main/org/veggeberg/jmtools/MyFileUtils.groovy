package org.veggeberg.jmtools

import groovy.transform.CompileStatic;

@CompileStatic
class MyFileUtils {

	static String sanitize(String name) {
		return name.replaceAll("[:\\\\/*\"?|<>]", "_");
	}

}

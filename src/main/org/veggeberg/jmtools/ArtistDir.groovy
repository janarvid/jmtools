package org.veggeberg.jmtools

import groovy.transform.CompileStatic;

import java.io.File
import java.net.URI;

@CompileStatic
class ArtistDir extends File implements Serializable 
{
	public ArtistDir(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	public ArtistDir(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	public ArtistDir(String parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}

	public ArtistDir(File parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}
}

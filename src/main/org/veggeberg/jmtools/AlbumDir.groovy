package org.veggeberg.jmtools

import groovy.transform.CompileStatic

@CompileStatic
class AlbumDir extends File implements Serializable 
{
	private Long dirSize
	
	public AlbumDir(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	public AlbumDir(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	public AlbumDir(String parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}

	public AlbumDir(File parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}
}

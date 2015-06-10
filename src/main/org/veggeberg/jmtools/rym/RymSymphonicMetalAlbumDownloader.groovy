package org.veggeberg.jmtools.rym

import groovy.transform.CompileStatic;

@CompileStatic
public class RymSymphonicMetalAlbumDownloader extends AbstractRymDownloader {
	public static final String RYM_ROOT_DIR = 'data/RateYourMusic'
	
	public static main(args) {
		final dl = new RymSymphonicMetalAlbumDownloader()
		dl.directory = new File("${RYM_ROOT_DIR}/SymphonicMetal")
		dl.downloadAll("symphonic+metal", 12)
	}
}

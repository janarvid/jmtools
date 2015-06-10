package org.veggeberg.jmtools.rym

import groovy.transform.CompileStatic;

@CompileStatic
public class RymSymphonicBlackMetalAlbumDownloader extends AbstractRymDownloader {
	public static final String RYM_ROOT_DIR = 'data/RateYourMusic'
	
	public static main(args) {
		final dl = new RymSymphonicBlackMetalAlbumDownloader()
		dl.directory = new File("${RYM_ROOT_DIR}/SymphonicBlackMetal")
		dl.downloadAll("symphonic+black+metal", 8)
	}
}

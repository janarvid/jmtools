package org.veggeberg.jmtools.rym

import groovy.transform.CompileStatic;

@CompileStatic
public class RymNeueDeuscheHarteAlbumDownloader extends AbstractRymDownloader {
	public static final String RYM_ROOT_DIR = 'data/RateYourMusic'
	
	public static main(args) {
		final dl = new RymNeueDeuscheHarteAlbumDownloader()
		dl.directory = new File("${RYM_ROOT_DIR}/GothicMetal")
		dl.downloadAll("gothic-metal", 19)
	}
}

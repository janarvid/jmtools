package org.veggeberg.jmtools.rym

import groovy.transform.CompileStatic;

@CompileStatic
public class RymMedievalFolkMetalAlbumDownloader extends AbstractRymDownloader {
	public static final String RYM_ROOT_DIR = 'data/RateYourMusic'
	
	public static main(args) {
		final dl = new RymMedievalFolkMetalAlbumDownloader()
		dl.directory = new File("${RYM_ROOT_DIR}/MedievalFolkMetal")
		dl.downloadAll("medieval+folk+metal", 1)
	}
}

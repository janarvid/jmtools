package org.veggeberg.jmtools.rym

import groovy.transform.CompileStatic;

@CompileStatic
public class RymNeueDeuscheWelleAlbumDownloader extends AbstractRymDownloader {
	public static final String RYM_ROOT_DIR = 'data/RateYourMusic'
	
	public static main(args) {
		final dl = new RymNeueDeuscheWelleAlbumDownloader()
		dl.directory = new File("${RYM_ROOT_DIR}/NeueDeutsheWelle")
		dl.downloadAll("neue+deutsche+welle", 4)
	}
}

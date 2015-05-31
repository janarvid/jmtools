package org.veggeberg.jmtools.progarchives

import groovy.transform.CompileStatic;

import org.veggeberg.jmtools.AbstractDownloader

@CompileStatic
class ArtistDownloader extends AbstractDownloader
{
	static main(args) {
		//http://www.progarchives.com/bands-alpha.asp
		final ad = new ArtistDownloader()
		ad.download(new URL("http://www.progarchives.com/bands-alpha.asp"), new File("${ad.directory}/artists.html"))
	}
}

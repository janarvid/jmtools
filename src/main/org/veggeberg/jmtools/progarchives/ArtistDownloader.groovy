package org.veggeberg.jmtools.progarchives

class ArtistDownloader extends AbstractDownloader
{
	public ArtistDownloader() {
		// TODO Auto-generated constructor stub
	}

	static main(args) {
		//http://www.progarchives.com/bands-alpha.asp
		final ad = new ArtistDownloader()
		ad.download(new URL("http://www.progarchives.com/bands-alpha.asp"), new File("$DIR/artists.html"))
	}
}

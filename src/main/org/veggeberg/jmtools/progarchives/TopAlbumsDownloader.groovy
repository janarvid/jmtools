package org.veggeberg.jmtools.progarchives

class TopAlbumsDownloader 
{
	final static String URL_PREFIX = "http://www.progarchives.com/top-prog-albums.asp"
	final static String DIR = "test/resources/progarchives"

	public TopAlbumsDownloader() {
		// TODO Auto-generated constructor stub
	}
	
	void download(URL url, File file) {
		/*
		URLConnection urlConn = url.openConnection()
		println urlConn.getHeaderFields()
		final length = urlConn.getContentLengthLong()
		urlConn.getInputStream().re
		final content = urlConn.getContent()
		println content.getClass()
		*/
		print "Downloading '$url'... "
		final urlText = url.text
		def operation = ' Not updated.'
		if ( (!file.exists()) || urlText != file.text) {
			file.text = url.text
			operation = "Updated. --> '$file'"
		}
		println operation
	}

	static main(args) {
		final tal = new TopAlbumsDownloader()
		for (year in 1968..2015) {
			final s = "${URL_PREFIX}?syears=${year}&smaxresults=250"  
			tal.download(new URL(s), new File("$DIR/top-prog-albums-${year}.html"))
		}
	}
}

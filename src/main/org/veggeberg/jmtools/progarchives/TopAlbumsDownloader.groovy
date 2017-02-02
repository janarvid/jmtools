package org.veggeberg.jmtools.progarchives

import org.apache.tools.ant.taskdefs.Get.DownloadProgress;

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

@CompileStatic
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
		//urlConn.getInputStream().re
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
	
	void downloadAllYears(List<Integer> years) {
		for (year in years) {
			final s = "${URL_PREFIX}?syears=${year}&smaxresults=250&sminratings=0".toString()
			download(new URL(s), new File("$DIR/top-prog-albums-${year}.html"))
		}
	}
	
	void generateWgetScript(List<Integer> years) {
		File file = File.createTempFile("fsdf", ".tmp")
		file.withWriter { Writer wr ->
			for (year in 1968..2016) {
				final s = "wget '${URL_PREFIX}?syears=${year}&smaxresults=250&sminratings=0' -O $DIR/top-prog-albums-${year}.html"
				wr.write(s + "\n")
				//println s
			}
		}
		println "Script '$file' created."
	}

	@CompileStatic(TypeCheckingMode.SKIP)
	static main(args) {
		final tal = new TopAlbumsDownloader()
		List<Integer> years = (1968..2016).collect { it }
//		tal.downloadAllYears(years)
		tal.generateWgetScript(years)
	}
}

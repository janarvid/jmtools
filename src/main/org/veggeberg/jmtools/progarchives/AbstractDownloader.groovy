package org.veggeberg.jmtools.progarchives

import groovy.transform.CompileStatic;

import java.io.File;
import java.net.URL;

@CompileStatic
abstract class AbstractDownloader {
	final static String DIR = "test/resources/progarchives"

	public AbstractDownloader() {
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
}

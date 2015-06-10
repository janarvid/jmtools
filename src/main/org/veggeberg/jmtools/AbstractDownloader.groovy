package org.veggeberg.jmtools

import groovy.transform.CompileStatic;

import java.io.File;
import java.net.URL;

@CompileStatic
public abstract class AbstractDownloader {
	private File directory = File.createTempFile("data", "");
	
	public AbstractDownloader() {
		directory.delete()
		assert directory.mkdir()
	}
	
	void setDirectory(File dir) {
		if ( ! dir.exists()) assert dir.mkdir()
		directory = dir
	}
	
	public File getDirectory() {
		return directory
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

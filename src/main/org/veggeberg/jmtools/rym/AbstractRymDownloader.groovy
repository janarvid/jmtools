package org.veggeberg.jmtools.rym

import org.veggeberg.jmtools.AbstractDownloader;

import groovy.transform.CompileStatic;

@CompileStatic
public abstract class AbstractRymDownloader extends AbstractDownloader {
	public static final String RYM_ROOT_DIR = 'data/RateYourMusic'
	
	void downloadAll(String genreUrlPart, int nofPages) {
		for (page in 1..nofPages) {
			final urlTemplate = "http://rateyourmusic.com/customchart?page=${page}&chart_type=top&type=album&year=alltime&genre_include=1&include_child_genres=1&genres=${genreUrlPart}&include_child_genres_chk=1&include=both&origin_countries=&limit=none&countries="
			final url = urlTemplate.toString()
			final name = genreUrlPart.toLowerCase().replaceAll(/\+/, '-')
			download(new URL(url), new File("${directory}/top-${name}-albums-${page}.html"))
		}
	}
}

package org.veggeberg.jmtools.rym

import groovy.transform.CompileStatic;

@CompileStatic
class RymAlbumDownloader extends AbstractRymDownloader {

	static main(args) {
		final dl = new RymAlbumDownloader()
		for (page in 1..50) {
			final urlTemplate = "http://rateyourmusic.com/customchart?page=${page}&chart_type=top&type=album&year=alltime&genre_include=1&include_child_genres=1&genres=rock&include_child_genres_chk=1&include=both&origin_countries=&limit=none&countries="
			final url = urlTemplate.toString()
			dl.download(new URL(url), new File("${dl.directory}/top-rym-albums-${page}.html"))
		}
	}

}

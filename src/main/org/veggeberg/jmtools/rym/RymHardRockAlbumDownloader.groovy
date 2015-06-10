package org.veggeberg.jmtools.rym

import groovy.transform.CompileStatic;

@CompileStatic
public class RymHardRockAlbumDownloader extends AbstractRymDownloader {
	public static final String RYM_ROOT_DIR = 'data/RateYourMusic'
	
	public static main(args) {
		final dl = new RymHardRockAlbumDownloader()
		dl.directory = new File("${RYM_ROOT_DIR}/HardRock")
		for (page in 1..50) {
			final urlTemplate = "http://rateyourmusic.com/customchart?page=${page}&chart_type=top&type=album&year=alltime&genre_include=1&include_child_genres=1&genres=hard+rock&include_child_genres_chk=1&include=both&origin_countries=&limit=none&countries="
			final url = urlTemplate.toString()
			dl.download(new URL(url), new File("${dl.directory}/top-hard-rock-rym-albums-${page}.html"))
		}
	}
}

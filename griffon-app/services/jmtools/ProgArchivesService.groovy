package jmtools

import griffon.core.GriffonApplication
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

import org.mapdb.DB
import org.mapdb.DBMaker
import org.veggeberg.jmtools.FileFormat
import org.veggeberg.jmtools.domain.Album
import org.veggeberg.jmtools.domain.Artist
import org.veggeberg.jmtools.domain.TopProgAlbum
import org.veggeberg.jmtools.progarchives.AlbumMatcher
import org.veggeberg.jmtools.progarchives.ArtistMatcher
import org.veggeberg.jmtools.progarchives.TopProgAlbumsReader
import org.veggeberg.jmtools.rym.TopRymReader;

@CompileStatic
class ProgArchivesService {
	GriffonApplication app
	JmtoolsConfigService jmtoolsConfigService
	List<TopProgAlbum> topAlbumsWithPath
	List<Artist> topArtists
	List<Artist> topArtistsWithPath
	Set<TopProgAlbum> topAlbumsCache
	Map<File,AlbumMatcher> albumMatchersByArtistDir
	DB db
	
     void serviceInit() {
		final File topAlbumsCacheFile = 
				new File("${jmtoolsConfigService.userCacheDir}${File.separator}topAlbumsCache")
        // this method is called after the model is instantiated
		 db = DBMaker.newFileDB(topAlbumsCacheFile)
		 .closeOnJvmShutdown()
		 .make()
     }

    // void serviceDestroy() {
    //    // this method is called when the service is destroyed
    // }
	
	final static Map cantFindAlbumsByArtist = [
		'gevende': ['sen balik degilsin ki'],  // Can't find
		'hail spirit noir': ['oi magoi'],  // Can't find
		'jean louis':[], // Can't find
		'jet black sea': ['the path of least existence'], // Can't find
		'mike keneally': ['scambot 1'], // Can't find
		'one shot':[], // Can't find
		'pervy perkin':[],
		'present':['barbaro (ma non troppo)'], // Can't find
		'state urge': ['white rock experience'], // Can't find
	]
	
	@CompileStatic(TypeCheckingMode.SKIP)
	def getTopAlbumsUrlPrefix() {
		def pa = app.config.progArchives.urlPrefix
		return "${pa}"
	}
	
	private Set<TopProgAlbum> getTopAlbumsCache() {
		if (topAlbumsCache == null) {
			topAlbumsCache = db.getHashSet("topAlbumsCache") 
		}
		return topAlbumsCache
	}

    void downloadTopAlbumsForYear(year) {
		def surl = topAlbumsUrlPrefix
		println "surl=$surl"
		//URL url = new URL()
    }
	
	List<TopProgAlbum> getTopAlbums() {
		app.log.info("Into getTopAlbums()")
		final cache = getTopAlbumsCache()
		if (cache.size() == 0) {
//			final reader = new TopProgAlbumsReader()
			final reader = new TopRymReader()
			app.log.info("Reading all top prog albums...")
			final albums = reader.getAlbums()
			app.log.info("${albums.size()} top prog albums read.")
			for (a in albums) {
//				final key = toTopAlbumKey(a)
				cache << a
			}
		}
		db.commit()
		List<TopProgAlbum> ret = new ArrayList(cache)
		ret.sort({TopProgAlbum a, TopProgAlbum b -> a.rank <=> b.rank} as Comparator)
		app.log.info("End of getTopAlbums()")
		return ret
	}
	
	private static String toTopAlbumKey(TopProgAlbum a) {
		return "${a.artist}-${a.year}-${a.title}".toString()
	}
	
	List<TopProgAlbum> getTopAlbumsWithPath(Integer maxUniqArtists=null) {
		final uniqueArtists = new HashSet()
		List<TopProgAlbum> ret = []
		albumMatchersByArtistDir = [:]
		ArtistMatcher artistMatcher = new ArtistMatcher(jmtoolsConfigService.collectionTopDir)
		for (topAlbum in getTopAlbums()) {
//			println "topAlbum.artist = ${topAlbum.artist}"
//			if ( !(topAlbum.artist =~ /T.empano/)) continue
			if (maxUniqArtists && uniqueArtists.size() >= maxUniqArtists) {
				if ( ! uniqueArtists.contains(topAlbum.artist) ) {
					continue
				}
			}
			uniqueArtists << topAlbum.artist
			if (topAlbum.artistPath == null) {
				final artistPath = artistMatcher.findArtistDir(topAlbum.artist)
				topAlbum.artistPath = artistPath
			}
			if (topAlbum.artistPath) {
//				if (topAlbum.artistPath.lastModified() > topAlbum.artistPathLastModified) {
					if (topAlbum.albumPath == null) {
						final myAlbum = new Album(name:topAlbum.title, year:topAlbum.year, url:new URL(topAlbum.albumUrl))
						final albumPath = findAlbumDir(topAlbum.artistPath, myAlbum)
						if (albumPath && formatIsSupported(albumPath.toString())) {
							topAlbum.albumPath = albumPath
						}
					}
//				}
			}
			ret << topAlbum
		}
		db.commit()
		app.log.info("${ret.size()} top prog albums with path read.")
		return ret
	}
	/*
	List<TopProgAlbum> getTopAlbumsWithPathOld(Integer maxUniqArtists=null) {
		if (topAlbumsWithPath == null) {
			topAlbumsWithPath = []
			final uniqueArtists = new HashSet()
			ArtistMatcher artistMatcher = new ArtistMatcher(jmtoolsConfigService.collectionTopDir)
			for (topAlbum in getTopAlbums()) {
				if (maxUniqArtists && uniqueArtists.size() >= maxUniqArtists) {
					if ( ! uniqueArtists.contains(topAlbum.artist) ) {
						continue
					}
				}
				uniqueArtists << topAlbum.artist
				final newTopAlbum = (TopProgAlbum)topAlbum.clone()
				final artistPath = artistMatcher.findArtistDir(topAlbum.artist)
				if (artistPath) {
					newTopAlbum.artistPath = artistPath
					final myAlbum = new Album(name:topAlbum.title, year:topAlbum.year)
					final albumPath = findAlbumDir(artistPath, myAlbum)
					if (albumPath && formatIsSupported(albumPath.toString())) {
						newTopAlbum.albumPath = albumPath
					}
				}
				topAlbumsWithPath << newTopAlbum
			}
			app.log.info("${topAlbumsWithPath.size()} top prog albums with path read.")
		}
		return topAlbumsWithPath
	}
	*/
	
	@CompileStatic(TypeCheckingMode.SKIP)
	private static List<Artist> getTopByArtists(Collection<TopProgAlbum> topAlbums) {
		List<Artist> ret = []
		final albumsByArtist = topAlbums.groupBy { TopProgAlbum it -> it.artist }
		albumsByArtist.each { 
			String artist, Collection<TopProgAlbum> albums ->
			final rank = albums*.rank.min()
			final myAlbums = albums.collect {
				new Album(name:it.title, year:it.year, rank:it.rank)
			}
			final topArtist = new Artist(name:artist, genre:albums[0].genre, albums: myAlbums, url: new URL(albums[0].artistUrl))
			ret << topArtist
		}
		return ret
	}
	
	List<Artist> getTopArtists() {
		if (topArtists == null) {
			topArtists = getTopByArtists(getTopAlbums())
		}
		return topArtists
	}
	
	List<Artist> getTopArtistsWithPath()
	{
		if (topArtistsWithPath == null) {
			topArtistsWithPath = getTopByArtists(getTopAlbumsWithPath(100))
		}
		return topArtistsWithPath
	}
	
	Collection<Artist> getMissingArtistsAndAlbums(List<TopProgAlbum> albums) {
		final missingAlbums = albums.findAll { TopProgAlbum album ->
			final cantFind = false 
			if (cantFindAlbumsByArtist.containsKey(album.artist.toLowerCase())) {
				final List cantFindAlbums = (List)cantFindAlbumsByArtist[album.artist]
				if (cantFindAlbums) {
					if (cantFindAlbums.contains(album.title.toLowerCase())) {
						cantFind = true
					}
				}
				else {
					cantFind = true
				}
			}
			if (cantFind) return false
			return album.artistPath == null || album.albumPath == null
		}
		final ret = getTopByArtists(missingAlbums)
		return ret
	}
	
//	@CompileStatic(TypeCheckingMode.SKIP)
	boolean formatIsSupported(String fileName) {
		final fnLower = fileName.toLowerCase()
		boolean flag = true
		for (FileFormat ff in FileFormat.values()) {
			if (fnLower.contains(ff.toString().toLowerCase())) {
				flag = false
				break
			}
		}
		return flag
	}
//	private boolean formatIsSupported(String fileName) {
//		final ret = FileFormat.findAll { it != FileFormat.MP3 }
//			.find { FileFormat it -> fileName.contains(it.name().toLowerCase()) }
//		return (!ret)
//	}
	
	private File findAlbumDir(File artistDir, Album album) {
		def albumMatcher = albumMatchersByArtistDir.get(artistDir, new AlbumMatcher(artistDir))
		return albumMatcher.findAlbumDir([title:album.name, year:album.year]) //TODO Pass album class 
	}
}

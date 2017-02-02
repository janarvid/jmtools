package org.veggeberg.jmtools.progarchives

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils;
import org.veggeberg.jmtools.FileFormat;
import org.veggeberg.jmtools.collection.AbstractCollectionBuilder
import org.veggeberg.jmtools.collection.StorageFolder;
import org.veggeberg.jmtools.domain.TopProgAlbum;

@CompileStatic
class TopProgCollectionBuilder extends AbstractCollectionBuilder
{
	@CompileStatic(TypeCheckingMode.SKIP)
	void makeLinksLinux(albumPaths, linkDir) {
		def scriptText = new StringBuilder()
		for (albumPath in albumPaths) {
			def artistPath = albumPath.parentFile
			def letterPath = artistPath.parentFile
			def album = albumPath.name
			def artist = artistPath.name
			def letter = letterPath.name
			def linkArtistDir = new File("${linkDir}/${artist}")
			if ( ! linkArtistDir.exists()) {
				if ( ! linkArtistDir.mkdirs() ) {
					throw new IOException("Unable to create directory '$linkArtistDir'")
				}
			}
			def link = "${linkArtistDir}/${album}".replaceAll(/"/, /\\"/)
			def ap = albumPath.toString().replaceAll(/"/, /\\"/)
			def cmd = "ln -s '${ap}' \"${link}\""
			scriptText.append(cmd)
			scriptText.append('\n')
			println "cmd=$cmd"
		}
		scriptText.append("echo \"linkDir='${linkDir}'\"\n")
		def scriptFile = File.createTempFile("linkScript", ".sh")
		scriptFile.text = scriptText
		println "scriptFile=${scriptFile}"
	}

	void makeLinksWindows(List<File> albumPaths, linkDir) {
		def scriptText = new StringBuilder()
		for (albumPath in albumPaths) {
			def artistPath = albumPath.parentFile
			def letterPath = artistPath.parentFile
			def album = albumPath.name
			def artist = artistPath.name
			StorageFolder sf = storageFolders.find { StorageFolder it ->
				it.dirSizeByArtist.keySet().contains(artist)
			}
//			println "$artist $sf"
			final collName = sf.name
			def linkArtistDir = new File("${linkDir}/${collName}/${artist}")
			if ( ! linkArtistDir.exists()) {
				if ( ! linkArtistDir.mkdirs() ) {
					throw new IOException("Unable to create directory '$linkArtistDir'")
				}
			}
			def link = "${linkArtistDir}/${album}".replaceAll(/"/, /\\"/)
					.replaceFirst("C:", '/cygdrive/c')
					.replaceAll('\\\\', '/')
//					.replaceAll(/\(/, '\\\\(')
//					.replaceAll(/\)/, '\\\\)')
			def ap = albumPath.toString().replaceAll(/"/, /\\"/)
					.replaceFirst("m:", '/cygdrive/m')
					.replaceAll('\\\\', '/')
			def q = '\"'
			if (link.contains('$')) q = "'"
			def cmd = "ln -s ${q}${ap}${q} ${q}${link}${q}"
			//Files.createSymbolicLink(new File("${linkArtistDir}/${album}").toPath(), albumPath.toPath())
			scriptText.append(cmd)
			scriptText.append('\n')
			println "cmd=$cmd"
		}
		scriptText.append("echo \"linkDir='${linkDir}'\"\n")
		def scriptFile = File.createTempFile("linkScript", ".sh")
		scriptFile.text = scriptText
		//"c:\\cygwin64\\bin\\bash.exe ${scriptFile}".execute()
		println "linkDir='${linkDir}"
		println "scriptFile=${scriptFile}"
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	List<TopProgAlbum> readAlbums() {
		final reader = new TopProgAlbumsReader();
		final readAlbums = reader.getAlbums()
		readAlbums.sort { TopProgAlbum a, TopProgAlbum b -> b.qwr <=> a.qwr } 
		Collection<TopProgAlbum> albums = []

		// Include
		final includeAlbums = readAlbums.findAll { TopProgAlbum it ->
			final artist = it.artist.toLowerCase()
			includeArtists.contains(artist)
		}
		albums.addAll(includeAlbums)
		
		// Add to album map
//		final albumMap = [:]
//		for (album in readAlbums) {
//			final key = album.artist + '-' + album.year + '-' + album.title
//			//println album
//			albumMap.put(key, album)
//		}
		//println("albumMap = ${albumMap.size()}")

		// Sort albums and add numbering
		albums.addAll(readAlbums)
		
		// Exclude
		albums = albums.findAll { TopProgAlbum it ->
			String artist = it.artist.toLowerCase()
			final title = it.title.toLowerCase()
			boolean ret = true
			if (excludeArtists.containsKey(artist)) {
				final titles = excludeArtists[artist]
				if (titles.size() == 0) { 
					ret = false
				}
				else if (titles.contains(title)) {
					ret = false
				}
			} 
			return ret
		}
//		for (i in 0..<albums.size()) {
//			albums[i].put('no', i+1);
//			//if (i>10) break
//		}
		return albums.unique()
	}
	
	void configure() {
		// excludeArtists. Artist names must be in lower case
		final cantFindAlbums = [:
		]
		
		excludeArtists = [
			'dave kerzner': ['new world (deluxe edition)'], // Can't find
		    'gevende': ['sen balik degilsin ki'],  // Can't find
		    'harmonium': ["si on avait besoin d'une cinquieme saison"], // Too slow
			'hail spirit noir': ['oi magoi'],  // Can't find
		    'jean louis':[], // Can't find
			'jet black sea': ['the path of least existence'], // Can't find
            'miles davis': [], // Just Jazz
			'kayo dot': [], // Too weired RIO		
			'mike keneally': ['scambot 1'], // Can't find
			'one shot':[], // Can't find
			'present':['barbaro (ma non troppo)'], // Can't find
			'state urge': ['white rock experience'], // Can't find
			//'syrinx':['qualia'], // Can't find
		]//.collectEntries { k,v -> [(k.toLowerCase()):v] }
		
		includeArtists = [
			//'Clepsydra',
			//'Shakary',
			//'Hail Spirit Noir',
		].collect { String it -> it.toLowerCase() }
		
		//maxNoOfAlbums = 12
		storageFolders = []
		storageFolders << new StorageFolder(name: "TopProg1", sizeLimit: 127L*GB)
		//storageFolders << new StorageFolder(name: "TopProg2", sizeLimit: 58L*GB)
		//sizeLimit = null
		
//		topDir = new File(/H:\diskstation\all_music\Music/)
		//topDir = new File("/mnt/jav004/media/ALL_MUSIC/Music")
		topDir = new File(/m:\Music/)
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	List<File> buildCollection(List<TopProgAlbum> albums)
	{
		def albumDirs = []
		long sizeSoFar = 0
		ArtistMatcher artistMatcher = new ArtistMatcher(topDir)
		for (paAlbum in albums) {
			final artistPath = artistMatcher.findArtistDir(paAlbum.artist)
			String artistDir = null
			long albumDirSize = 0
			def albumDir = null
			if (artistPath) {
				artistDir = artistPath.name
				albumDir = findAlbumDir(artistPath, paAlbum)
				if (albumDir) {
					if (!formatIsSupported(albumDir.toString())) continue
//					if (albumDir.toString().contains('mpc')) continue
						if (!albumDirs.contains(albumDir)) {
							if (sizeLimitTotal)
								try {
									albumDirSize = FileUtils.sizeOfDirectory(albumDir)
								} catch (Exception e) {
									e.printStackTrace()
									break
								}
						}
				}
				else {
					missingAlbums << paAlbum;
				}
			}
			else {
				missingAlbums << paAlbum;
				def list = missingArtists.get(paAlbum.artist, [])
				list << paAlbum
			}
			sizeSoFar += albumDirSize;
			//		  println "sizeSoFar=$sizeSoFar"
//			if (sizeLimitTotal && sizeSoFar > sizeLimitTotal) {
//				println "Size limit reached ($sizeSoFar > ${sizeLimitTotal})"
//				break
//			}
			if (albumDir) {
				final dz = dirSizeByArtist.get(artistDir, 0L)
				dirSizeByArtist[artistDir] = dz + albumDirSize
				List<String> testArtistSizes = findLetterLimits()
				if (testArtistSizes.size() > storageFolders.size()) {
					final dirSizeByArtistList = storageFolders*.dirSizeByArtist.collect {
						final first = it.keySet().first()
						final last = it.keySet().last()
						long sum = it.values().sum()
						sum = sum /1024L/1024L
						"${first}-${last}=${sum}MB"
					}
					println "Size limit reached.  $dirSizeByArtistList\n${storageFolders*.dirSizeByArtist}"
					break
				}
				albumDirs << albumDir
				testArtistSizes.eachWithIndex { map,i -> storageFolders[i].dirSizeByArtist = map }
				if (albumDirs.size() % 10 == 0) {
					println "${albumDirs.size()} albums in collection so far.  ${testArtistSizes}"
				}
			}
			if (maxNoOfAlbums && albumDirs.size() >= maxNoOfAlbums) break
			assert sizeSoFar == (Long)dirSizeByArtist.values().sum()
		}
		
		return albumDirs.unique()
	}
	
	boolean formatIsSupported(String fileName) {
		//fileName.contains(FileFormat.MP3.name().toLowerCase())
		true
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	void printStatus(List<File> albumDirs) {
		int n=0
		println(albumDirs.each { n++; println "${n}. ${it}"})
		println "${missingAlbums.size()} missing albums:"
		//missingAlbums.reverse().each { //          println "${it.no}. ${it.artist}-${it.year}-${it.title}-${it.rating}-${it.nof_ratings}-${it.rank}";
			//println "${it.no}. ${it.artist}-${it.year}-${it.title}-${it.rating}-${it.nof_ratings}-${it.rank}" }
		missingArtists.keySet().each { println it }
		//println "sizeSoFar=${sizeSoFar/1024/1024/1024} GB"
		println "${albumDirs.size()} albums"
		//dirSizeByArtist.each { k,v -> println "$k=${v/1024/1024}MB" }
	}

	@CompileStatic(TypeCheckingMode.SKIP)
	static void main(args) {
		// Put all artist dirs in artistDirs
		final builder = new TopProgCollectionBuilder()
		builder.configure()
		print "Reading albums..."
		final albums = builder.readAlbums()
		println "  ${albums.size()} read."
		def albumDirs = builder.buildCollection(albums)
		
		def linkDir = File.createTempFile("linkDir", null)
		linkDir.delete();
		linkDir.mkdir()
		builder.makeLinksWindows(albumDirs, linkDir)
		
		builder.printStatus(albumDirs)
	}
}

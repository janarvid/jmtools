package org.veggeberg.jmtools.progarchives

import java.nio.file.Files;

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

import org.apache.commons.io.FileUtils;
import org.cyberneko.html.parsers.SAXParser
import org.veggeberg.jmtools.FileFormat;

@CompileStatic
class Progarchives
{    
	/*
    static def genreMap = [
    	'Neo Progressive':['adjustFactor':1.2],
    	'Prog Related':['adjustFactor':1.1],
    	'Prog Folk':['adjustFactor':1.1],
    	'Art Rock':['adjustFactor':1.1],
    	'Progressive Metal':['adjustFactor':1.1],
    	'Symphonic Prog':['adjustFactor':1.1],
    	'Italian Symphonic Prog':['adjustFactor':1.21],
    	'Proto-Prog':['adjustFactor':1.1]            
    	];
    
    static def yearAdjust = [2004:1.05, 2005:1.1, 2006:1.15, 2007:1.2];
    */
    
    static final List<String> prepositions = ['a', 'and', 'de', 'del', 'do', 'e', 'le', 'on', 'of', 'the', 'y'];
    
    static String toArtistName(String name) {
        def words = name.toLowerCase().split(' ');
        String ret = '';
        for (i in 0..<words.size()) {
            def word = words[i];
            def uword = word;
            if (i == 0 || (!prepositions.contains(word))) {
                uword = word[0].toUpperCase() + word.substring(1);
            }
            ret += ' ' + uword;
        }
        return ret.trim();
    }

    static String compact(String s) {
        return s.replaceAll(/[\. ]/, '')
    }
    
	@CompileStatic(TypeCheckingMode.SKIP)
    static findAlbumDir(def artistDir, Map album) {
		def albumMatcher = new AlbumMatcher(artistDir)
		return albumMatcher.findAlbumDir(album);
    }
    
	@CompileStatic(TypeCheckingMode.SKIP)
    static toHtmlFile(def file, def artistMap) {
//        list.each {
//            it.artist = toArtistName(it.artist);
//        }
        def writer = new FileWriter(file);
        def html = new groovy.xml.MarkupBuilder(writer);
        html.html {
            head {
                title "${artistMap.size()} Missing Artists";
            }
            body {
                h1 'Missing Artists';
                table(border:1) {
                    artistMap.each { artistName, albums ->
                    	tr {
							td {
								a(href:albums[0].artistUrl, artistName);
							}
							td {
								table(border:1) {
									for (album in albums) {
										tr {
											td album.no;
											td {
												a(href:album.albumUrl, album.title);
											}
											td album.year
											td album.qwr
										}
									}
								}
							}
							td albums[0].genre
                    	}
                    }
                }
            }
        }
    }
    
	@CompileStatic(TypeCheckingMode.SKIP)
    static toAlbumsHtmlFile(def file, albums) {
        def writer = new FileWriter(file);
        def html = new groovy.xml.MarkupBuilder(writer);
        html.html {
            head {
                title "${albums.size()} Missing Albums";
            }
            body {
                h1 'Missing Albums';
                table(border:1) {
                    for (album in albums)
                    tr {
                        td album.no;
                        td {
							a(href:album.artistUrl, album.artist);
                        }
                        td album.year;
                        td {
                            a(href:album.albumUrl, album.title);
                        }
                        //td map.rating;
                        //td map.nof_ratings;
                        //td map.rank;
						td album.qwr;
                        td album.genre;
                        //"${it.no}. ${it.artist}-${it.year}-${it.title}-${it.rating}-${it.nof_ratings}-${it.rank}";
                    }
                }
            }
        }
    }
	
	@CompileStatic(TypeCheckingMode.SKIP)
	static makeLinksLinux(albumPaths, linkDir) {
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
	
	static makeLinksWindows(List<File> albumPaths, linkDir) {
		def scriptText = new StringBuilder()
		for (albumPath in albumPaths) {
			def artistPath = albumPath.parentFile
			def album = albumPath.name
			def artist = artistPath.name
			def linkArtistDir = new File("${linkDir}/${artist}")
			if ( ! linkArtistDir.exists()) {
				if ( ! linkArtistDir.mkdirs() ) {
					throw new IOException("Unable to create directory '$linkArtistDir'")
				}
			}
			def link = "${linkArtistDir}/${album}".replaceAll(/"/, /\\"/)
				.replaceFirst("C:", '/cygdrive/c')
				.replaceAll('\\\\', '/')
				.replaceAll(/\(/, '\\\\(')
				.replaceAll(/\)/, '\\\\)')
			def ap = albumPath.toString().replaceAll(/"/, /\\"/)
				.replaceFirst("m:", '/cygdrive/m')
				.replaceAll('\\\\', '/')
			def cmd = "ln -s \"${ap}\" \"${link}\""
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
		
//	static boolean formatIsSupported(String fileName) {
//		final ret = FileFormat.findAll { it != FileFormat.MP3 }
//			.find { FileFormat it -> fileName.contains(it.name().toLowerCase()) }
//		return (!ret)
//	}

	@CompileStatic(TypeCheckingMode.SKIP)
  static void main(args) 
  {    
	  Progarchives cb = new Progarchives()
      def albumMap = [:]
	  def reader = new TopProgAlbumsReader();
	  def albums = reader.getAlbums()
      
          // Add to album map
	  for (album in albums) {
		  def key = album.artist + '-' + album.year + '-' + album.title
		  //println album
		  albumMap.put(key, album)
	  }
	  //println("albumMap = ${albumMap.size()}")
	  
	  def artistArchive = new ArtistArchive()
	  artistArchive.addAlbums(albumMap.values())
	  
	  // Sort albums and add numbering
      albums = albumMap.values()
      albums = albums.sort { a,b -> b.qwr.toFloat() <=> a.qwr.toFloat() }
      for (i in 0..<albums.size()) {
          albums[i].put('no', i+1);
		  //if (i>10) break
      }
      
	  // Put all artist dirs in artistDirs
      //def topDir = new File("/mnt/jav004/media/ALL_MUSIC/Music")
//      def topDir = new File(/m:\Music/)
	  def topDir = new File(/H:\diskstation\all_music\Music/)
	  def missingAlbums = []
	  def missingArtists = [:]
	  def cantFindArtists = ['one shot', 'jean louis']
	  def albumDirs = []
	  def albumDirsStop = 1000
	  Long sizeLimit = 1L * 1024L * 1024L * 1024L// GB
	  //sizeLimit = null
	  //FileUtils.sizeOfDirectory(topDir)
	  long sizeSoFar = 0
	  ArtistMatcher artistMatcher = new ArtistMatcher(topDir)
	  for (paAlbum in albums) {
		  if (paAlbum.artist.toLowerCase() in cantFindArtists) continue
		  final artistDir = artistMatcher.findArtistDir(paAlbum.artist)
		  long albumDirSize = 0
		  def albumDir = null
		  if (artistDir) {
			  albumDir = findAlbumDir(artistDir, paAlbum)
			  if (albumDir) {
				  //if (albumDir.toString().contains('mpc')) continue
				  if (!formatIsSupported(albumDir.toString())) continue
				  if (!albumDirs.contains(albumDir)) {
					  if (sizeLimit)
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
		  if (sizeLimit && sizeSoFar > sizeLimit) {
			  println "Size limit reached ($sizeSoFar > $sizeLimit)"
			  break
		  }
		  if (albumDir) albumDirs << albumDir
		  if (albumDirs.size() >= albumDirsStop) break
	  }
      
      //def missingAlbums = new LinkedHashMap()
	  /*
	  def artistDirs = [:]
	  def dirs = new ArrayList(Arrays.asList(topDir.listFiles()).findAll{it.isDirectory()})
	  //dirs << new File("/mnt/jav004/media/ALL_MUSIC/work")
	  for (dir in dirs) {
		  dir.eachDir {
			  def key = it.name.toLowerCase()
			  artistDirs.put(key, it)
			  def ck = compact(key)
			  if (artistDirs.get(ck) == null) {
				  artistDirs.put(ck, it)
			  }
		  }
      }
      for (paAlbum in albums) {
          def artist = paAlbum.artist.toLowerCase()
		  if (artist in cantFindArtists) continue
		  
          artist = ArtistMatcher.artistDirMatchMap.get(artist, artist)
          def artistDir = artistDirs.get(artist)
          if (artistDir == null) {
              artistDir = artistDirs.get(compact(artist))
          }
          if (artistDir == null) {
              String art = artist
              for (repl in BaseMatcher.replaceMap) {
                  art = art.replaceAll(repl.key, repl.value)
                  artistDir = artistDirs.get(art)
              }
          }
          if (artistDir == null) {
              def i = artist.indexOf(", ")
              def key = artist
              if (i > 0) {
                  key = artist[i+2..artist.length()-1] + ' ' + artist[0..i-1];
                  artistDir = artistDirs.get(key)
              }
              if (artistDir == null) {
				  def list = missingArtists.get(paAlbum.artist, [])
				  list << paAlbum
              }
          }
          if (artistDir) {
			  def albumDir = findAlbumDir(artistDir, paAlbum)
              if (albumDir) {
				  if ( missingAlbums.size() <= albumDirsStop && (!albumDirs.contains(albumDir))) {
					  albumDirs << albumDir
				  }
              }
			  else {
                  missingAlbums << paAlbum;
              }
          }
		  else {			  
			  missingAlbums << paAlbum;
		  }
          //println haveArtist
          //println it;
      }
      */
//      missingArtists.each {
//          println it;
//      }
	  
//      toHtmlFile('/var/www/html/janni/missingArtists.html', missingArtists);
//      //println "missingArtists = ${missingArtists}";
//      toAlbumsHtmlFile('/var/www/html/janni/missingAlbums.html', missingAlbums);   
	  
	  println artistArchive.artistMap.keySet()
	  int n=0
	  println(albumDirs.each { n++; println "${n}. ${it}"})
	  def linkDir = File.createTempFile("linkDir", null)
	  linkDir.delete();
	  linkDir.mkdir()
	  makeLinksWindows(albumDirs, linkDir)

	  println "${missingAlbums.size()} missing albums:"
	  missingAlbums.reverse().each {
		  //          println "${it.no}. ${it.artist}-${it.year}-${it.title}-${it.rating}-${it.nof_ratings}-${it.rank}";
		  println "${it.no}. ${it.artist}-${it.year}-${it.title}-${it.rating}-${it.nof_ratings}-${it.rank}"
	  }
	  missingArtists.keySet().each { println it }
	  println "sizeSoFar=${sizeSoFar/1024/1024/1024} GB"
	  println "${albumDirs.size()} albums"
  }

}
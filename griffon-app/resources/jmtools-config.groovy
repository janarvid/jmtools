// Top directory where the source or incoming archives are located
final osName = System.getProperty("os.name").toLowerCase()
println "osName=$osName"
final topDir = (osName.contains("win")) ? "m:" : System.getProperty('user.home')  
//sourceArchivesTopDir = "${System.getProperty('user.home')}/incoming".toString()
sourceArchivesTopDir = "${topDir}${File.separator}incoming".toString()

// The work are top directory
//workAreaTopDir = "${System.getProperty('user.home')}/work".toString()
workAreaTopDir = "${topDir}${File.separator}work".toString()

// Collection top directory
//collectionTopDir = "${System.getProperty('user.home')}/Music".toString()
collectionTopDir = "${topDir}${File.separator}Music".toString()

userConfDir = "${System.getProperty('user.home')}/.jmtools".toString()
userCacheDir = "${userConfDir}/cache".toString()

// ProgArchives settings
progArchives {
	urlPrefix = "http://www.progarchives.com"
	topProgAlbums = "top-prog-albums.asp"
}
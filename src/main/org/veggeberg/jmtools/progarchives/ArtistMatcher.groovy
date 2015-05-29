package org.veggeberg.jmtools.progarchives

class ArtistMatcher extends BaseMatcher 
{
	private File lettersTopDir
	private dirByArtist
	
	// Lower Case
	static Map<String,String> artistDirMatchMap = [
//		'4/3 de trio': '4-3 de trio',
//		'andromeda (sweden)': 'andromeda',
//		'akinetón retard': 'akineton retard',
//		'amon düül ii': 'amon duul',
//		'älgarnas trädgård': 'algarnas tradgaard',
//		'ark (nor)': 'ark',
//		'asgard (ita)': 'asgard',
//		'autograph (avtograf / autograf)': 'avtograf',
//		'banda elástica': 'banda elastica',
//		'blue öyster cult': 'blue oyster cult',
//		'blue effect (modrý efekt; m. efekt)': 'blue effect (modry Efekt  m. efekt)',
//		'cid, josé': 'jose cid',
//		'delirium (ita)': 'delirium',
//		'thordendal\'s (fredrik) special defects,': 'fredrik thordendal\'s special defects',
//		'd\'errico, gianni*': 'gianni d\'errico',
//		'dün': 'dun',
//		'emerson lake & palmer (elp)': 'emerson lake and palmer',
//		'emerson lake & palmer': 'emerson lake and palmer',
//		'fermáta': 'fermata',
//		'galahad (en)': 'galahad',
//		'galás, diamanda': 'diamanda galas',
//		'höyry-kone': 'hoyry-kone',
//		'hayward & lodge (the moody blues)': 'justin hayward & john lodge',
//		'korni grupa / kornelyans': 'korni grupa - kornelyans',
//		'lindh project, pär': 'par lindh project',
//		'máquina de hacer pájaros, la': 'la maquina de hacer pajaros',
//		'parsons project, alan': 'the alan parsons project',
//		'popol vuh / popol ace': 'popol vuh - popol ace',
//		'premiata forneria marconi (pfm)': 'premiata forneria marconi',
//		'quasar lux symphoniæ': 'quasar lux symphoniae',
//		'rhapsody / rhapsody of fire': 'rhapsody',
//		'rovescio della medaglia (rdm), il': 'rovescio della medaglia, il',
//		'schicke führs & fröhling (sff)': 'schicke fuhrs & frohling (sff)',
//		'sigur rós': 'sigur ros',
		'shaman / shaaman': 'shaman',
//		'société des timides à la parade des oiseaux, la': 'la societe des timides a la parade des oiseaux',
//		'terço, o': 'o terco',
//		"uovo di colombo, l'": "l'uovo di colombo"
	];

	static String compact(String s) {
		def ret = s
		for (repl in BaseMatcher.replaceMap) {
			ret = ret.replaceAll(repl.key.toUpperCase(), repl.value)
			ret = ret.replaceAll(repl.key.toLowerCase(), repl.value)
			//println ret
		}
		ret = ret.replaceAll(/[\.\/\- ;]/, '')
		return ret
	}

	public ArtistMatcher(lettersTopDir) {
		super();
		this.lettersTopDir = lettersTopDir
		dirByArtist = [:]
		lettersTopDir.eachDir { letterDir ->
			letterDir.eachDir {
				def key = it.name.toLowerCase()
				dirByArtist.put(key, it)
				def ck = compact(key)
				if (dirByArtist.get(ck) == null) {
					dirByArtist.put(ck, it)
				}
			}
		}
	}
	
	public File findArtistDir(String artist) {
		def key = artist.toLowerCase()
		if (artistDirMatchMap.containsKey(key)) {
			key = artistDirMatchMap[key]
		}
		//println "lk=$key"
		File ret = dirByArtist[key]
		if (ret == null) {
			def ck = compact(key)
			ret = dirByArtist[ck]
		}
		return ret
	}
}
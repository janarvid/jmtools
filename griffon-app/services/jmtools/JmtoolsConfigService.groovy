package jmtools

import griffon.core.GriffonApplication;
import groovy.transform.CompileStatic;

@CompileStatic
class JmtoolsConfigService {
	GriffonApplication app
    // void serviceInit() {
    //    // this method is called after the model is instantiated
    // }

    // void serviceDestroy() {
    //    // this method is called when the service is destroyed
    // }
	
	private File getMandatoryDir(configOpt) {
		def dn = app.config[configOpt]
		if (dn == null) 
			throw new IllegalArgumentException("Mandatory config value '$configOpt' not set") 
		File ret = new File(dn.toString())
		if ( ! (ret.exists() && ret.isDirectory()) ) {
			throw new FileNotFoundException("'${ret}' directory does not exist")
		}
		return ret
	}
	
	File getWorkAreaTopDir() {
		return getMandatoryDir("workAreaTopDir")
	}

    File getSourceArchivesTopDir() {
		return getMandatoryDir("sourceArchivesTopDir")
    }
	
	File getCollectionTopDir() {
		return getMandatoryDir("collectionTopDir")
	}
	
	File getUserConfDir() {
		return getExistingOrNewDir("userConfDir")
	}
	
	File getUserCacheDir() {
		File ret = getExistingOrNewDir("userCacheDir") 
		return ret
	}
	
	private File getExistingOrNewDir(configOpt) {
		final dn = (String)app.config[configOpt]
		if (dn == null)
			throw new IllegalArgumentException("Config value '$configOpt' not set")
		final dir = new File(dn)
		if (!dir.exists()) {
			assert dir.mkdirs();
		}
		return dir
	}
	
	File findFileEntry(String entryName) {
		File ret = new File(entryName) 
		while (! ret.exists() && ret != null) {
			ret = ret.getParentFile()
		}
		return ret
	}
}

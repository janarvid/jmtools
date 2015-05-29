package jmtools.test

import jmtools.MyMockGriffonApplication;
import griffon.core.GriffonApplication;
import griffon.test.GriffonUnitTestCase
import griffon.test.mock.MockGriffonApplication;
import groovy.lang.MetaClass;

abstract class MyGriffonUnitTC extends GriffonUnitTestCase 
{
	GriffonApplication app
	static private ConfigObject confObject
	//Map<String,?> args
	
	protected newInstance(Class clazz) {
		def ret = clazz.newInstance()
		ret.app = app
		return ret
	}
	
	static void createTestDirs() {
		for (d in ['sourceArchivesTopDir', "workAreaTopDir", "collectionTopDir"]) {
			File dir = new File("target${File.separator}$d")
			if ( ! dir.exists()) {
				assertTrue(dir.mkdirs())
			}
		}
	}
	
	@Override
	protected void setUp() {
		super.setUp()
		app = new MyMockGriffonApplication()
		app.appConfigClass = Class.forName("Application")
		app.initialize()
		println "app.configClass=${app.configClass}"
		app.config = new ConfigObject()
		if (confObject == null) {
			createTestDirs()
			ConfigSlurper slurper = new ConfigSlurper()
			URL url =new URL("file:///"+new File('test/resources/jmtools-test-config.groovy').absolutePath)
			println "url=$url"
			confObject = slurper.parse(url)
		}
		app.config = confObject
		//app.Startup()
	}
/*
	@Override
	public MetaClass getMetaClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invokeMethod(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMetaClass(MetaClass arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
*/
}

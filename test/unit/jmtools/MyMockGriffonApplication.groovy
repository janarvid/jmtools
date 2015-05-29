package jmtools

import java.util.List;

import org.codehaus.griffon.runtime.core.DefaultMVCGroup;

import griffon.core.GriffonMvcArtifact;
import griffon.core.MVCGroup;
import griffon.core.MVCGroupConfiguration;
import griffon.test.mock.MockGriffonApplication
import groovy.lang.MetaClass;

class MyMockGriffonApplication extends MockGriffonApplication 
{
	@Override
	public MVCGroup buildMVCGroup(String mvcType) {
		MVCGroupConfiguration mvcgConf = mvcGroupManager.findConfiguration(mvcType)
		def members = [:]
		mvcgConf.members.each { k,v ->
			def mem = Class.forName(v).newInstance()
			members[k] = mem
		}
		def m = members['model']
		def v = members['view']
		def c = members['controller']
		if (m.hasProperty('view')) m.view = v
		if (m.hasProperty('controller')) m.controller = c
		v.model = m
		v.controller =c
		c.model = m
		c.view = v
		//def members = [model: Class.forName(m).newInstance(), view: Class.forName(v), controller: Class.forName(c)]
		MVCGroup mvcg = new DefaultMVCGroup(this, mvcgConf, null, members) 
		return mvcg
	}	
}

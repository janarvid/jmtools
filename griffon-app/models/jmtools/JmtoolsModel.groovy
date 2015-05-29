package jmtools

import org.codehaus.griffon.runtime.core.AbstractGriffonModel;

import groovy.beans.Bindable
import groovy.transform.CompileStatic;


//@CompileStatic
class JmtoolsModel extends AbstractGriffonModel {
	@Bindable messageLog = ''
	@Bindable statusMessage
}
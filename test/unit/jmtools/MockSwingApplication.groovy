package jmtools

import static org.junit.Assert.*
import griffon.core.MVCGroup
import griffon.swing.SwingApplication
import griffon.test.*

class MockSwingApplication extends SwingApplication 
{
	Map args
	public MockSwingApplication(Map args=[:]) {
		super();
		this.args = args;
	}
	
	@Override
	public MVCGroup buildMVCGroup(String mvcType, String mvcName, Map<String, Object> args) {
		MVCGroup mvc = super.buildMVCGroup(mvcType, mvcName, args)
		setArgs(mvc, args)
		return mvc
	}
	
	@Override
	public MVCGroup buildMVCGroup(String mvcType, Map<String, Object> args) {
		MVCGroup mvc = super.buildMVCGroup(mvcType, args)
		setArgs(mvc, args)
//		def model, view, controller
//		(model,view,controller) = [mvc.model, mvc.members.view, mvc.controller]
//		if (args) {
//			injectFields(controller, args)
//			injectFields(model, args)
//		}
//		assertSame(controller.model, model)
//		controller.model = model
		return mvc
	}
	
	@Override
	public MVCGroup buildMVCGroup(String mvcType) {
		println "Into ${getClass()}.buildMVCGroup(${mvcType})}"
		return super.buildMVCGroup(mvcType, args);
	}

	void setArgs(MVCGroup mvc, Map<String, Object> args) {
		def model, view, controller
		(model,view,controller) = [mvc.model, mvc.members.view, mvc.controller]
		if (args) {
			injectFields(controller, args)
			injectFields(model, args)
		}
		assertSame(controller.model, model)
		controller.model = model
	}
	
	void injectFields(obj,args) {
		println "injectFields($obj).  args=$args"
		def fields = obj.getClass().declaredFields
		def fieldNames = fields*.name
		args.each { k,v ->
			if (fieldNames.contains(k)) {
				println "$k=$v"
				obj."$k" = v
			}
		}
	}

	@Override
	public Object newInstance(Class clazz, String type) {
		println "clazz=$clazz, type=$type"
		def obj = super.newInstance(clazz, type);
		// TODO
//		if (obj instanceof ListSelectorController) {
//			obj.gSelectConfigService = args.gSelectConfigService
//		}
		return obj
	}
} 

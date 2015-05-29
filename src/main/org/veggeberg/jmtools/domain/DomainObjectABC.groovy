package org.veggeberg.jmtools.domain

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@CompileStatic
abstract class DomainObjectABC
{
	static Collection<String> getFieldNames(Class clazz) {
		List<String> ret = []
		for (Field f in clazz.getDeclaredFields()) {
			String name = f.name
			def mods = f.modifiers
			if (name == 'metaClass') continue
			if (name =~ /^[_$]/) continue
			if (Modifier.isStatic(mods)) continue
			ret << name
		}
		ImmutableList.copyOf(ret)
	}
	
	Collection<String> getFieldNames() {
		getFieldNames(getClass())
	}

	@CompileStatic(TypeCheckingMode.SKIP)
	Map<String,?> asMap() {
		Map map = [:]
		for (f in getFieldNames(getClass())) {
			def val = this."$f"
			if (val) map[f] = val
		}
		ImmutableMap.copyOf(map)
	}
}

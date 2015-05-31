package jmtools.controllers

import griffon.core.GriffonClass
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode;

import javax.swing.JOptionPane
import javax.swing.JTable

import org.apache.commons.io.FileUtils
import org.codehaus.griffon.runtime.core.AbstractGriffonController
import org.codehaus.griffon.runtime.core.DefaultGriffonControllerClass

import com.google.common.collect.ImmutableList;

@CompileStatic
abstract class AbstractJMToolsController extends AbstractGriffonController {
	@Override
	GriffonClass getGriffonClass() {
		GriffonClass gc = new DefaultGriffonControllerClass(app, this.getClass())
		return gc
	}
	
	
	@CompileStatic(TypeCheckingMode.SKIP)
	int[] getMinOneSelectedRows(JTable table, String msg=null) {
		int[] selRows
		edt {
			selRows = table.selectedRows
			if (selRows.size() == 0) {
				if (msg == null) msg = 'Please select at least 1 row from table'
				JOptionPane.showMessageDialog(null, msg)
				return 0
			}
		}
		selRows
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	List<Integer> getSelectedRows(JTable table) {
		List<Integer> selRows
		edt {
			selRows = ImmutableList.copyOf(table.selectedRows)
		}
		selRows
	}
	
	static void doRefreshDir(File dir, List<Map<String,?>> list) {
		list.clear()
		println "dir = $dir"
		if (dir?.exists()) {
			dir.eachFile { File file ->
				def z = file.length()
				if (file.isDirectory()) {
					final zz = FileUtils.sizeOfDirectory(file)
					zz /= 1024 * 1024
					z = "dir[${zz}M]"
				}
				list << [name: file.name.toString(), size:z, date: file.lastModified()]
			}
		}
	}
}

package jmtools

import javax.swing.tree.TreeModel;

import org.codehaus.griffon.runtime.core.AbstractGriffonModel;

import groovy.beans.Bindable
import groovy.transform.CompileStatic;

@CompileStatic
class ProgArchiveTreeModel extends AbstractGriffonModel {
   // @Bindable String propName
	TreeModel treeModel
}
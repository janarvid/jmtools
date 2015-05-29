package jmtools.view

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

import javax.swing.JTree
import javax.swing.tree.TreeModel
import javax.swing.tree.TreeNode

import jmtools.model.Neo4jTreeModel

import org.neo4j.graphdb.Node
import org.neo4j.graphdb.Relationship

@CompileStatic
class Neo4jTree extends JTree {
	public Neo4jTree() {
		super()
	}

	public Neo4jTree(Object[] arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Neo4jTree(Vector<?> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Neo4jTree(Hashtable<?, ?> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Neo4jTree(TreeNode arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Neo4jTree(TreeModel arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Neo4jTree(TreeNode arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	static String formatNode(Node node, Collection<String> props) {
		props.findAll { String it ->
			node.hasProperty(it)
		}
		.collectEntries() { String it ->
			[(it): node.getProperty(it)]
		}.toMapString()
	}
	
	public String convertAlbumToText(Node node) {
		formatNode(node, ['year', 'name', 'rank'])
	}

	@Override
	@CompileStatic(TypeCheckingMode.SKIP)
	public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		//println "convertValueToText(value=$value, selected=$selected, expanded=$expanded, leaf=leaf, row=$row, hasFocus=$hasFocus"
		final thisModel = this.getModel()
		if (thisModel.getClass() != Neo4jTreeModel.class) {
			return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		}
		Neo4jTreeModel treeModel = (Neo4jTreeModel)this.getModel()
		String ret
		if (value.is(treeModel)) { // Root node
			ret = treeModel.topLabel.toString()+"s"
		}
		else if (value instanceof Node) {
			Node node = (Node)value
			final methodName = "convert${node.getLabels().iterator().next().name()}ToText".toString()
			try {
				ret = this."${methodName}"(node)
			}
			catch (MissingMethodException e) {
//				e.printStackTrace()
				ret = ((Node)value).getProperty("name").toString()
			}
		}
		else if (value instanceof Collection && ((Collection)value).iterator().next() instanceof Relationship) {
			final rels = (Collection<Relationship>)value
			ret = rels.iterator().next().type.name()
		}
		else {
			ret = super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		}
		return ret
	}
}

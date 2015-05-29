package jmtools.model

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel
import javax.swing.tree.TreePath;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

@CompileStatic
class Neo4jTreeModel implements TreeModel {
	Label topLabel
	GraphDatabaseService graphDb
	
	@Override
	public void addTreeModelListener(TreeModelListener arg0) {
		println "Into addTreeModelListener($arg0)}"
		// TODO Auto-generated method stub
	}

	@Override
	public Object getChild(Object parent, int index) {
		println "Into getChild($parent, $index)}"
		Transaction tx = graphDb.beginTx()
		List items = getTreeNodes(parent)
		final node = items[index]
		println "node=$node"
		/*
		def props = []
		for (prop in node.getPropertyKeys()) {
			props << node.getProperty((String)prop) 
		}
		*/
		def ret = node
//		return node.getProperty("name");
//		def ret = props.join(",")
		println "ret=$ret"
		tx.success()
		return ret
	}
	
	private List<Node> getTopNodes() {
		List<Node> nodes = []
		GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(topLabel).each { Node node ->
			nodes << node
		}
		return nodes
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	private List getTreeNodes(parent) {
		List items
		if (parent.is(this)) {
			items = topNodes
		}
		else if (parent instanceof Node) { // Return a List<Collection<Relationship>> 
			Node node = (Node)parent
			Map<String, List<Relationship>> relsByType = new TreeMap()
//			node.getRelationships().each { Relationship rel -> println rel }
			for (Relationship rel in node.getRelationships()) {
				final type = rel.type.name()
				final rels = relsByType.get(type, [])
				rels << rel
			}
			items = relsByType.values().toList()
		}
		else if (parent instanceof Collection) { // Returns a List<Node>
			items = parent.collect { Relationship rel ->
				rel.endNode
			}
		}
	}

	@Override
	public int getChildCount(Object parent) {
		println "Into getChildCount($parent)}"
		Transaction tx = graphDb.beginTx()
		List items = getTreeNodes(parent)
		tx.success()
		return items.size();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		println "getIndexOfChild($parent)"
		return 0;
	}

	@Override
	public Object getRoot() {
		println "getRoot()"
		return this;
	}

	@Override
	public boolean isLeaf(Object arg0) {
		//println "isLeaf($arg0)"
		return false;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		println "removeTreeModelListener($arg0)"
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		println "valueForPathChanged($arg0,$arg1)"
	}
}

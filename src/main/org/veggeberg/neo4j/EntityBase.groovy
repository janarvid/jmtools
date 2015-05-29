package org.veggeberg.neo4j

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import groovy.transform.CompileStatic;

@CompileStatic
abstract class EntityBase
{	
	GraphDatabaseService graphDb
	Node node

	public EntityBase(GraphDatabaseService graphDb) {
		this.graphDb = graphDb
		node = graphDb.createNode(DynamicLabel.label(getClass().getSimpleName()))
	}
	
	public EntityBase(Node node) {
		this.node = node
	}
	
	@Override
	public String toString() {
		final map = node.propertyKeys.collectEntries {String it -> [(it):node.getProperty(it)]}
		return "${getClass().getSimpleName()}$map"
	}
}

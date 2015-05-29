package org.veggeberg.neo4j

import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Node;

class Genre extends EntityBase
{
	public Genre(GraphDatabaseService graphDb) {
		super(graphDb);
	}

	public Genre(Node node) {
		super(node);
	}
	
	void setName(String name) {
		node.setProperty("name", name)
	}
	
	String getName() {
		return node.getProperty("name")
	}
	
	void setDescription(String description) {
		node.setProperty("description", description)
	}
	
	String getDescription() {
		node.getProperty("description")
	}
}

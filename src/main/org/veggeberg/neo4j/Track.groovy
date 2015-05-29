package org.veggeberg.neo4j

import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Node;

class Track extends EntityBase
{
	public Track(GraphDatabaseService graphDb) {
		super(graphDb);
	}

	public Track(Node node) {
		super(node);
	}
	
	void setTitle(String title) {
		node.setProperty("title", title)
	}
	
	void setRank(int rank) {
		node.setProperty("rank", rank)
	}
	
	void setDuration(int seconds) {
		node.setProperty("duration", seconds)
	}
}

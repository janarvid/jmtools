package org.veggeberg.neo4j

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.tooling.GlobalGraphOperations;
import org.neo4j.graphdb.Node;

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

@CompileStatic
class MusicRepository
{
	GraphDatabaseService graphDb
	
	public MusicRepository(GraphDatabaseService graphDb) {
		this.graphDb = graphDb
	}
	
	List<Artist> getArtists() {
		final nodes = GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(DynamicLabel.label(Artist.class.simpleName))
		nodes.collect { Node n ->
			new Artist(n)
		}
	}
	
	@CompileStatic(value=TypeCheckingMode.SKIP)
	private void populateEntity(EntityBase entity, map) {
		map.each { String k,v ->
			entity."$k" = v
		}
	}
	
	Artist createArtist(Map<String, ?> map=null) {
		final ret = new Artist(graphDb)
		if (map) {
			populateEntity(ret, map)	
		}
		return ret
	}
	
	Genre createGenre(Map<String, ?> map=null) {
		final ret = new Genre(graphDb)
		if (map) populateEntity(ret, map)
		return ret
	}
}

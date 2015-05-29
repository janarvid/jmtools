package org.veggeberg.neo4j

import java.util.List;

import groovy.transform.CompileStatic;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

@CompileStatic
class Album extends EntityBase 
{
	public Album(GraphDatabaseService graphDb) {
		super(graphDb)
	}
	
	public Album(Node node) {
		super(node)
	}
	
	void setName(String name) {
		node.setProperty("name", name)
	}

	void setYear(Integer year) {
		node.setProperty("year", year)
	}
	
	void addTrack(Track track) {
		node.createRelationshipTo(track.node, RelTypes.HAS_TRACK);
	}
	
	List<Track> getTracks() {
		node.getRelationships(RelTypes.HAS_TRACK).collect { Relationship r ->
			new Track(r.endNode)
		}
	}
	
	Artist getArtist() {
		return new Artist(node.getRelationships(RelTypes.HAS_TRACK).first().startNode)
	}
	
	@Override
	public String toString() {
		final map = node.propertyKeys.collectEntries {String it -> [(it):node.getProperty(it)]}
		map.numberOfTracks = tracks.size()
		return "${getClass().getSimpleName()}$map"
	}
}

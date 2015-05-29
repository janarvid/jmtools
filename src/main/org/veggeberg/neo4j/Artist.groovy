package org.veggeberg.neo4j

import groovy.transform.CompileStatic;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

@CompileStatic
class Artist extends EntityBase
{
	Artist(GraphDatabaseService graphDb) {
		super(graphDb)
	}
	
	public Artist(Node node) {
		super(node)
	}
	
	void setName(String name) {
		node.setProperty("name", name)
	}
	
	String getName() {
		return node.getProperty("name")
	}

	void setCountry(String country) {
		node.setProperty("country", country)
	}
	
	Genre getGenre() {
		Genre ret = null
		if (node.hasRelationship(RelTypes.HAS_GENRE)) {
			Node n = node.getRelationships(RelTypes.HAS_GENRE).first()?.endNode
			ret = new Genre(n)
		}
		return ret
	}
	
	void setGenre(Genre genre) {
		node.createRelationshipTo(genre.node, RelTypes.HAS_GENRE);
	}
	
	void addAlbum(Album album) {
		def rel = node.createRelationshipTo(album.node, RelTypes.HAS_ALBUM);
	}
	
	List<Album> getAlbums() {
		node.getRelationships(RelTypes.HAS_ALBUM).collect { Relationship r ->
			new Album(r.endNode) 
		}
	} 

	@Override
	public String toString() {
		final map = node.propertyKeys.collectEntries {String it -> [(it):node.getProperty(it)]}
		map.albums = albums
		map.genre = genre?.name
		return "${getClass().getSimpleName()}$map"
	}
}

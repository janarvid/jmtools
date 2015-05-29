package org.veggeberg.neo4j

import java.io.File;
import java.io.IOException;

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.tooling.GlobalGraphOperations;
import org.veggeberg.jmtools.progarchives.TopProgAlbumsReader;

@CompileStatic
class FirstTry {
	Artist artist
	Album album
	GraphDatabaseService graphDb
	private static final String DB_PATH = "target/neo4j-firsttry-db";

	public FirstTry() {
		// TODO Auto-generated constructor stub
	}

	void initDb() {
		clearDb();
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		registerShutdownHook( graphDb );
		println "DB init done."
		// END SNIPPET: startDb
		/*
		 // START SNIPPET: transaction
		 try ( Transaction tx = graphDb.beginTx() )
		 {
		 // Database operations go here
		 // END SNIPPET: transaction
		 // START SNIPPET: addData
		 firstNode = graphDb.createNode();
		 firstNode.setProperty( "message", "Hello, " );
		 secondNode = graphDb.createNode();
		 secondNode.setProperty( "message", "World!" );
		 relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
		 relationship.setProperty( "message", "brave Neo4j " );
		 // END SNIPPET: addData
		 // START SNIPPET: readData
		 System.out.print( firstNode.getProperty( "message" ) );
		 System.out.print( relationship.getProperty( "message" ) );
		 System.out.println( secondNode.getProperty( "message" ) );
		 // END SNIPPET: readData
		 greeting = ( (String) firstNode.getProperty( "message" ) )
		 + ( (String) relationship.getProperty( "message" ) )
		 + ( (String) secondNode.getProperty( "message" ) );
		 // START SNIPPET: transaction
		 tx.success();
		 }
		 // END SNIPPET: transaction
		 * 
		 */
	}

	static main(args) {
		final firstTry = new FirstTry()
		firstTry.initDb();
		firstTry.createDb();
		firstTry.listDb();
		/*
		 firstTry.removeData();
		 */
		firstTry.shutDown();
	}

	private void listDb() {
		Transaction tx = graphDb.beginTx()
		GlobalGraphOperations.at(graphDb).allNodes.each { Node node ->
			println("${node.labels}=$node ");
			node.getPropertyKeys();
			for (String key : node.getPropertyKeys()) {
				println("  " + key + "=" + node.getProperty(key));
			}
		}
		GlobalGraphOperations.at(graphDb).allRelationships.each { Relationship rel ->
			println("rel=$rel ${rel.type}");
			rel.getPropertyKeys();
			for (String key : rel.getPropertyKeys()) {
				println("  " + key + "=" + rel.getProperty(key));
			}
		}
		tx.success();
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	List getAlbums() {
		final urls = []
		new File("test/resources/progarchives").eachFile { File file ->
			if (file.name.startsWith("top-prog-albums-")) {
				urls << file.toString()
			}
		}
	//	def urls = ['../test/resources/progarchives/top-prog-albums-2013.html']
	//	urls << '../test/resources/progarchives/top-prog-albums-2012.html'
		TopProgAlbumsReader reader = new TopProgAlbumsReader()
		List albums = reader.getAlbums(urls)
	}

	@CompileStatic(TypeCheckingMode.SKIP)
	void createDb() {
		List albums = getAlbums()
		final artistMap = [:]
		final genreMap = [:]
		for (album in albums) {
			artistMap.get(album.artist, []) << album
			artistMap.get(album.genre, []) << album
		}
		artistMap.each { artistName, albumList ->
			
		}
		final artists = albums*.artist.unique().sort()
		final genres = albums*.genre.unique().sort()
		
		Transaction tx = graphDb.beginTx()
		MusicRepository musicRepo = new MusicRepository(graphDb)
		Genre symponicProg = musicRepo.createGenre(name: "Symphonic Prog")
		Genre heavyProg = musicRepo.createGenre(name: "Heavy Prog")
		artist = musicRepo.createArtist(name:"Spock's Beard", genre: symponicProg, country:"United States")
		
		artist = new Artist(graphDb);
		artist.name = "Genesis"
		artist.genre = symponicProg
		artist.country = "United Kingdom"
		
		artist = new Artist(graphDb);
		artist.name = "Haken"
		artist.genre = heavyProg
		artist.country = "United Kingdom"
		
//		for (artist in artists) {
//			final a = new Artist(graphDb);
//			a.name = artist
//		}
		
		for (genre in genres) {
			final g = new Genre(graphDb);
			g.name = genre
		}
		
		album = new Album(graphDb)
		album.name = "Aquarius"
		album.year = 2010
		artist.addAlbum(album)
		
		album = new Album(graphDb)
		album.name = "Visions"
		album.year = 2011
		artist.addAlbum(album)
		
		album = new Album(graphDb)
		album.name = "The Mountain"
		album.year = 2013
		artist.addAlbum(album)
		Track track = new Track(graphDb)
		track.title = "The Path"
		track.rank = 1
		track.duration = 2*60+47
		album.addTrack(track)
		println "album.artist=${album.artist}"
		println "musicRepo.artists = ${musicRepo.artists*.name.sort()}"
		
		println artist
		
		/*
		 secondNode = graphDb.createNode();
		 secondNode.setProperty( "message", "World!" );
		 relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
		 relationship.setProperty( "message", "brave Neo4j " );
		 // END SNIPPET: addData
		 // START SNIPPET: readData
		 System.out.print( firstNode.getProperty( "message" ) );
		 System.out.print( relationship.getProperty( "message" ) );
		 System.out.println( secondNode.getProperty( "message" ) );
		 // END SNIPPET: readData
		 greeting = ( (String) firstNode.getProperty( "message" ) )
		 + ( (String) relationship.getProperty( "message" ) )
		 + ( (String) secondNode.getProperty( "message" ) );
		 */
		tx.success();
	}

	private void clearDb() {
		try {
			FileUtils.deleteRecursively( new File( DB_PATH ) );
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	private static void registerShutdownHook( final GraphDatabaseService graphDb ) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook( new Thread()
				{
					@Override
					public void run()
					{
						graphDb.shutdown();
					}
				} );
	}

	void shutDown() {
		println "\nShutting down database ..."
		// START SNIPPET: shutdownServer
		graphDb.shutdown();
		println "\nShutdown finished."
	}
}

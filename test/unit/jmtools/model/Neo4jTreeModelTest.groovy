package jmtools.model;

import static org.junit.Assert.*;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import jmtools.view.Neo4jTree;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.veggeberg.jmtools.progarchives.TopProgAlbumsReader;
import org.veggeberg.neo4j.RelTypes;

class Neo4jTreeModelTest
{
	Neo4jTreeModel cut
	static final GRAPH_DB_DIR = "/tmp/new4j-testDb"
	static GraphDatabaseService graphDb
	static List albums
	static Map<String,Collection<String>> artistsByGenre
	static List<Node> genreNodes
	static Label genreLabel, artistLabel, albumLabel
	JFrame frame
	
	static bootstrap() {
		println "Into bootstrap()"
		final urls = []
		new File("data/progarchives").eachFile { File file ->
			if (file.name.startsWith("top-prog-albums-")) {
				urls << file.toString()
				//println file
			}
		}
	//	def urls = ['../test/resources/progarchives/top-prog-albums-2013.html']
	//	urls << '../test/resources/progarchives/top-prog-albums-2012.html'
		TopProgAlbumsReader reader = new TopProgAlbumsReader()
		albums = reader.getAlbums(urls)
		println "${albums.size()} albums read"
		println albums[0]
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FileUtils.deleteDirectory( new File( GRAPH_DB_DIR ) );
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(GRAPH_DB_DIR)
		println "graphDb=$graphDb"
		bootstrap()
		initDb()
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		println "Shutting down..."
		//graphDb.shutdown()
	}

	@Before
	public void setUp() throws Exception {
		cut = new Neo4jTreeModel()
		cut.graphDb = graphDb
		cut.topLabel = DynamicLabel.label("Genre")
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private static toArtistsByGenre() {
		def orderedByName = [ compare:{ a,b -> a.title <=> b.title } ] as Comparator
		def orderedByYear = [ compare:{ a,b -> a.year <=> b.year } ] as Comparator
		def orderedByRank = [ compare:{ a,b -> a.rank <=> b.rank } ] as Comparator
		final ret = new TreeMap()
		for (album in albums) {
			final albumsByArtist = ret.get(album.genre, new TreeMap())
			final albumList = albumsByArtist.get(album.artist, new TreeSet(orderedByRank))
//			println "albumList=$albumList"
//			println "album=$album"
			albumList.add(album)
		}
		return ret
	}
	
	@Test
	public void testGetChildOfTopNode() { // Level 1.  Should be genre nodes
		final genres = artistsByGenre.keySet().toList()
		assertEquals(genres.size(), cut.getChildCount(cut))
		for (i in 0..<genres.size()) {
			assertTrue(cut.getChild(cut,i) instanceof Node)
			assertEquals(genres[i], cut.getChild(cut,i).getProperty('name'))
		}
	}
	
	@Test
	public void testGetChildOfGenre() { // Level 2.  Should be Collection<Relationship>
		final genreNode = genreNodes[0]
		assertEquals(1, cut.getChildCount(genreNode))
		Collection<Relationship> gotRels = cut.getChild(genreNode,0)
		assertEquals(RelTypes.HAS_GENRE.name(), gotRels.iterator().next().type.name())
	}
	
	@Test
	public void testGetChildOfGenreRelation() { // Level 3.  Should be artists
		final genreNode = genreNodes[0]
		Transaction transaction = graphDb.beginTx()
		final rels = genreNode.getRelationships().collect { it }
		assertEquals(artistsByGenre.values().iterator().next().size(), rels.size())
		assertEquals(rels.size(), cut.getChildCount(rels))
		Node gotArtistNode = cut.getChild(rels, 0)
		assertEquals(artistLabel, gotArtistNode.labels.iterator().next())
		transaction.success()
	}

	//@Test Too slow and shows windows
	public void testTree() {
		frame = new JFrame("My frame")
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.size = new Dimension(600, 800)
		JTree tree = new Neo4jTree()
		//println tree.cellRenderer
		tree.model = cut
		JScrollPane sp = new JScrollPane(tree)
		frame.add(sp)
		//frame.pack()
		frame.visible = true
		System.sleep(100000)
	}
	
	static void initDb() {
		Transaction transaction = graphDb.beginTx()
		artistsByGenre = toArtistsByGenre()
		genreLabel = DynamicLabel.label("Genre")
		artistLabel = DynamicLabel.label("Artist")
		albumLabel = DynamicLabel.label("Album")
		genreNodes = []
		artistsByGenre.each { genre, albumsByArtist ->
			Node genreNode = graphDb.createNode(genreLabel);
			genreNodes << genreNode
			genreNode.setProperty("name", genre)
			albumsByArtist.each { artist, albums ->
				Node artistNode = graphDb.createNode(artistLabel);
				artistNode.setProperty("name", artist)
				genreNode.createRelationshipTo(artistNode, RelTypes.HAS_GENRE);
				for (album in albums) {
					Node albumNode = graphDb.createNode(albumLabel);
					//println "album=$album"
					albumNode.setProperty("name", album.title)
					albumNode.setProperty("year", album.year)
					albumNode.setProperty("rank", album.rank)
					artistNode.createRelationshipTo(albumNode, RelTypes.HAS_ALBUM);
				}
			}
		}
		transaction.success()
		transaction.close()
	}
}

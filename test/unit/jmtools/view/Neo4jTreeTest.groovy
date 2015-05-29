package jmtools.view;

import static org.junit.Assert.*;

import javax.management.relation.RelationType;

import jmtools.model.Neo4jTreeModel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.neo4j.graphdb.Node;
import org.veggeberg.neo4j.RelTypes;

class Neo4jTreeTest 
{
	Neo4jTree cut
	Neo4jTreeModel treeModel
	static GraphDatabaseService graphDb
	static Node genreNode
	static Map<RelationshipType,Relationship> relationshipsByTypeMap
	static final String GENRE_NAME = "Neo-Prog"
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase()
		initDb()
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		treeModel = new Neo4jTreeModel()
		treeModel.graphDb = graphDb
		treeModel.topLabel = DynamicLabel.label("Genre")
		cut = new Neo4jTree()
		cut.model = treeModel
	}

	@After
	public void tearDown() throws Exception {
	}
	
	static void initDb() {
		Transaction transaction = graphDb.beginTx()
		Label genreLabel = DynamicLabel.label("Genre")
		Label artistLabel = DynamicLabel.label("Artist")
		genreNode = graphDb.createNode(genreLabel);
		genreNode.setProperty("name", GENRE_NAME)
		relationshipsByTypeMap = [:]
		for (artist in ['Marillion']) {
			Node artistNode = graphDb.createNode(artistLabel);
			artistNode.setProperty("name", artist)
			final rel = artistNode.createRelationshipTo(genreNode, RelTypes.HAS_GENRE);
			relationshipsByTypeMap.get(rel.type, []) << rel
		}
		transaction.success()
		transaction.close()
	}

	@Test
	public void testConvertValueToText() {
		// Root Node
		assertEquals(treeModel.topLabel.toString()+"s", cut.convertValueToText(treeModel, false, false, false, 0, false))
		
		// neo4j Node Node
		assertEquals(GENRE_NAME, cut.convertValueToText(genreNode, false, false, false, 0, false))
		
		// neo4j Relationships Node
		assertEquals(RelTypes.HAS_GENRE.name(), cut.convertValueToText(relationshipsByTypeMap.values().iterator().next(), false, false, false, 0, false))
	}
	
	@Test void testConvertValueToTextAlbum() {
		Transaction transaction = graphDb.beginTx()
		
		Node node = graphDb.createNode(DynamicLabel.label("Album"));
		def map = [year: 2006, name: "My Album Name", rank: 4.33f]
		map.each { k, v ->
			node.setProperty(k, v)
		}
		node.setProperty('url', 'my url')
		def actual = cut.formatNode(node, map.keySet())
//		println "exp=${map.toString()}"
//		println "actual=$actual"
		assertEquals(map.toString(), actual)
		assertEquals(map.toString(), cut.convertValueToText(node, false, false, false, 0, false))
		
		transaction.success()
		transaction.close()
	}
}

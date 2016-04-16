package model.graph;

import static model.graph.JSONTestFileData.DIEGO;
import static model.graph.JSONTestFileData.FEED_FILE_PATH;
import static model.graph.JSONTestFileData.GROUP_ID;
import static model.graph.JSONTestFileData.GROUP_USER;
import static model.graph.JSONTestFileData.GUSTAVO;
import static model.graph.JSONTestFileData.MURILLO;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.fbdata.GroupFeed;
import model.fbdata.Post;

public class GroupNetworkGraphParsingTest {

	private static ObjectMapper mapper;

	private static GroupNetworkGraph graph = new GroupNetworkGraph(GROUP_ID);

	@BeforeClass
	public static void setUpOnce() throws Exception {
		setUpMapper();
		loadGraph();
	}

	private static void setUpMapper() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	private static List<Post> retrievePosts()
			throws IOException, JsonParseException, JsonMappingException {
		InputStream is = GroupNetworkGraphParsingTest.class
				.getResourceAsStream(FEED_FILE_PATH);
		GroupFeed feed = mapper.readValue(is, GroupFeed.class);
		final List<Post> posts = feed.getPosts();
		return posts;
	}

	private static void loadGraph()
			throws IOException, JsonParseException, JsonMappingException {
		List<Post> posts = retrievePosts();
		posts.forEach(
				p -> p.getInteractions().forEach(i -> graph.addInteraction(i)));
	}

	@Test
	public void shouldConstructTheGraph() throws Exception {
		assertNotNull(graph);
	}

	@Test
	public void shouldOnlyHaveUsersAsNodes() throws Exception {
		Collection<Node> nodes = graph.getNodes();
		assertThat(nodes, hasItem(new Node(DIEGO)));
		assertThat(nodes, hasItem(new Node(MURILLO)));
		assertThat(nodes, hasItem(new Node(GUSTAVO)));
		assertThat(nodes.size(), equalTo(3));
	}

	@Test
	public void shouldNotHaveGroupAsANode() {
		Node groupNode = new Node(GROUP_USER);
		assertThat(graph.getNodes(), not(hasItem(groupNode)));
	}

	@Test
	public void shouldNotHaveGroupAsUser() {
		assertThat(graph.getUsers(), not(hasItem(GROUP_USER)));
	}

	@Test
	public void shouldNotHaveInteractionsEnvolvingTheGroupUser() {
		Node groupNode = new Node(GROUP_USER);
		Collection<Node> nodes = graph.getNodes();
		nodes.forEach(
				n -> assertThat(n.getNeighbors(), not(hasItem(groupNode))));
	}

	@Test
	public void shouldHaveCorrectNumberOfEdges() {
		assertEquals(8, graph.getNumEdges().intValue());
	}

	@Test
	public void shouldHaveDiegoTaggingMurilloFourTimes() {
		Node diegoNode = graph.getUserNode(DIEGO);
		Interactions i = diegoNode.getInteractionsWith(MURILLO);
		assertEquals(4, i.getTotalTags().intValue());
	}

	@Test
	public void shouldHaveDiegoTaggingGustavoThreeTimes() {
		Node diegoNode = graph.getUserNode(DIEGO);
		Interactions i = diegoNode.getInteractionsWith(GUSTAVO);
		assertEquals(3, i.getTotalTags().intValue());
	}

	@Test
	public void shouldHaveCommentFromMurilloToDiego() {
		Node murilloNode = graph.getUserNode(MURILLO);
		Interactions i = murilloNode.getInteractionsWith(DIEGO);
		assertEquals(1, i.getTotalComments().intValue());
	}

	@Test
	public void shouldHaveDiegoNodeWithCorrectDegrees() {
		Node diegoNode = graph.getUserNode(DIEGO);
		assertEquals(1, diegoNode.getInDegree().intValue());
		assertEquals(7, diegoNode.getOutDegree().intValue());
	}

	@Test
	public void shouldHaveMurilloNodeWithCorrectDegrees() {
		Node murilloNode = graph.getUserNode(MURILLO);
		assertEquals(4, murilloNode.getInDegree().intValue());
		assertEquals(1, murilloNode.getOutDegree().intValue());
	}

	@Test
	public void shouldHaveGustavoNodeWithCorrectDegrees() {
		Node gustavoNode = graph.getUserNode(GUSTAVO);
		assertEquals(3, gustavoNode.getInDegree().intValue());
		assertEquals(0, gustavoNode.getOutDegree().intValue());
	}

}

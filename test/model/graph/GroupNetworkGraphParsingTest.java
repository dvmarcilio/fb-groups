package model.graph;

import static model.graph.JSONTestFileData.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
import model.fbdata.User;

public class GroupNetworkGraphParsingTest {

	private static ObjectMapper mapper;

	private static GroupNetworkGraph graph = new GroupNetworkGraph();

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
		User groupUser = new User(GROUP_ID, GROUP_NAME);
		assertThat(graph.getNodes(), not(hasItem(new Node(groupUser))));
	}

}

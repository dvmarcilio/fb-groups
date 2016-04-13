package model.graph;

import java.io.IOException;
import java.io.InputStream;
import java.security.acl.Group;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.fbdata.GroupFeed;
import model.fbdata.Interaction;
import model.fbdata.Post;
import model.fbdata.User;

public class Main {

	private static ObjectMapper mapper = new ObjectMapper();

	private static GroupNetworkGraph graph = new GroupNetworkGraph();

	public static void main(String[] args) throws Exception {
		setUpMapper();
		addUsers();
		addInteractions();
		FunWithBasicStats stats = new FunWithBasicStats(graph);
		stats.show();
	}

	private static void setUpMapper() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	private static void addUsers() throws Exception {
		retrieveUsers().forEach(u -> graph.addNode(u));
	}

	private static List<User> retrieveUsers()
			throws JsonParseException, JsonMappingException, IOException {
		InputStream is = Main.class
				.getResourceAsStream("/data/maristao_members.json");
		return mapper.readValue(is, mapper.getTypeFactory()
				.constructCollectionLikeType(List.class, User.class));
	}

	private static void addInteractions()
			throws JsonParseException, JsonMappingException, IOException {
		List<Post> posts = GroupFeed.retrieveAllPosts();
		System.out.println("Total posts:" + posts.size());
		posts.forEach(
				p -> p.getInteractions().forEach(i -> graph.addInteraction(i)));
	}

}

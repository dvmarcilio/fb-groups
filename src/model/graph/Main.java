package model.graph;

import java.io.IOException;
import java.io.InputStream;
import java.security.acl.Group;
import java.util.List;
import java.util.Set;

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
		printUsersNotInTheGroupAnymore();
	}

	private static void setUpMapper() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	private static void addUsers() throws Exception {
		final List<User> users = retrieveUsers();
		System.out.println("Users: " + users.size());
		users.forEach(u -> graph.addNode(u));
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

	private static void printUsersNotInTheGroupAnymore()
			throws JsonParseException, JsonMappingException, IOException {
		Set<User> usersNotInMembersJson = graph.getUsers();
		usersNotInMembersJson.removeAll(retrieveUsers());
		System.out.println(
				"Users with Facebook probably deleted or not in the group anymore: "
						+ usersNotInMembersJson.size());
		System.out.println(usersNotInMembersJson);
	}

}

package model.graph;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.fbdata.GroupFeed;
import model.fbdata.Post;
import model.fbdata.User;

public class Main {

	private static ObjectMapper mapper = new ObjectMapper();

	private static GroupNetworkGraph graph;

	public static void main(String[] args) throws Exception {
		loadTheGraph();
		showStats();
	}

	private static void loadTheGraph() throws Exception, JsonParseException,
			JsonMappingException, IOException {
		setUpMapper();
		addInteractions();
	}

	private static void setUpMapper() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	private static void addInteractions()
			throws JsonParseException, JsonMappingException, IOException {
		List<Post> posts = GroupFeed.retrieveAllPosts();
		instantiateTheGraph(posts);
		System.out.println("Total posts:" + posts.size());
		posts.forEach(
				p -> p.getInteractions().forEach(i -> graph.addInteraction(i)));
	}

	private static void instantiateTheGraph(List<Post> posts) {
		Long groupID = getGroupId(posts);
		graph = new GroupNetworkGraph(groupID);
	}

	private static Long getGroupId(List<Post> posts) {
		return posts.get(0).getGroupID();
	}

	private static void showStats()
			throws JsonParseException, JsonMappingException, IOException {
		FunWithBasicStats stats = new FunWithBasicStats(graph);
		stats.show();
		printUsersNotInTheGroupAnymore();
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

	private static List<User> retrieveUsers()
			throws JsonParseException, JsonMappingException, IOException {
		InputStream is = Main.class
				.getResourceAsStream("/data/maristao_members.json");
		return mapper.readValue(is, mapper.getTypeFactory()
				.constructCollectionLikeType(List.class, User.class));
	}

}

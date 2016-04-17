package model.graph;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

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
		showPageRank();
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
		Set<User> usersNotInMembersJson = new HashSet<>(graph.getUsers());
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

	private static void showPageRank() {
		System.out.println("\n\n\n\n");
		PageRank pr = new PageRank(graph);
		Map<Node, Double> nodesToPageRank = pr.compute();
		nodesToPageRank = sortByValue(nodesToPageRank);
		for (Map.Entry<Node, Double> entry : nodesToPageRank.entrySet()) {
			System.out.println(entry.getKey().getUser() + "\nPageRank:"
					+ entry.getValue() + "\n");
		}
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Map.Entry.<K, V> comparingByValue().reversed())
				.forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}

}

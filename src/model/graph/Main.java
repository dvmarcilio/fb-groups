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

	public static boolean PRINT = true;

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
		if (PRINT) {
			stats.show();
			printUsersNotInTheGroupAnymore();
		}
	}

	private static void printUsersNotInTheGroupAnymore()
			throws JsonParseException, JsonMappingException, IOException {
		Set<User> usersNotInMembersJson = new HashSet<>(graph.getUsers());
		usersNotInMembersJson.removeAll(retrieveUsers());
		System.out.println(
				"\nUsers with Facebook probably deleted or not in the group anymore: "
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
		System.out.println("\n\n");
		printNonScaledPageRank();
		printScaledPageRank();
	}

	private static void printNonScaledPageRank() {
		Map<Node, Double> nodesToPageRank = new PageRank(graph).compute();
		assertThatValuesSumUpToOne(nodesToPageRank);
		nodesToPageRank = sortByValue(nodesToPageRank);
		printPageRanks(nodesToPageRank);
	}

	private static void printPageRanks(Map<Node, Double> nodesToPageRank) {
		if (PRINT) {
			for (Map.Entry<Node, Double> entry : nodesToPageRank.entrySet()) {
				System.out.println(entry.getKey() + "\nPageRank:"
						+ entry.getValue() + "\n");
			}
		}
	}

	private static void assertThatValuesSumUpToOne(
			Map<Node, Double> nodesToPageRank) {
		double valuesSum = nodesToPageRank.values().stream()
				.mapToDouble(v -> v.doubleValue()).sum();
		if (Math.abs(valuesSum - PageRank.TOTAL_PAGE_RANK) >= 0.001) {
			System.out.println("PAGERANK IS WRONG. Aborting...");
			System.exit(0);
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

	private static void printScaledPageRank() {
		Map<Node, Double> scaledNodeToPageRank = new PageRank(graph)
				.computeScaled();
		assertThatValuesSumUpToOne(scaledNodeToPageRank);
		scaledNodeToPageRank = sortByValue(scaledNodeToPageRank);
		System.out.println("\n\n SCALED PAGE RANK");
		printPageRanks(scaledNodeToPageRank);
	}

}

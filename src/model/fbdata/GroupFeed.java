package model.fbdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GroupFeed {

	private static final String JSON_EXTENSION = ".json";

	@JsonProperty("data")
	private List<Post> posts;

	public List<Post> getPosts() {
		return posts;
	}

	public static void main(String[] args)
			throws JsonParseException, JsonMappingException, IOException {
		List<Post> allPosts = retrieveAllPosts();

		System.out.println("Total posts: " + allPosts.size());
		Long totalInteractions = allPosts.stream()
				.mapToLong(p -> p.getInteractions().size()).sum();
		System.out.println("Total Interactions: " + totalInteractions);
	}

	public static List<Post> retrieveAllPosts()
			throws IOException, JsonParseException, JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		List<Post> allPosts = new LinkedList<>();
		List<String> files = Arrays.asList("1", "2", "3", "4", "5", "6", "7",
				"8");

		for (String fileName : files) {
			GroupFeed feed = parseGroupFeedFromJson(mapper, fileName);
			printFeedRead(fileName, feed);
			allPosts.addAll(feed.getPosts());
		}
		return allPosts;
	}

	private static GroupFeed parseGroupFeedFromJson(ObjectMapper mapper,
			String fileName) throws IOException, JsonParseException,
					JsonMappingException {
		String fullFileName = "/data/" + fileName + JSON_EXTENSION;
		InputStream is = GroupFeed.class.getResourceAsStream(fullFileName);
		return mapper.readValue(is, GroupFeed.class);
	}

	private static void printFeedRead(String fileName, GroupFeed feed) {
		System.out.println("================================================");
		System.out.println("File '" + fileName + JSON_EXTENSION + "' read.");
		System.out.println("Posts: " + feed.getPosts().size());
		Long totalInteractions = feed.getPosts().stream()
				.mapToLong(p -> p.getInteractions().size()).sum();
		System.out.println("Interactions: " + totalInteractions);
		System.out.println("================================================");
	}

}

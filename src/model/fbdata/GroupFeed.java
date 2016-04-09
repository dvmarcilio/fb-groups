package model.fbdata;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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
	public List<Post> posts;

	public List<Post> getPosts() {
		return posts;
	}

	public static void main(String[] args)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		// InputStream is = GroupFeed.class
		// .getResourceAsStream("/data/maristao_novo_feed_2.json");
		List<Post> allPosts = new LinkedList<>();
		List<String> files = Arrays.asList("1", "2", "3");

		for (String fileName : files) {
			GroupFeed feed = parseGroupFeedFromJson(mapper, fileName);
			printFeedRead(fileName, feed);
			allPosts.addAll(feed.getPosts());
		}

//		InputStream is = GroupFeed.class.getResourceAsStream("/data/3.json");
//		takeBuggedCharOut(is);

		// GroupFeed feed = mapper.readValue(json.substring(1),
		// GroupFeed.class);

		System.out.println("Total posts: " + allPosts.size());
		Long totalInteractions = allPosts.stream()
				.mapToLong(p -> p.getInteractions().size()).sum();
		System.out.println("Total Interactions: " + totalInteractions);
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
		System.out.println("File '" + fileName + JSON_EXTENSION + "'read.");
		System.out.println("Posts: " + feed.getPosts().size());
		Long totalInteractions = feed.getPosts().stream()
				.mapToLong(p -> p.getInteractions().size()).sum();
		System.out.println("Interactions: " + totalInteractions);
		System.out.println("================================================");
	}

	private static void takeBuggedCharOut(InputStream is) throws IOException,
			UnsupportedEncodingException, FileNotFoundException {
		String json = convertStreamToString(is);
		json.replace("\r\n", "\\n");

		try (Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("teste.json"), "UTF-8"))) {
			out.write(json);
		}
	}

	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is, "UTF-8")
				.useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}

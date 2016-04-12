package model.fbdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GroupFeed {

	public static void main(String[] args)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		InputStream is = GroupFeed.class
				.getResourceAsStream("/data/maristao.json");
		List<Post> posts = mapper.readValue(is, mapper.getTypeFactory()
				.constructCollectionLikeType(List.class, Post.class));

		Long totalInteractions = posts.stream()
				.mapToLong(p -> p.getInteractions().size()).sum();
		System.out.println("Total Interactions: " + totalInteractions);
	}
}

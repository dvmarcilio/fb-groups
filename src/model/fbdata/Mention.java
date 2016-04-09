package model.fbdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mention {

	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Mention [User Mentioned=" + getUserMentioned() + "]";
	}

	public User getUserMentioned() {
		return new User(id, name);
	}

	public static void main(String[] args)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		InputStream is = Mention.class
				.getResourceAsStream("/data/mentions_test.json");

		List<Mention> testObj = mapper.readValue(is,
				new TypeReference<List<Mention>>() {
				});
		System.out.println(testObj);
	}

}

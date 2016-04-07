package model.fbdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GroupMembers {

	@JsonProperty("users")
	private List<User> users = Collections.emptyList();

	@Override
	public String toString() {
		return "GroupMembers [users=" + users + "]";
	}

	public int getUsersCount() {
		return users.size();
	}

	public Collection<User> getUsers() {
		return users;
	}

	public static void main(String[] args)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		InputStream is = GroupMembers.class
				.getResourceAsStream("/data/maristao_members.json");
		GroupMembers testObj = mapper.readValue(is, GroupMembers.class);
		System.out.println(testObj.getUsersCount());
	}
}

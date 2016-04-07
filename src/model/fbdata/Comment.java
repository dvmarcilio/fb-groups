package model.fbdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Comment {

	private Long id;

	@JsonProperty("from")
	private User author;

	@JsonProperty("message_tags")
	private List<Mention> mentions = Collections.emptyList();

	public User getAuthor() {
		return author;
	}

	public List<Mention> getMentions() {
		return mentions;
	}

	public void setMentions(List<Mention> mentions) {
		this.mentions = mentions;
	}

	public Long getId() {
		return id;
	}

	public Collection<Interaction> getMentionsInteractions() {
		List<Interaction> interactions = new ArrayList<Interaction>(
				mentions.size());
		for (Mention mention : mentions) {
			interactions
					.add(new Interaction(author, mention.getUserMentioned()));
		}
		return interactions;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", author=" + author + ", mentions="
				+ mentions + "]";
	}

	public static void main(String[] args)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		InputStream is = Comment.class
				.getResourceAsStream("/data/comment_teste.json");
		Comment testObj = mapper.readValue(is, Comment.class);
		System.out.println(testObj);
		System.out.println(testObj.getMentionsInteractions());
	}

}

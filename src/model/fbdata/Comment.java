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

import model.fbdata.Interaction.Type;

public class Comment {

	private Long id;

	@JsonProperty("from")
	private User author;

	@JsonProperty("message_tags")
	private List<Tag> tags = Collections.emptyList();

	public User getAuthor() {
		return author;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public Long getId() {
		return id;
	}

	public Collection<Interaction> getTagsInteractions() {
		List<Interaction> interactions = new ArrayList<Interaction>(
				tags.size());
		for (Tag tag : tags) {
			interactions.add(
					new Interaction(author, tag.getUserTagged(), Type.MENTION));
		}
		return interactions;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", author=" + author + ", tags=" + tags
				+ "]";
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
		System.out.println(testObj.getTagsInteractions());
	}

}

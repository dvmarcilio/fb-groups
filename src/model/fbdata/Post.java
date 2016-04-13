package model.fbdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.fbdata.Interaction.Type;

public class Post {

	@JsonProperty("id")
	private String id;

	@JsonProperty("from")
	private User author;

	@JsonProperty("comments")
	private PostComments postComments;

	@JsonProperty("likes")
	private PostLikes postLikes;

	@JsonProperty("story_tags")
	private StoryTags storyTags;

	@JsonProperty("with_tags")
	private WithTags withTags;

	@JsonProperty("message_tags")
	private PostMessageTags messageTags;

	public String getId() {
		return id;
	}

	public User getAuthor() {
		return author;
	}

	public List<Comment> getComments() {
		if (postComments == null)
			return Collections.emptyList();
		return postComments.getComments();
	}

	public List<User> getUsersWhoLiked() {
		if (postLikes == null)
			return Collections.emptyList();
		return postLikes.getLikesFrom();
	}

	public List<Tag> getWithTags() {
		if (withTags == null)
			return Collections.emptyList();
		return withTags.getTags();
	}

	public List<Tag> getStoryTags() {
		if (storyTags == null)
			return Collections.emptyList();
		return storyTags.getTags();
	}

	public List<Tag> getMessageTags() {
		if (messageTags == null)
			return Collections.emptyList();
		return messageTags.getTags();
	}

	@Override
	public String toString() {
		return "Post [author=" + author + ", \nlikes=" + getUsersWhoLiked()
				+ ", \ncomments=" + getComments() + "]";
	}

	public Collection<Interaction> getInteractions() {
		List<Interaction> interactions = new LinkedList<Interaction>();
		interactions.addAll(getLikesInteractions());
		interactions.addAll(getCommentsInteractions());
		interactions.addAll(getStoryTagsInteractions());
		interactions.addAll(getWithTagsInteractions());
		return interactions;
	}

	private Collection<Interaction> getLikesInteractions() {
		List<Interaction> likesInteractions = new ArrayList<Interaction>(
				getUsersWhoLiked().size());
		for (User userWhoLiked : getUsersWhoLiked()) {
			likesInteractions
					.add(new Interaction(userWhoLiked, author, Type.LIKE));
		}
		return likesInteractions;
	}

	private Collection<Interaction> getCommentsInteractions() {
		List<Interaction> commentsInteractions = new ArrayList<Interaction>(
				getComments().size());
		for (Comment comment : getComments()) {
			commentsInteractions.add(
					new Interaction(comment.getAuthor(), author, Type.COMMENT));
			commentsInteractions.addAll(comment.getTagsInteractions());
		}
		return commentsInteractions;
	}

	private Collection<Interaction> getStoryTagsInteractions() {
		List<Interaction> storyTagsInteractions = new ArrayList<Interaction>(
				getStoryTags().size());
		for (Tag tag : getStoryTags()) {
			storyTagsInteractions.add(
					new Interaction(author, tag.getUserTagged(), Type.TAG));
		}
		return storyTagsInteractions;
	}

	private Collection<Interaction> getWithTagsInteractions() {
		List<Interaction> withTagsInteractions = new ArrayList<Interaction>(
				getWithTags().size());
		for (Tag tag : getWithTags()) {
			withTagsInteractions.add(
					new Interaction(author, tag.getUserTagged(), Type.TAG));
		}
		return withTagsInteractions;
	}

	public static void main(String[] args)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		InputStream is = Post.class
				.getResourceAsStream("/data/post_with_mentions.json");
		Post testObj = mapper.readValue(is, Post.class);
		System.out.println(testObj);
		System.out.println("Story tags: " + testObj.getStoryTags());
		System.out.println("With tags: " + testObj.getWithTags());
		System.out.println(testObj.getStoryTags().size() + " Story tags");
		System.out.println(testObj.getWithTags().size() + " With tags");
		System.out.println(testObj.getUsersWhoLiked().size() + " likes");
		System.out.println(testObj.getComments().size() + " comments");
		System.out.println(testObj.getInteractions().size() + " interactions");
		Collection<Interaction> postInteractions = testObj.getInteractions();
		for (Interaction interaction : postInteractions) {
			System.out.println(interaction);
		}
	}

}

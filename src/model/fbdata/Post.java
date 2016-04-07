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

public class Post {

	@JsonProperty("from")
	private User author;

	@JsonProperty("comments")
	private PostComments postComments;

	@JsonProperty("likes")
	private PostLikes postLikes;

	public User getAuthor() {
		return author;
	}

	public List<Comment> getComments() {
		if (postComments == null)
			return Collections.emptyList();
		return postComments.getComments();
	}

	public List<User> getLikes() {
		if (postLikes == null)
			return Collections.emptyList();
		return postLikes.getLikesFrom();
	}


	@Override
	public String toString() {
		return "Post [author=" + author + ", \nlikes=" + getLikes()
				+ ", \ncomments=" + getComments() + "]";
	}

	public Collection<Interaction> getInteractions() {
		List<Interaction> interactions = new LinkedList<Interaction>();
		interactions.addAll(getLikesInteractions());
		interactions.addAll(getCommentsInteractions());
		return interactions;
	}

	private Collection<Interaction> getLikesInteractions() {
		List<Interaction> likes = new ArrayList<Interaction>(
				getLikes().size());
		for (User userWhoLiked : getLikes()) {
			likes.add(new Interaction(userWhoLiked, author));
		}
		return likes;
	}

	private Collection<Interaction> getCommentsInteractions() {
		List<Interaction> comments = new ArrayList<Interaction>(
				getComments().size());
		for (Comment comment : getComments()) {
			comments.add(new Interaction(comment.getAuthor(), author));
			comments.addAll(comment.getMentionsInteractions());
		}
		return comments;
	}

	public static void main(String[] args)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		InputStream is = Post.class.getResourceAsStream("/data/post_big.json");
		Post testObj = mapper.readValue(is, Post.class);
		System.out.println(testObj);
		System.out.println(testObj.getLikes().size() + " likes");
		System.out.println(testObj.getComments().size() + " comments");
		System.out.println(testObj.getInteractions().size() + " interactions");
		Collection<Interaction> postInteractions = testObj.getInteractions();
		for (Interaction interaction : postInteractions) {
			System.out.println(interaction);
		}
	}

}

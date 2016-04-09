package model.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.fbdata.Comment;
import model.fbdata.Interaction;
import model.fbdata.Mention;
import model.fbdata.Post;
import model.fbdata.User;

public class PostTest {

	private static final int MURILLO_COMMENT_ID = 1111;

	private static final long DIEGO_COMMENT_ID = 1000L;

	private static final User GUSTAVO = new User(987L, "gustavo");

	private static final User MURILLO = new User(123L, "murillo");

	private static final User DIEGO = new User(666L, "diego");

	private static final String JSON_FILE_PATH = "/data/post.json";

	private static Post post;

	private static ObjectMapper mapper;

	@BeforeClass
	public static void setUpOnce() throws Exception {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		InputStream is = PostTest.class.getResourceAsStream(JSON_FILE_PATH);
		post = mapper.readValue(is, Post.class);
	}

	@Test
	public void parsesTheID() {
		assertEquals("groupId_postId", post.getId());
	}

	@Test
	public void parsesTheAuthor() {
		assertEquals(DIEGO, post.getAuthor());
	}

	@Test
	public void shouldHaveWithTags() {
		List<Mention> withTags = post.getWithTags();
		assertThat(withTags, hasItem(mention(MURILLO)));
		assertThat(withTags, hasItem(mention(GUSTAVO)));
		assertEquals(2, withTags.size());
	}

	private Mention mention(User to) {
		return new Mention(to);
	}

	@Test
	public void shouldHaveNoStoryTags() throws Exception {
		List<Mention> storyTags = post.getStoryTags();
		assertTrue(storyTags.isEmpty());
	}

	@Test
	public void shouldHaveMessageTags() {
		List<Mention> messageTags = post.getMessageTags();
		assertThat(messageTags, hasItem(mention(MURILLO)));
		assertThat(messageTags, hasItem(mention(GUSTAVO)));
		assertThat(messageTags, hasItem(mention(DIEGO)));
		assertEquals(3, messageTags.size());
	}

	@Test
	public void shouldHaveLikes() {
		List<User> usersWhoLikedPost = post.getUsersWhoLiked();
		assertThat(usersWhoLikedPost, hasItem(DIEGO));
		assertEquals(1, usersWhoLikedPost.size());
	}

	@Test
	public void shouldHaveComments() throws Exception {
		assertEquals(2, post.getComments().size());
	}

	@Test
	public void shouldHaveCommentFromDiegoTaggingMurillo() {
		Comment comment = post.getComments().stream()
				.filter(c -> c.getId() == DIEGO_COMMENT_ID)
				.collect(Collectors.toList()).get(0);

		assertThat(comment.getAuthor(), equalTo(DIEGO));

		assertThat(comment.getMentions(), hasItem(mention(MURILLO)));
		assertEquals(1, comment.getMentions().size());
	}

	@Test
	public void shouldHaveCommentFromMurillo() {
		Comment comment = post.getComments().stream()
				.filter(c -> c.getId() == MURILLO_COMMENT_ID)
				.collect(Collectors.toList()).get(0);

		assertThat(comment.getAuthor(), equalTo(MURILLO));
		assertTrue(comment.getMentions().isEmpty());
	}

}

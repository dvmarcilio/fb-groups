package model.graph;

import static data.JSONTestFileData.DIEGO;
import static data.JSONTestFileData.DIEGO_COMMENT_ID;
import static data.JSONTestFileData.GROUP_ID;
import static data.JSONTestFileData.GUSTAVO;
import static data.JSONTestFileData.MURILLO;
import static data.JSONTestFileData.MURILLO_COMMENT_ID;
import static data.JSONTestFileData.POST_FILE_PATH;
import static data.JSONTestFileData.POST_ID_1;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.fbdata.Comment;
import model.fbdata.Interaction;
import model.fbdata.Interaction.Type;
import model.fbdata.Post;
import model.fbdata.Tag;
import model.fbdata.User;

public class PostTest {

	private static Post post;

	private static ObjectMapper mapper;

	@BeforeClass
	public static void setUpOnce() throws Exception {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		InputStream is = PostTest.class.getResourceAsStream(POST_FILE_PATH);
		post = mapper.readValue(is, Post.class);
	}

	@Test
	public void shouldParsePostIdCorrectly() {
		assertEquals(POST_ID_1, post.getId());
	}

	@Test
	public void shouldParsePostAuthorCorrectly() {
		assertEquals(DIEGO, post.getAuthor());
	}

	@Test
	public void shouldReturnTheCorrectGroupID() {
		assertEquals(GROUP_ID, post.getGroupID());
	}

	@Test
	public void shouldHaveAllWithTags() {
		List<Tag> withTags = post.getWithTags();
		assertThat(withTags, hasItem(tag(MURILLO)));
		assertThat(withTags, hasItem(tag(GUSTAVO)));
		assertEquals(2, withTags.size());
	}

	private Tag tag(User to) {
		return new Tag(to);
	}

	@Test
	public void shouldHaveNoStoryTags() throws Exception {
		List<Tag> storyTags = post.getStoryTags();
		assertTrue(storyTags.isEmpty());
	}

	@Test
	public void shouldHaveAllMessageTags() {
		List<Tag> messageTags = post.getMessageTags();
		assertThat(messageTags, hasItem(tag(MURILLO)));
		assertThat(messageTags, hasItem(tag(GUSTAVO)));
		assertThat(messageTags, hasItem(tag(DIEGO)));
		assertEquals(3, messageTags.size());
	}

	@Test
	public void shouldHaveAllLikes() {
		List<User> usersWhoLikedPost = post.getUsersWhoLiked();
		assertThat(usersWhoLikedPost, hasItem(DIEGO));
		assertEquals(1, usersWhoLikedPost.size());
	}

	@Test
	public void shouldHaveAllComments() throws Exception {
		assertEquals(2, post.getComments().size());
	}

	@Test
	public void shouldHaveCommentFromDiegoTaggingMurillo() {
		Comment comment = post.getComments().stream()
				.filter(c -> c.getId() == DIEGO_COMMENT_ID)
				.collect(Collectors.toList()).get(0);

		assertThat(comment.getAuthor(), equalTo(DIEGO));

		assertThat(comment.getTags(), hasItem(tag(MURILLO)));
		assertEquals(1, comment.getTags().size());
	}

	@Test
	public void shouldHaveCommentFromMurillo() {
		Comment comment = post.getComments().stream()
				.filter(c -> c.getId() == MURILLO_COMMENT_ID)
				.collect(Collectors.toList()).get(0);

		assertThat(comment.getAuthor(), equalTo(MURILLO));
		assertTrue(comment.getTags().isEmpty());
	}

	@Test
	public void shouldHaveAllInteractions() {
		assertEquals(9, post.getInteractions().size());
	}

	@Test
	public void shouldHaveAllCommentsInteractions() {
		assertThat(post.getInteractions(),
				hasItem(new Interaction(DIEGO, DIEGO, Type.COMMENT)));
		assertThat(post.getInteractions(),
				hasItem(new Interaction(MURILLO, DIEGO, Type.COMMENT)));
	}

	// Including with_tags, message_tags, and tags on comment
	// This particular file has:
	// with_tags: 3
	// message_tags: 2
	// comment tag: 1
	@Test
	public void shouldHaveAllTagsInteractions() {
		List<Interaction> tags = post.getInteractions().stream()
				.filter(i -> i.getType().equals(Type.TAG))
				.collect(Collectors.toList());
		assertEquals(6, tags.size());
	}

	@Test
	public void shouldHaveAllLikesInteractions() {
		List<Interaction> likes = post.getInteractions().stream()
				.filter(i -> i.getType().equals(Type.LIKE))
				.collect(Collectors.toList());
		assertEquals(1, likes.size());
	}

}

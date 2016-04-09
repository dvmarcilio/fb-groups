package model.graph;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;

import model.fbdata.Mention;
import model.fbdata.Post;
import model.fbdata.User;
import sun.invoke.empty.Empty;

public class PostTest {

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
		assertEquals(2, messageTags.size());
	}

}

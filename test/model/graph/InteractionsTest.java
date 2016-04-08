package model.graph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.fbdata.Interaction.Type;
import model.graph.Interactions;

public class InteractionsTest {

	private Interactions interactions;

	private int previousTotalComments;
	private int previousTotalMentions;

	@Before
	public void setUp() {
		interactions = new Interactions();
		previousTotalComments = 0;
		previousTotalMentions = 0;
	}

	@Test
	public void addingCommentAddsTotalsCorrectly() throws Exception {
		interactions.add(Type.COMMENT);
		assertEquals(previousTotalMentions, interactions.getTotalMentions());
		assertEquals(1, interactions.getTotalComments());
		assertEquals(1, interactions.getTotal());
	}

	@Test
	public void addingMentionAddsTotalsCorrectly() throws Exception {
		interactions.add(Type.MENTION);
		assertEquals(previousTotalComments, interactions.getTotalComments());
		assertEquals(1, interactions.getTotalMentions());
		assertEquals(1, interactions.getTotal());
	}

}

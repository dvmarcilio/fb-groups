package model.graph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.fbdata.Interaction.Type;
import model.graph.Interactions;

public class InteractionsTest {

	private Interactions interactions;

	private int previousTotalComments;
	private int previousTotalTags;

	@Before
	public void setUp() {
		interactions = new Interactions();
		previousTotalComments = 0;
		previousTotalTags = 0;
	}

	@Test
	public void shouldAddTotalCorrectlyWhenAddingComments() throws Exception {
		interactions.add(Type.COMMENT);
		assertEquals(previousTotalTags, interactions.getTotalTags().intValue());
		assertEquals(1, interactions.getTotalComments().intValue());
		assertEquals(1, interactions.getTotal().intValue());
	}

	@Test
	public void shouldAddTotalCorrectlyWhenAddingTags() throws Exception {
		interactions.add(Type.TAG);
		assertEquals(previousTotalComments,
				interactions.getTotalComments().intValue());
		assertEquals(1, interactions.getTotalTags().intValue());
		assertEquals(1, interactions.getTotal().intValue());
	}

}

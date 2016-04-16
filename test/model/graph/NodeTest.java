package model.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.fbdata.Interaction.Type;
import model.fbdata.User;

public class NodeTest {

	private User u1 = new User(1L, "1");
	private User u2 = new User(2L, "2");
	private Node n1 = new Node(u1);
	private Node n2 = new Node(u2);

	@Test
	public void shouldOnlyIncreaseInteractingNodeOutDegree() {
		n1.addInteractionWith(n2, Type.TAG);
		assertEquals(0, n1.getInDegree().intValue());
		assertEquals(1, n1.getOutDegree().intValue());
	}

	@Test
	public void shouldOnlyIncreaseInteractedNodeInDegree() {
		n1.addInteractionWith(n2, Type.TAG);
		assertEquals(1, n2.getInDegree().intValue());
		assertEquals(0, n2.getOutDegree().intValue());
	}

	@Test
	public void shouldReturnInteractionsWithUser() {
		n1.addInteractionWith(n2, Type.COMMENT);
		n1.addInteractionWith(n2, Type.COMMENT);
		n1.addInteractionWith(n2, Type.TAG);
		n1.addInteractionWith(n2, Type.LIKE);

		Interactions interactions = n1.getInteractionsWith(u2);

		assertEquals(4, interactions.getTotal().intValue());
		assertEquals(2, interactions.getTotalComments().intValue());
		assertEquals(1, interactions.getTotalLikes().intValue());
		assertEquals(1, interactions.getTotalTags().intValue());
	}

	@Test
	public void shouldReturnEmptyInteractionsWhenUserIsNotANeighbor() {
		Interactions interactions = n1
				.getInteractionsWith(new User(33L, "not a neighbor"));

		assertEquals(0, interactions.getTotal().intValue());
		assertEquals(0, interactions.getTotalComments().intValue());
		assertEquals(0, interactions.getTotalLikes().intValue());
		assertEquals(0, interactions.getTotalTags().intValue());
	}
}

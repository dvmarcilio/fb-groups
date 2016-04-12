package model.graph;

import static org.junit.Assert.*;
import model.fbdata.Interaction;
import model.fbdata.User;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {

	private User u1;
	private User u2;
	private Node n1;
	private Node n2;

	@Before
	public void setUp() {
		u1 = new User(1L, "1");
		n1 = new Node(u1);
		u2 = new User(2L, "2");
		n2 = new Node(u2);
	}

	@Test
	public void shouldOnlyIncreaseInteractingNodeOutDegree() throws Exception {
		n1.addInteractionWith(n2, Interaction.Type.MENTION);
		assertEquals(0, n1.getInDegree().intValue());
		assertEquals(1, n1.getOutDegree().intValue());
	}

	@Test
	public void shouldOnlyIncreaseInteractedNodeInDegree() throws Exception {
		n1.addInteractionWith(n2, Interaction.Type.MENTION);
		assertEquals(1, n2.getInDegree().intValue());
		assertEquals(0, n2.getOutDegree().intValue());
	}

}

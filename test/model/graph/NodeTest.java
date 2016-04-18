package model.graph;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static model.graph.GraphTestHelper.*;
import static data.BookGraphExampleData.*;

import org.junit.Test;

import model.fbdata.Interaction.Type;
import model.fbdata.User;

public class NodeTest {

	private User u1 = new User(1L, "1");
	private User u2 = new User(2L, "2");
	private User u3 = new User(3L, "3");
	private Node n1 = new Node(u1);
	private Node n2 = new Node(u2);
	private Node n3 = new Node(u3);

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
	public void shouldReturnEmptyInteractionsWithWhenUserIsNotANeighbor() {
		Interactions interactions = n1
				.getInteractionsWith(new User(33L, "not a neighbor"));

		assertEquals(0, interactions.getTotal().intValue());
		assertEquals(0, interactions.getTotalComments().intValue());
		assertEquals(0, interactions.getTotalLikes().intValue());
		assertEquals(0, interactions.getTotalTags().intValue());
	}

	@Test
	public void shouldReturnOutNeighborsCorrectly() {
		n1.addInteractionWith(n2, Type.TAG);
		n1.addInteractionWith(n3, Type.COMMENT);

		assertThat(n1.getOutNeighbors(), hasItem(n2));
		assertThat(n1.getOutNeighbors(), hasItem(n3));
		assertEquals(2, n1.getOutNeighbors().size());
	}

	@Test
	public void shouldReturnOutNeighborsCorrectlyFromTheExampleGraph() {
		GroupNetworkGraph graph = NON_SCALED_EXAMPLE_GRAPH;
		Node nodeA = graph.getUserNode(USER_A);

		assertThat(nodeA.getOutNeighbors(), hasItem(NODE_B));
		assertThat(nodeA.getOutNeighbors(), hasItem(NODE_C));
		assertEquals(2, nodeA.getOutNeighbors().size());
	}

	@Test
	public void shouldReturnInNeighborsCorrectlyFromTheExampleGraph() {
		GroupNetworkGraph graph = NON_SCALED_EXAMPLE_GRAPH;
		Node nodeA = graph.getUserNode(USER_A);
		
		assertThat(nodeA.getInNeighbors(), hasItem(NODE_D));
		assertThat(nodeA.getInNeighbors(), hasItem(NODE_E));
		assertThat(nodeA.getInNeighbors(), hasItem(NODE_F));
		assertThat(nodeA.getInNeighbors(), hasItem(NODE_G));
		assertThat(nodeA.getInNeighbors(), hasItem(NODE_H));
		assertEquals(5, nodeA.getInNeighbors().size());
	}

}

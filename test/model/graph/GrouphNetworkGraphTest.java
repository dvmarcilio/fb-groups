package model.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import model.fbdata.Interaction;
import model.fbdata.Interaction.Type;
import model.fbdata.User;

public class GrouphNetworkGraphTest {

	private GroupNetworkGraph graph = new GroupNetworkGraph();

	private User u1 = new User(1L, "1");
	private User u2 = new User(2L, "2");

	@Test
	public void shouldReturnTrueWhenAddingAnInteraction() {
		boolean successfullyAdded = graph
				.addInteraction(commentFromUser1ToUser2());
		assertTrue(successfullyAdded);
	}

	@Test
	public void shouldReturnFalseAndNotAddSelfInteraction() {
		boolean successfullyAdded = graph
				.addInteraction(new Interaction(u1, u1, Type.LIKE));
		assertFalse(successfullyAdded);
		assertEquals(0, graph.getNumEdges().intValue());
	}

	@Test
	public void shouldCreateNodesIfTheyDontExistWhenAddingAnInteraction() {
		graph.addInteraction(commentFromUser1ToUser2());

		assertTrue(graph.hasNodeForUser(u1));
		assertTrue(graph.hasNodeForUser(u2));
	}

	private Interaction commentFromUser1ToUser2() {
		return new Interaction(u1, u2, Type.COMMENT);
	}

	@Test
	public void shouldReturnTrueWhenSuccessfullyAddedANode() {
		boolean successfullyAdded = graph.addNode(u1);
		assertTrue(successfullyAdded);
	}

	@Test
	public void shouldReturnFalseWhenAddingAnExistentNode() {
		graph.addNode(u1);
		boolean successfullyAdded = graph.addNode(u1);
		assertFalse(successfullyAdded);
	}

	@Test
	public void shouldNotCreateNewNodeWhenTryingToAddExistingNode() {
		graph.addNode(u1);
		graph.addInteraction(commentFromUser1ToUser2());
		Node originalNode = graph.getUserNode(u1);

		graph.addNode(u1);
		Node nodeAddedAgain = graph.getUserNode(u1);

		assertThatNodeAddedAgainIsEqualToOriginalNode(originalNode,
				nodeAddedAgain);
	}

	private void assertThatNodeAddedAgainIsEqualToOriginalNode(
			Node originalNode, Node nodeAddedAgain) {
		assertEquals(originalNode, nodeAddedAgain);
		assertEquals(originalNode.getInDegree(), nodeAddedAgain.getInDegree());
		assertEquals(originalNode.getOutDegree(),
				nodeAddedAgain.getOutDegree());
		assertEquals(originalNode.getNeighbors(),
				nodeAddedAgain.getNeighbors());
	}

}

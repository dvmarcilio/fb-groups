package model.graph;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import model.fbdata.Interaction;
import model.fbdata.Interaction.Type;
import model.fbdata.User;

public class GrouphNetworkGraphBehaviorTest {

	private static final Long GROUP_ID = 54321L;

	private static final User GROUP_USER = new User(GROUP_ID, "GROUP");
	private static final User USER_1 = new User(1L, "1");
	private static final User USER_2 = new User(2L, "2");

	private GroupNetworkGraph graph = new GroupNetworkGraph(GROUP_ID);

	@Test
	public void shouldReturnTrueWhenAddingAnInteraction() {
		boolean successfullyAdded = graph
				.addInteraction(commentFromUser1ToUser2());
		assertTrue(successfullyAdded);
	}

	@Test
	public void shouldReturnFalseAndNotAddSelfInteraction() {
		boolean successfullyAdded = graph
				.addInteraction(new Interaction(USER_1, USER_1, Type.LIKE));
		assertFalse(successfullyAdded);
		assertEquals(0, graph.getNumEdges().intValue());
	}

	@Test
	// This actually happens because some users might have deleted their profile
	// or they are not in the group anymore, but they were at some point.
	// Even if not technically in the group, in both situations they are still
	// present in the Group Feed.
	public void shouldCreateNodesIfTheyDontExistWhenAddingAnInteraction() {
		graph.addInteraction(commentFromUser1ToUser2());

		assertTrue(graph.hasNodeForUser(USER_1));
		assertTrue(graph.hasNodeForUser(USER_2));
	}

	private Interaction commentFromUser1ToUser2() {
		return new Interaction(USER_1, USER_2, Type.COMMENT);
	}

	@Test
	public void shouldReturnTrueWhenSuccessfullyAddedANode() {
		boolean successfullyAdded = graph.addNode(USER_1);
		assertTrue(successfullyAdded);
	}

	@Test
	public void shouldReturnFalseWhenAddingAnExistentNode() {
		graph.addNode(USER_1);
		boolean successfullyAdded = graph.addNode(USER_1);
		assertFalse(successfullyAdded);
	}

	@Test
	public void shouldNotCreateNewNodeWhenTryingToAddExistingNode() {
		graph.addNode(USER_1);
		graph.addInteraction(commentFromUser1ToUser2());
		Node originalNode = graph.getUserNode(USER_1);

		graph.addNode(USER_1);
		Node nodeAddedAgain = graph.getUserNode(USER_1);

		assertThatNodeAddedAgainIsEqualToOriginalNode(originalNode,
				nodeAddedAgain);
	}

	private void assertThatNodeAddedAgainIsEqualToOriginalNode(
			Node originalNode, Node nodeAddedAgain) {
		assertEquals(originalNode, nodeAddedAgain);
		assertEquals(originalNode.getInDegree(), nodeAddedAgain.getInDegree());
		assertEquals(originalNode.getOutDegree(),
				nodeAddedAgain.getOutDegree());
		assertEquals(originalNode.getOutNeighbors(),
				nodeAddedAgain.getOutNeighbors());
	}

	@Test
	public void shouldReturnFalseWhenAddingGroupNode() {
		boolean successfullyAdded = graph.addNode(GROUP_USER);
		assertFalse(successfullyAdded);
	}

	@Test
	public void shouldNotAddGroupNode() {
		graph.addNode(GROUP_USER);
		assertThat(graph.getNodes(), not(hasItem(new Node(GROUP_USER))));
	}

	@Test
	public void shouldNotAddGroupAsAnUser() {
		graph.addNode(GROUP_USER);
		assertThat(graph.getUsers(), not(hasItem(GROUP_USER)));
	}

	@Test
	public void shouldReturnFalseWhenAddingInteractionToGroup() {
		Interaction i = new Interaction(USER_1, GROUP_USER, Type.COMMENT);
		boolean successfullyAdded = graph.addInteraction(i);
		assertFalse(successfullyAdded);
	}

	@Test
	public void shouldReturnFalseWhenAddingInteractionFromGroup() {
		Interaction i = new Interaction(GROUP_USER, USER_2, Type.LIKE);
		boolean successfullyAdded = graph.addInteraction(i);
		assertFalse(successfullyAdded);
	}

	@Test
	public void shouldNotAddGroupAsNodeWhenAddingInteractionFromGroup() {
		Interaction i = new Interaction(GROUP_USER, USER_2, Type.LIKE);
		graph.addInteraction(i);
		assertThat(graph.getNodes(), not(hasItem(new Node(GROUP_USER))));
	}

	@Test
	public void shouldNotAddGroupAsNodeWhenAddingInteractionToGroup() {
		Interaction i = new Interaction(USER_1, GROUP_USER, Type.COMMENT);
		graph.addInteraction(i);
		assertThat(graph.getNodes(), not(hasItem(new Node(GROUP_USER))));
	}

	@Test
	public void shouldNotHaveNodesWithGroupAsNeighborWhenAddingInteractionEnvolvingTheGroup() {
		Node groupNode = new Node(GROUP_USER);
		Collection<Node> nodes = graph.getNodes();
		nodes.forEach(
				n -> assertThat(n.getOutNeighbors(), not(hasItem(groupNode))));
	}

}

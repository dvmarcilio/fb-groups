package model.graph;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.fbdata.Interaction;
import model.fbdata.User;
import model.fbdata.Interaction.Type;

public class PageRankScaledReasoningTest {

	private GroupNetworkGraph graph = new GroupNetworkGraph(123123L);

	private Node nodeA = new Node(new User(1L, "A"));
	private Node nodeB = new Node(new User(2L, "B"));
	private Node nodeC = new Node(new User(3L, "C"));
	private Node nodeD = new Node(new User(4L, "D"));
	private Node nodeE = new Node(new User(5L, "E"));
	private Node nodeF = new Node(new User(6L, "F"));
	private Node nodeG = new Node(new User(7L, "G"));
	private Node nodeH = new Node(new User(8L, "H"));

	private PageRank pageRank;

	private Map<Node, Double> nodeToPageRank;

	@Before
	public void setUp() {
		graph.addInteraction(interaction(nodeA, nodeB));
		graph.addInteraction(interaction(nodeA, nodeC));

		graph.addInteraction(interaction(nodeB, nodeD));
		graph.addInteraction(interaction(nodeB, nodeE));

		graph.addInteraction(interaction(nodeC, nodeF));
		graph.addInteraction(interaction(nodeC, nodeG));

		graph.addInteraction(interaction(nodeD, nodeA));
		graph.addInteraction(interaction(nodeD, nodeH));

		graph.addInteraction(interaction(nodeE, nodeA));
		graph.addInteraction(interaction(nodeE, nodeH));

		graph.addInteraction(interaction(nodeF, nodeG));

		graph.addInteraction(interaction(nodeG, nodeF));

		graph.addInteraction(interaction(nodeH, nodeA));

		pageRank = new PageRank(graph);
	}

	private Interaction interaction(Node from, Node to) {
		return new Interaction(from.getUser(), to.getUser(), Type.TAG);
	}

	@After
	public void assertValuesSumToOne() {
		PageRankBookExampleTest.assertValuesSumToOne(nodeToPageRank.values());
	}

	@Test
	public void shouldConvergeAllPageRankToNodeFAndNodeGWhenRepeatedlyRunning()
			throws Exception {
		nodeToPageRank = pageRank.compute(10000);
		assertEquals(new Double(0), nodeToPageRank.get(nodeA));
		assertEquals(new Double(0), nodeToPageRank.get(nodeB));
		assertEquals(new Double(0), nodeToPageRank.get(nodeC));
		assertEquals(new Double(0), nodeToPageRank.get(nodeD));
		assertEquals(new Double(0), nodeToPageRank.get(nodeE));
		assertEquals(new Double(1 / 2.0), nodeToPageRank.get(nodeF), 0.01);
		assertEquals(new Double(1 / 2.0), nodeToPageRank.get(nodeG), 0.01);
		assertEquals(new Double(0), nodeToPageRank.get(nodeH));
	}

}

package model.graph;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.fbdata.Interaction;
import model.fbdata.Interaction.Type;
import model.fbdata.User;

public class PageRankBookExampleTest {

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

	private Map<Node, Double> nodesToPageRanks;

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

		graph.addInteraction(interaction(nodeF, nodeA));

		graph.addInteraction(interaction(nodeG, nodeA));

		graph.addInteraction(interaction(nodeH, nodeA));

		pageRank = new PageRank(graph);
	}

	private Interaction interaction(Node from, Node to) {
		return new Interaction(from.getUser(), to.getUser(), Type.TAG);
	}

	@After
	public void assertValuesSumToOne() {
		PageRankBookExampleTest.assertValuesSumToOne(nodesToPageRanks.values());
	}

	@Test
	public void oneStep() {
		nodesToPageRanks = pageRank.compute(1);
		assertEquals(new Double(1 / 2.0), nodesToPageRanks.get(nodeA));
		assertEquals(new Double(1 / 16.0), nodesToPageRanks.get(nodeB));
		assertEquals(new Double(1 / 16.0), nodesToPageRanks.get(nodeC));
		assertEquals(new Double(1 / 16.0), nodesToPageRanks.get(nodeD));
		assertEquals(new Double(1 / 16.0), nodesToPageRanks.get(nodeE));
		assertEquals(new Double(1 / 16.0), nodesToPageRanks.get(nodeF));
		assertEquals(new Double(1 / 16.0), nodesToPageRanks.get(nodeG));
		assertEquals(new Double(1 / 8.0), nodesToPageRanks.get(nodeH));
	}

	@Test
	public void twoSteps() throws Exception {
		nodesToPageRanks = pageRank.compute(2);
		// XXX the book chapter incorrectly shows A with PageRank 3/16
		assertEquals(new Double(5 / 16.0), nodesToPageRanks.get(nodeA));
		assertEquals(new Double(1 / 4.0), nodesToPageRanks.get(nodeB));
		assertEquals(new Double(1 / 4.0), nodesToPageRanks.get(nodeC));
		assertEquals(new Double(1 / 32.0), nodesToPageRanks.get(nodeD));
		assertEquals(new Double(1 / 32.0), nodesToPageRanks.get(nodeE));
		assertEquals(new Double(1 / 32.0), nodesToPageRanks.get(nodeF));
		assertEquals(new Double(1 / 32.0), nodesToPageRanks.get(nodeG));
		assertEquals(new Double(1 / 16.0), nodesToPageRanks.get(nodeH));
	}

	public static void assertValuesSumToOne(Collection<Double> values) {
		Double valuesSum = values.stream().mapToDouble(v -> v.doubleValue())
				.sum();
		assertEquals(new Double(PageRank.TOTAL_PAGE_RANK), valuesSum, 0.001);
	}

}

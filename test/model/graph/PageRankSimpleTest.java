package model.graph;

import static model.graph.JSONTestFileData.DIEGO;
import static model.graph.JSONTestFileData.GROUP_ID;
import static model.graph.JSONTestFileData.GUSTAVO;
import static model.graph.JSONTestFileData.MURILLO;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.fbdata.Interaction;
import model.fbdata.Interaction.Type;

public class PageRankSimpleTest {

	private GroupNetworkGraph graph = new GroupNetworkGraph(GROUP_ID);

	private Node diegoNode = new Node(DIEGO);
	private Node murilloNode = new Node(MURILLO);
	private Node gustavoNode = new Node(GUSTAVO);

	private PageRank pageRank;

	private HashMap<Node, Double> nodesToPageRank;

	@Before
	public void setUp() {
		constructTheGraph();
		pageRank = new PageRank(graph);
	}

	private void constructTheGraph() {
		graph.addInteraction(new Interaction(DIEGO, MURILLO, Type.TAG));
		graph.addInteraction(new Interaction(DIEGO, MURILLO, Type.TAG));
		graph.addInteraction(new Interaction(DIEGO, MURILLO, Type.TAG));

		graph.addInteraction(new Interaction(DIEGO, GUSTAVO, Type.TAG));
		graph.addInteraction(new Interaction(DIEGO, GUSTAVO, Type.TAG));

		graph.addInteraction(new Interaction(MURILLO, DIEGO, Type.COMMENT));
	}

	@After
	public void assertValuesSumToOne() {
		PageRankBookExampleTest.assertValuesSumToOne(nodesToPageRank.values());
	}

	@Test
	public void oneStep() {
		nodesToPageRank = pageRank.compute(1);
		assertEquals(new Double(1 / 3.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 15.0), nodesToPageRank.get(murilloNode));
		assertEquals(new Double(7 / 15.0), nodesToPageRank.get(gustavoNode));
	}

	@Test
	public void twoSteps() {
		nodesToPageRank = pageRank.compute(2);
		assertEquals(new Double(3 / 15.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 15.0), nodesToPageRank.get(murilloNode));
		assertEquals(new Double(9 / 15.0), nodesToPageRank.get(gustavoNode));
	}

	@Test
	public void threeSteps() {
		nodesToPageRank = pageRank.compute(3);
		assertEquals(new Double(3 / 15.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 25.0), nodesToPageRank.get(murilloNode));
		assertEquals(new Double(17 / 25.0), nodesToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void fourSteps() {
		nodesToPageRank = pageRank.compute(4);
		assertEquals(new Double(3 / 25.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 25.0), nodesToPageRank.get(murilloNode));
		assertEquals(new Double(19 / 25.0), nodesToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void fiveSteps() {
		nodesToPageRank = pageRank.compute(5);
		assertEquals(new Double(3 / 25.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(9 / 125.0), nodesToPageRank.get(murilloNode),
				0.001);
		assertEquals(new Double(101 / 125.0), nodesToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void sixSteps() {
		nodesToPageRank = pageRank.compute(6);
		assertEquals(new Double(9 / 125.0), nodesToPageRank.get(diegoNode),
				0.001);
		assertEquals(new Double(9 / 125.0), nodesToPageRank.get(murilloNode),
				0.001);
		assertEquals(new Double(107 / 125.0), nodesToPageRank.get(gustavoNode),
				0.001);
	}

}

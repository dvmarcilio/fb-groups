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

	private HashMap<Node, Double> nodeToPageRank;

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
		PageRankBookExampleTest.assertValuesSumToOne(nodeToPageRank.values());
	}

	@Test
	public void oneStep() {
		nodeToPageRank = pageRank.compute(1);
		assertEquals(new Double(1 / 3.0), nodeToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 15.0), nodeToPageRank.get(murilloNode));
		assertEquals(new Double(7 / 15.0), nodeToPageRank.get(gustavoNode));
	}

	@Test
	public void twoSteps() {
		nodeToPageRank = pageRank.compute(2);
		assertEquals(new Double(3 / 15.0), nodeToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 15.0), nodeToPageRank.get(murilloNode));
		assertEquals(new Double(9 / 15.0), nodeToPageRank.get(gustavoNode));
	}

	@Test
	public void threeSteps() {
		nodeToPageRank = pageRank.compute(3);
		assertEquals(new Double(3 / 15.0), nodeToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 25.0), nodeToPageRank.get(murilloNode));
		assertEquals(new Double(17 / 25.0), nodeToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void fourSteps() {
		nodeToPageRank = pageRank.compute(4);
		assertEquals(new Double(3 / 25.0), nodeToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 25.0), nodeToPageRank.get(murilloNode));
		assertEquals(new Double(19 / 25.0), nodeToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void fiveSteps() {
		nodeToPageRank = pageRank.compute(5);
		assertEquals(new Double(3 / 25.0), nodeToPageRank.get(diegoNode));
		assertEquals(new Double(9 / 125.0), nodeToPageRank.get(murilloNode),
				0.001);
		assertEquals(new Double(101 / 125.0), nodeToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void sixSteps() {
		nodeToPageRank = pageRank.compute(6);
		assertEquals(new Double(9 / 125.0), nodeToPageRank.get(diegoNode),
				0.001);
		assertEquals(new Double(9 / 125.0), nodeToPageRank.get(murilloNode),
				0.001);
		assertEquals(new Double(107 / 125.0), nodeToPageRank.get(gustavoNode),
				0.001);
	}

}

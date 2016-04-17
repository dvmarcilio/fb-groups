package model.graph;

import static model.graph.JSONTestFileData.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import model.fbdata.Interaction;
import model.fbdata.Interaction.Type;

public class PageRankTest {

	private GroupNetworkGraph graph = new GroupNetworkGraph(GROUP_ID);

	private Node diegoNode = new Node(DIEGO);
	private Node murilloNode = new Node(MURILLO);
	private Node gustavoNode = new Node(GUSTAVO);

	private PageRank pageRank;

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

	@Test
	public void pageRank1() {
		HashMap<Node, Double> nodesToPageRank = pageRank.compute(1);
		assertEquals(new Double(1 / 3.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 15.0), nodesToPageRank.get(murilloNode));
		assertEquals(new Double(7 / 15.0), nodesToPageRank.get(gustavoNode));
	}

	@Test
	public void pageRank2() {
		HashMap<Node, Double> nodesToPageRank = pageRank.compute(2);
		assertEquals(new Double(3 / 15.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 15.0), nodesToPageRank.get(murilloNode));
		assertEquals(new Double(9 / 15.0), nodesToPageRank.get(gustavoNode));
	}

	@Test
	public void pageRank3() {
		HashMap<Node, Double> nodesToPageRank = pageRank.compute(3);
		assertEquals(new Double(3 / 15.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 25.0), nodesToPageRank.get(murilloNode));
		assertEquals(new Double(17 / 25.0), nodesToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void pageRank4() {
		HashMap<Node, Double> nodesToPageRank = pageRank.compute(4);
		assertEquals(new Double(3 / 25.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(3 / 25.0), nodesToPageRank.get(murilloNode));
		assertEquals(new Double(19 / 25.0), nodesToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void pageRank5() {
		HashMap<Node, Double> nodesToPageRank = pageRank.compute(5);
		assertEquals(new Double(3 / 25.0), nodesToPageRank.get(diegoNode));
		assertEquals(new Double(9 / 125.0), nodesToPageRank.get(murilloNode),
				0.001);
		assertEquals(new Double(101 / 125.0), nodesToPageRank.get(gustavoNode),
				0.001);
	}

	@Test
	public void pageRank6() {
		HashMap<Node, Double> nodesToPageRank = pageRank.compute(6);
		assertEquals(new Double(9 / 125.0), nodesToPageRank.get(diegoNode),
				0.001);
		assertEquals(new Double(9 / 125.0), nodesToPageRank.get(murilloNode),
				0.001);
		assertEquals(new Double(107 / 125.0), nodesToPageRank.get(gustavoNode),
				0.001);
	}
	
	@Test
	public void pageRank100() {
		HashMap<Node, Double> nodesToPageRank = pageRank.compute();
		for (Map.Entry<Node, Double> entry : nodesToPageRank.entrySet()) {
			System.out.println(entry.getKey().getUser() + "\nPageRank:"
					+ entry.getValue() + "\n");
		}
	}

}

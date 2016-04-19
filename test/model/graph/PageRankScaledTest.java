package model.graph;

import static model.graph.GraphTestHelper.NODE_A;
import static model.graph.GraphTestHelper.NODE_B;
import static model.graph.GraphTestHelper.NODE_C;
import static model.graph.GraphTestHelper.NODE_D;
import static model.graph.GraphTestHelper.NODE_E;
import static model.graph.GraphTestHelper.NODE_F;
import static model.graph.GraphTestHelper.NODE_G;
import static model.graph.GraphTestHelper.NODE_H;
import static model.graph.GraphTestHelper.NON_SCALED_EXAMPLE_GRAPH;
import static model.graph.GraphTestHelper.SCALED_EXAMPLE_GRAPH;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Test;

/**
 * All the Scaled Tests assume that the scaling factor is 0.85
 *
 */
public class PageRankScaledTest {

	private static final int STEPS = 10000;

	private Map<Node, Double> nodeToPageRank;

	@After
	public void assertValuesSumToOne() {
		PageRankBookExampleTest.assertValuesSumToOne(nodeToPageRank.values());
	}

	@Test
	public void shouldConvergeAllPageRankToNodeFAndNodeGWhenRepeatedlyRunning() {
		PageRank pageRank = new PageRank(SCALED_EXAMPLE_GRAPH);

		nodeToPageRank = pageRank.compute(STEPS);

		assertEquals(new Double(0), nodeToPageRank.get(NODE_A));
		assertEquals(new Double(0), nodeToPageRank.get(NODE_B));
		assertEquals(new Double(0), nodeToPageRank.get(NODE_C));
		assertEquals(new Double(0), nodeToPageRank.get(NODE_D));
		assertEquals(new Double(0), nodeToPageRank.get(NODE_E));
		assertEquals(new Double(1 / 2.0), nodeToPageRank.get(NODE_F), 0.01);
		assertEquals(new Double(1 / 2.0), nodeToPageRank.get(NODE_G), 0.01);
		assertEquals(new Double(0), nodeToPageRank.get(NODE_H));
	}

	@Test
	public void shouldNotConvergeAllPageRankToNodeFAndNodeGWithScaledPageRank() {
		PageRank pageRank = new PageRank(SCALED_EXAMPLE_GRAPH);

		nodeToPageRank = pageRank.computeScaled(STEPS);

		assertNotEquals(new Double(0), nodeToPageRank.get(NODE_A));
		assertNotEquals(new Double(0), nodeToPageRank.get(NODE_B));
		assertNotEquals(new Double(0), nodeToPageRank.get(NODE_C));
		assertNotEquals(new Double(0), nodeToPageRank.get(NODE_D));
		assertNotEquals(new Double(0), nodeToPageRank.get(NODE_E));
		assertNotEquals(new Double(0), nodeToPageRank.get(NODE_H));
	}

	@Test
	public void oneStepScaledOnNonScaledExampleGraph() {
		PageRank pageRank = new PageRank(NON_SCALED_EXAMPLE_GRAPH);

		nodeToPageRank = pageRank.computeScaled(1);

		assertEquals(new Double(0.44375), nodeToPageRank.get(NODE_A), 0.01);
		assertEquals(new Double(0.071875), nodeToPageRank.get(NODE_B), 0.01);
		assertEquals(new Double(0.071875), nodeToPageRank.get(NODE_C), 0.01);
		assertEquals(new Double(0.071875), nodeToPageRank.get(NODE_D), 0.01);
		assertEquals(new Double(0.071875), nodeToPageRank.get(NODE_E), 0.01);
		assertEquals(new Double(0.071875), nodeToPageRank.get(NODE_F), 0.01);
		assertEquals(new Double(0.071875), nodeToPageRank.get(NODE_G), 0.01);
		assertEquals(new Double(0.125), nodeToPageRank.get(NODE_H), 0.01);
	}

	@Test
	public void twoStepsScaledOnNonScaledExampleGraph() {
		PageRank pageRank = new PageRank(NON_SCALED_EXAMPLE_GRAPH);

		nodeToPageRank = pageRank.computeScaled(2);

		assertEquals(new Double(0.30828125), nodeToPageRank.get(NODE_A), 0.01);
		assertEquals(new Double(0.20734375), nodeToPageRank.get(NODE_B), 0.01);
		assertEquals(new Double(0.20734375), nodeToPageRank.get(NODE_C), 0.01);
		assertEquals(new Double(0.049296875), nodeToPageRank.get(NODE_D), 0.01);
		assertEquals(new Double(0.049296875), nodeToPageRank.get(NODE_E), 0.01);
		assertEquals(new Double(0.049296875), nodeToPageRank.get(NODE_F), 0.01);
		assertEquals(new Double(0.049296875), nodeToPageRank.get(NODE_G), 0.01);
		assertEquals(new Double(0.07984375), nodeToPageRank.get(NODE_H), 0.01);
	}

	@Test
	public void threeStepsScaledOnNonScaledExampleGraph() {
		PageRank pageRank = new PageRank(NON_SCALED_EXAMPLE_GRAPH);

		nodeToPageRank = pageRank.computeScaled(3);

		assertEquals(new Double(0.21232421875), nodeToPageRank.get(NODE_A), 0.01);
		assertEquals(new Double(0.14976953125), nodeToPageRank.get(NODE_B), 0.01);
		assertEquals(new Double(0.14976953125), nodeToPageRank.get(NODE_C), 0.01);
		assertEquals(new Double(0.10687109375), nodeToPageRank.get(NODE_D), 0.01);
		assertEquals(new Double(0.10687109375), nodeToPageRank.get(NODE_E), 0.01);
		assertEquals(new Double(0.10687109375), nodeToPageRank.get(NODE_F), 0.01);
		assertEquals(new Double(0.10687109375), nodeToPageRank.get(NODE_G), 0.01);
		assertEquals(new Double(0.06065234375), nodeToPageRank.get(NODE_H), 0.01);
	}

}

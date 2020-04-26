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
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

public class PageRankBookExampleTest {

	private GroupNetworkGraph graph = NON_SCALED_EXAMPLE_GRAPH;

	private PageRank pageRank = new PageRank(graph);

	private Map<Node, Double> nodeToPageRank;

	@After
	public void assertValuesSumToOne() {
		PageRankBookExampleTest.assertValuesSumToOne(nodeToPageRank.values());
	}

	@Test
	public void oneStep() {
		nodeToPageRank = pageRank.compute(1);
		assertEquals(new Double(1 / 2.0), nodeToPageRank.get(NODE_A));
		assertEquals(new Double(1 / 16.0), nodeToPageRank.get(NODE_B));
		assertEquals(new Double(1 / 16.0), nodeToPageRank.get(NODE_C));
		assertEquals(new Double(1 / 16.0), nodeToPageRank.get(NODE_D));
		assertEquals(new Double(1 / 16.0), nodeToPageRank.get(NODE_E));
		assertEquals(new Double(1 / 16.0), nodeToPageRank.get(NODE_F));
		assertEquals(new Double(1 / 16.0), nodeToPageRank.get(NODE_G));
		assertEquals(new Double(1 / 8.0), nodeToPageRank.get(NODE_H));
	}

	@Test
	public void twoSteps() throws Exception {
		nodeToPageRank = pageRank.compute(2);
		// XXX the book chapter incorrectly shows A with PageRank 3/16
		assertEquals(new Double(5 / 16.0), nodeToPageRank.get(NODE_A));
		assertEquals(new Double(1 / 4.0), nodeToPageRank.get(NODE_B));
		assertEquals(new Double(1 / 4.0), nodeToPageRank.get(NODE_C));
		assertEquals(new Double(1 / 32.0), nodeToPageRank.get(NODE_D));
		assertEquals(new Double(1 / 32.0), nodeToPageRank.get(NODE_E));
		assertEquals(new Double(1 / 32.0), nodeToPageRank.get(NODE_F));
		assertEquals(new Double(1 / 32.0), nodeToPageRank.get(NODE_G));
		assertEquals(new Double(1 / 16.0), nodeToPageRank.get(NODE_H));
	}

	public static void assertValuesSumToOne(Collection<Double> values) {
		Double valuesSum = values.stream().mapToDouble(v -> v.doubleValue())
				.sum();
		assertEquals(new Double(PageRank.TOTAL_PAGE_RANK), valuesSum, 0.001);
	}

}

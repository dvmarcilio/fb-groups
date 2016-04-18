package model.graph;

import java.util.Collection;
import java.util.HashMap;

public class PageRank {

	private static final int STEPS = 5;

	public static final Double TOTAL_PAGE_RANK = 1.0;

	private GroupNetworkGraph graph;

	private HashMap<Node, Double> nodeToPageRank = new HashMap<>();

	private HashMap<Node, Double> nodeToZeroValue = new HashMap<>();

	public PageRank(GroupNetworkGraph graph) {
		this.graph = graph;
		initPageRankNodes();
	}

	private void initPageRankNodes() {
		Collection<Node> nodes = graph.getNodes();
		Double initialPageRankValue = TOTAL_PAGE_RANK / nodes.size();
		for (Node node : nodes) {
			nodeToPageRank.put(node, initialPageRankValue);
			nodeToZeroValue.put(node, 0.0);
		}
	}

	public HashMap<Node, Double> compute() {
		return compute(STEPS);
	}

	public HashMap<Node, Double> compute(int steps) {
		for (int i = 0; i < steps; i++) {
			basicPageRankUpdate();
		}
		return nodeToPageRank;
	}

	private void basicPageRankUpdate() {
		HashMap<Node, Double> iterationNodeToPageRank = new HashMap<>(
				nodeToZeroValue);

		for (Node node : iterationNodeToPageRank.keySet()) {

			passPageRankSharesToNeighbors(iterationNodeToPageRank, node);

			passAllCurrentPageRankToItselfIfNoNeighbors(iterationNodeToPageRank,
					node);

		}

		nodeToPageRank = iterationNodeToPageRank;
	}

	private double getCurrentNodePageRank(Node node) {
		return nodeToPageRank.get(node);
	}

	private void passPageRankSharesToNeighbors(
			HashMap<Node, Double> iterationNodeToPageRank, Node node) {
		double nodePageRankSingleShare = getCurrentNodePageRank(node)
				/ node.getOutDegree();
		for (Node neighbor : node.getNeighbors()) {
			passPageRankShareToNeighbor(iterationNodeToPageRank, node,
					nodePageRankSingleShare, neighbor);
		}
	}

	private void passPageRankShareToNeighbor(
			HashMap<Node, Double> iterationNodeToPageRank, Node node,
			double nodePageRankSingleShare, Node neighbor) {
		double neighborNewPageRank = figureNeighborNewPageRank(
				iterationNodeToPageRank, node, nodePageRankSingleShare,
				neighbor);
		iterationNodeToPageRank.put(neighbor, neighborNewPageRank);
	}

	private double figureNeighborNewPageRank(
			HashMap<Node, Double> iterationNodeToPageRank, Node node,
			double currNodePageRankShare, Node neighbor) {
		double neighborPageRankShare = figureSharerBasedOnHowManyEdgesToNeighbor(
				node, currNodePageRankShare, neighbor);
		double neighborPageRank = iterationNodeToPageRank.get(neighbor);
		return neighborPageRank + neighborPageRankShare;
	}

	private double figureSharerBasedOnHowManyEdgesToNeighbor(Node node,
			double currNodePageRankShare, Node neighbor) {
		int totalEdgesToNeighbor = retrieveTotalOutEdgesToNeighbor(node,
				neighbor);
		return currNodePageRankShare * totalEdgesToNeighbor;
	}

	private int retrieveTotalOutEdgesToNeighbor(Node node, Node neighbor) {
		Interactions neighborInteractions = node
				.getInteractionsWith(neighbor.getUser());
		return neighborInteractions.getTotal();
	}

	private void passAllCurrentPageRankToItselfIfNoNeighbors(
			HashMap<Node, Double> iterationNodeToPageRank, Node node) {
		if (node.getOutDegree() == 0) {
			double nodeIterationPageRank = iterationNodeToPageRank.get(node);
			double nodeNewPageRank = nodeIterationPageRank
					+ getCurrentNodePageRank(node);
			iterationNodeToPageRank.put(node, nodeNewPageRank);
		}
	}

}

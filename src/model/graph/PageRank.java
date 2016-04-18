package model.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PageRank {

	public static final Double TOTAL_PAGE_RANK = 1.0;

	public static final Double SCALING_FACTOR = 0.85;

	private static final int STEPS = 100;

	private GroupNetworkGraph graph;

	private HashMap<Node, Double> nodeToPageRank = new HashMap<>();

	private HashMap<Node, Double> nodeToZeroValue = new HashMap<>();

	private Double scaledShare;

	public PageRank(GroupNetworkGraph graph) {
		this.graph = graph;
		initPageRankNodes();
		computeScaledShare();
	}

	private void initPageRankNodes() {
		Collection<Node> nodes = graph.getNodes();
		Double initialPageRankValue = TOTAL_PAGE_RANK / nodes.size();
		for (Node node : nodes) {
			nodeToPageRank.put(node, initialPageRankValue);
			nodeToZeroValue.put(node, 0.0);
		}
	}

	private void computeScaledShare() {
		double residualPageRankFactor = TOTAL_PAGE_RANK - SCALING_FACTOR;
		int numNodes = graph.getNumNodes();
		scaledShare = residualPageRankFactor / numNodes;
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
		BasicPageRankUpdate basicPageRankUpdate = new BasicPageRankUpdate();

		for (Node node : graph.getNodes()) {
			basicPageRankUpdate.execute(node);
		}

		nodeToPageRank = basicPageRankUpdate.getIterationNodeToPageRank();
	}

	private double getNodePageRank(Node node) {
		return nodeToPageRank.get(node);
	}

	public Map<Node, Double> computeScaled() {
		return computeScaled(STEPS);
	}

	public Map<Node, Double> computeScaled(int steps) {
		BasicPageRankUpdate basicPageRankUpdate = new BasicPageRankUpdate();
		for (int i = 0; i < steps; i++) {
			for (Node node : graph.getNodes()) {
				basicPageRankUpdate.execute(node);
				scaleNode(node, basicPageRankUpdate);
			}
		}
		return basicPageRankUpdate.getIterationNodeToPageRank();
	}

	private void scaleNode(Node node, BasicPageRankUpdate basicPageRankUpdate) {
		double nodePageRankScaledDown = basicPageRankUpdate
				.getIterationNodePageRank(node) * SCALING_FACTOR;
		double finalNodePageRank = nodePageRankScaledDown + scaledShare;
		basicPageRankUpdate.updateNodePageRank(node, finalNodePageRank);
	}

	private class BasicPageRankUpdate {

		private HashMap<Node, Double> iterationNodeToPageRank = new HashMap<>(
				nodeToZeroValue);

		private Node node;

		private HashMap<Node, Double> getIterationNodeToPageRank() {
			return iterationNodeToPageRank;
		}

		private Double getIterationNodePageRank(Node node) {
			return iterationNodeToPageRank.get(node);
		}

		private void updateNodePageRank(Node node, double value) {
			iterationNodeToPageRank.put(node, value);
		}

		private void execute(Node node) {
			this.node = node;
			passPageRankSharesToNeighbors();
			passAllCurrentPageRankToItselfIfNoNeighbors();
		}

		private void passPageRankSharesToNeighbors() {
			double nodePageRankSingleShare = getNodePageRank(node)
					/ node.getOutDegree();
			for (Node neighbor : node.getNeighbors()) {
				passPageRankShareToNeighbor(nodePageRankSingleShare, neighbor);
			}
		}

		private void passPageRankShareToNeighbor(double nodePageRankSingleShare,
				Node neighbor) {
			double neighborNewPageRank = figureNeighborNewPageRank(
					nodePageRankSingleShare, neighbor);
			iterationNodeToPageRank.put(neighbor, neighborNewPageRank);
		}

		private double figureNeighborNewPageRank(double currNodePageRankShare,
				Node neighbor) {
			double neighborPageRankShare = figureSharerBasedOnHowManyEdgesToNeighbor(
					currNodePageRankShare, neighbor);
			double neighborPageRank = iterationNodeToPageRank.get(neighbor);
			return neighborPageRank + neighborPageRankShare;
		}

		private double figureSharerBasedOnHowManyEdgesToNeighbor(
				double currNodePageRankShare, Node neighbor) {
			int totalEdgesToNeighbor = retrieveTotalOutEdgesToNeighbor(
					neighbor);
			return currNodePageRankShare * totalEdgesToNeighbor;
		}

		private int retrieveTotalOutEdgesToNeighbor(Node neighbor) {
			Interactions neighborInteractions = node
					.getInteractionsWith(neighbor.getUser());
			return neighborInteractions.getTotal();
		}

		private void passAllCurrentPageRankToItselfIfNoNeighbors() {
			if (node.getOutDegree() == 0) {
				double nodeIterationPageRank = iterationNodeToPageRank
						.get(node);
				double nodeNewPageRank = nodeIterationPageRank
						+ getNodePageRank(node);
				iterationNodeToPageRank.put(node, nodeNewPageRank);
			}
		}

	}

}

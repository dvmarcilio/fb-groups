package model.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PageRank {

	public static final Double TOTAL_PAGE_RANK = 1.0;

	public static final Double DEFAULT_SCALING_FACTOR = 0.85;

	private static final int STEPS = 100;

	private final GroupNetworkGraph graph;

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
		BasicPageRankUpdate basicPageRankUpdate = new BasicPageRankUpdate();

		graph.getNodes().forEach(n -> basicPageRankUpdate.execute(n));

		nodeToPageRank = basicPageRankUpdate.getIterationNodeToPageRank();
	}

	private double getNodePageRank(Node node) {
		return nodeToPageRank.get(node);
	}

	/**
	 * Run for STEPS iterations with scaling factor DEFAULT_SCALING_FACTOR.
	 * 
	 * @return
	 */
	public Map<Node, Double> computeScaled() {
		return computeScaled(STEPS);
	}

	/**
	 * Run for steps iterations with scaling factor DEFAULT_SCALING_FACTOR.
	 * 
	 * @param steps
	 * @return
	 */
	public Map<Node, Double> computeScaled(int steps) {
		return computeScaled(steps, DEFAULT_SCALING_FACTOR);
	}

	public Map<Node, Double> computeScaled(int steps, double scalingFactor) {
		for (int i = 0; i < steps; i++) {
			ScaledPageRankUpdate scaledPageRankUpdate = new ScaledPageRankUpdate(
					scalingFactor);
			nodeToPageRank = scaledPageRankUpdate.execute();
		}
		return nodeToPageRank;
	}

	private class BasicPageRankUpdate {

		private HashMap<Node, Double> iterationNodeToPageRank = new HashMap<>(
				nodeToZeroValue);

		private Node node;

		private HashMap<Node, Double> getIterationNodeToPageRank() {
			return iterationNodeToPageRank;
		}

		private void execute(Node node) {
			this.node = node;
			passPageRankSharesToNeighbors();
			passAllCurrentPageRankToItselfIfNoNeighbors();
		}

		private void passPageRankSharesToNeighbors() {
			double nodePageRankSingleShare = getNodePageRank(node)
					/ node.getOutDegree();
			for (Node neighbor : node.getOutNeighbors()) {
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

	private class ScaledPageRankUpdate {

		private double scalingFactor;

		private double scaledShare;

		private BasicPageRankUpdate basicPageRankUpdate = new BasicPageRankUpdate();

		private HashMap<Node, Double> scaledNodeToPageRank = new HashMap<>();

		public ScaledPageRankUpdate(double scalingFactor) {
			this.scalingFactor = scalingFactor;
			computeScaledShare();
		}

		private void computeScaledShare() {
			double residualPageRankFactor = TOTAL_PAGE_RANK - scalingFactor;
			int numNodes = graph.getNumNodes();
			scaledShare = residualPageRankFactor / numNodes;
		}

		private HashMap<Node, Double> execute() {
			executeBasicUpdateOnAllNodes();
			scaleNodesPageRank();
			return scaledNodeToPageRank;
		}

		private void executeBasicUpdateOnAllNodes() {
			graph.getNodes().forEach(n -> basicPageRankUpdate.execute(n));
		}

		private void scaleNodesPageRank() {
			basicPageRankUpdate.iterationNodeToPageRank.keySet()
					.forEach(n -> scaleNode(n));
		}

		private void scaleNode(Node node) {
			double nodePageRankScaledDown = basicPageRankUpdate
					.getIterationNodeToPageRank().get(node) * scalingFactor;
			double finalNodePageRank = nodePageRankScaledDown + scaledShare;
			scaledNodeToPageRank.put(node, finalNodePageRank);
		}

	}

}

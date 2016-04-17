package model.graph;

import java.util.Collection;
import java.util.HashMap;

public class PageRank {

	private static final int STEPS = 5;

	public static final Double TOTAL_PAGE_RANK = 1.0;

	private GroupNetworkGraph graph;

	private HashMap<Node, Double> nodeToPageRank = new HashMap<>();

	private HashMap<Node, Double> nodeToPageRankPreIteration = new HashMap<>();

	public PageRank(GroupNetworkGraph graph) {
		super();
		this.graph = graph;
		initPageRankNodes();
	}

	private void initPageRankNodes() {
		Collection<Node> nodes = graph.getNodes();
		Double initialPageRankValue = TOTAL_PAGE_RANK / nodes.size();
		for (Node node : nodes) {
			nodeToPageRank.put(node, initialPageRankValue);
			nodeToPageRankPreIteration.put(node, 0.0);
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
		HashMap<Node, Double> preIterationNodeToPageRank = new HashMap<>(
				nodeToPageRank);
		HashMap<Node, Double> iterationNodeToPageRank = new HashMap<>(
				nodeToPageRankPreIteration);

		for (Node node : iterationNodeToPageRank.keySet()) {
			Double currentNodePageRank = preIterationNodeToPageRank.get(node);
			Double currNodePageRankShare = currentNodePageRank
					/ node.getOutDegree();

			for (Node neighbor : node.getNeighbors()) {
				Interactions neighborInteractions = node
						.getInteractionsWith(neighbor.getUser());
				int totalEdgesToNeighbor = neighborInteractions.getTotal();

				Double neighborPageRank = iterationNodeToPageRank.get(neighbor);
				Double neighborPageRankShare = currNodePageRankShare
						* totalEdgesToNeighbor;
				Double neighborNewPageRank = neighborPageRank
						+ neighborPageRankShare;

				iterationNodeToPageRank.put(neighbor, neighborNewPageRank);
			}

			if (node.getNeighbors().isEmpty()) {
				Double nodeIterationPageRank = iterationNodeToPageRank
						.get(node);
				Double nodeNewPageRank = nodeIterationPageRank
						+ currentNodePageRank;
				iterationNodeToPageRank.put(node, nodeNewPageRank);
			}

		}

		nodeToPageRank = iterationNodeToPageRank;
	}

}

package model.graph;

import java.util.Collection;
import java.util.HashMap;

public class PageRank {

	private static final int STEPS = 5;

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
		Double initialPageRankValue = 1.0 / nodes.size();
		for (Node node : nodes) {
			nodeToPageRank.put(node, initialPageRankValue);
			nodeToPageRankPreIteration.put(node, 0.0);
		}
	}

	public HashMap<Node, Double> compute() {
		for (int i = 0; i < STEPS; i++) {
			basicPageRankUpdate();
		}
		return nodeToPageRank;
	}

	private void basicPageRankUpdate() {
		HashMap<Node, Double> preIterationNodeToPageRank = new HashMap<>(nodeToPageRank);
		HashMap<Node, Double> iterationNodeToPageRank = new HashMap<>(
				nodeToPageRankPreIteration);

		for (Node node : iterationNodeToPageRank.keySet()) {
			Double currentNodePageRank = preIterationNodeToPageRank.get(node);
			Double currNodePageRankShare = currentNodePageRank
					/ node.getOutDegree();
			
			for (Node neighbor : node.getNeighbors()) {
				int totalEdgesToNeighbor = node
						.getInteractionsWith(node.getUser()).getTotal();
				Double neighborPageRank = preIterationNodeToPageRank.get(neighbor);
				Double neighborPageRankShare = currNodePageRankShare
						* totalEdgesToNeighbor;
				Double neighborNewPageRank = neighborPageRank
						+ neighborPageRankShare;
				iterationNodeToPageRank.put(neighbor, neighborNewPageRank);
			}
			
			if (!node.getNeighbors().isEmpty()) {
				preIterationNodeToPageRank.put(node, 0D);
			}
		}

		nodeToPageRank = iterationNodeToPageRank;
	}

}

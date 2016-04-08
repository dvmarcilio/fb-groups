package model.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FunWithBasicStats {

	private GroupNetworkGraph graph;

	public FunWithBasicStats(GroupNetworkGraph graph) {
		super();
		this.graph = graph;
	}

	public void show() {
		showGraphNodeCountAndEdgeCount();
		showPopularPeople();
		showStuffWithZeroDegree();
	}

	private void showGraphNodeCountAndEdgeCount() {
		System.out.println("Edges count: " + graph.getNumEdges());
		System.out.println("Nodes count: " + graph.getNumNodes());
	}

	private void showPopularPeople() {
		System.out
				.println("Largest In Degree: " + getNodeWithLargestInDegree());
		System.out.println(
				"Largest Out Degree: " + getNodeWithLargestOutDegree());
	}

	private Node getNodeWithLargestInDegree() {
		return graph.getNodes().stream()
				.max(Comparator.comparingInt(n -> n.getInDegree())).get();
	}

	private List<Node> getAllZeroInDegree() {
		return graph.getNodes().stream()
				.filter(n -> ((Integer) n.getInDegree()).equals(0))
				.collect(Collectors.toList());
	}

	private Node getNodeWithLargestOutDegree() {
		return graph.getNodes().stream()
				.max(Comparator.comparingInt(n -> n.getOutDegree())).get();
	}

	private List<Node> getAllZeroOutDegree() {
		return graph.getNodes().stream()
				.filter(n -> ((Integer) n.getOutDegree()).equals(0))
				.collect(Collectors.toList());
	}

	private void showStuffWithZeroDegree() {
		final List<Node> allZeroInDegree = getAllZeroInDegree();
		showAllZeroInDegree(allZeroInDegree);

		final List<Node> allZeroOutDegree = getAllZeroOutDegree();
		showAllZeroOutDegree(allZeroOutDegree);

		showAllZeroActivity(allZeroInDegree, allZeroOutDegree);
	}

	private void showAllZeroInDegree(final List<Node> allZeroInDegree) {
		System.out.println("Zero In Degree Count: " + allZeroInDegree.size());
		System.out.println("All zero In Degree: " + allZeroInDegree);
	}

	private void showAllZeroOutDegree(final List<Node> allZeroOutDegree) {
		System.out.println("Zero Out Degree Count: " + allZeroOutDegree.size());
		System.out.println("All zero Out Degree: " + allZeroOutDegree);
	}

	private void showAllZeroActivity(final List<Node> allZeroInDegree,
			final List<Node> allZeroOutDegree) {
		List<Node> allZeroInAndOutDegree = new ArrayList<>(allZeroInDegree);
		allZeroInAndOutDegree.retainAll(allZeroOutDegree);
		System.out.println(
				"Zero activity count: " + allZeroInAndOutDegree.size());
		System.out.println("All zero activity: " + allZeroInAndOutDegree);
	}

}

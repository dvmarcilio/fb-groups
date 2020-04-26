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
		System.out.println("\nTop 10 inDegree");
		getTop10LargestInDegree().forEach(n -> System.out.println(n));

		System.out.println("\nTop 10 outDegree");
		getTop10LargestOutDegree().forEach(n -> System.out.println(n));
	}

	private List<Node> getTop10LargestInDegree() {
		return graph.getNodes().stream()
				.sorted(Comparator.comparingInt(Node::getInDegree).reversed())
				.limit(10).collect(Collectors.toList());
	}

	private List<Node> getTop10LargestOutDegree() {
		return graph.getNodes().stream()
				.sorted(Comparator.comparingInt(Node::getOutDegree).reversed())
				.limit(10).collect(Collectors.toList());
	}

	private List<Node> getAllZeroInDegree() {
		return graph.getNodes().stream()
				.filter(n -> ((Integer) n.getInDegree()).equals(0))
				.collect(Collectors.toList());
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
		System.out.println("\nZero In Degree Count: " + allZeroInDegree.size());
		System.out.println("All zero In Degree: " + allZeroInDegree);
	}

	private void showAllZeroOutDegree(final List<Node> allZeroOutDegree) {
		System.out
				.println("\nZero Out Degree Count: " + allZeroOutDegree.size());
		System.out.println("All zero Out Degree: " + allZeroOutDegree);
	}

	private void showAllZeroActivity(final List<Node> allZeroInDegree,
			final List<Node> allZeroOutDegree) {
		List<Node> allZeroInAndOutDegree = new ArrayList<>(allZeroInDegree);
		allZeroInAndOutDegree.retainAll(allZeroOutDegree);
		System.out.println(
				"\nZero activity count: " + allZeroInAndOutDegree.size());
		System.out.println("All zero activity: " + allZeroInAndOutDegree);
	}

}

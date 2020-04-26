package data;

import model.fbdata.Interaction;
import model.fbdata.Interaction.Type;
import model.fbdata.User;
import model.graph.GroupNetworkGraph;

public class BookGraphExampleData {

	public static final User USER_A = new User(1L, "A");
	public static final User USER_B = new User(2L, "B");
	public static final User USER_C = new User(3L, "C");
	public static final User USER_D = new User(4L, "D");
	public static final User USER_E = new User(5L, "E");
	public static final User USER_F = new User(6L, "F");
	public static final User USER_G = new User(7L, "G");
	public static final User USER_H = new User(8L, "H");

	public static GroupNetworkGraph nonScaledExampleGraph() {
		GroupNetworkGraph graph = new GroupNetworkGraph(123123L);
		constructCommonGraph(graph);

		graph.addInteraction(interaction(USER_F, USER_A));

		graph.addInteraction(interaction(USER_G, USER_A));

		return graph;
	}

	private static void constructCommonGraph(GroupNetworkGraph graph) {
		graph.addInteraction(interaction(USER_A, USER_B));
		graph.addInteraction(interaction(USER_A, USER_C));

		graph.addInteraction(interaction(USER_B, USER_D));
		graph.addInteraction(interaction(USER_B, USER_E));

		graph.addInteraction(interaction(USER_C, USER_F));
		graph.addInteraction(interaction(USER_C, USER_G));

		graph.addInteraction(interaction(USER_D, USER_A));
		graph.addInteraction(interaction(USER_D, USER_H));

		graph.addInteraction(interaction(USER_E, USER_A));
		graph.addInteraction(interaction(USER_E, USER_H));

		graph.addInteraction(interaction(USER_H, USER_A));
	}

	private static Interaction interaction(User from, User to) {
		return new Interaction(from, to, Type.TAG);
	}

	public static GroupNetworkGraph scaledExampleGraph() {
		GroupNetworkGraph graph = new GroupNetworkGraph(123123L);
		constructCommonGraph(graph);

		graph.addInteraction(interaction(USER_F, USER_G));

		graph.addInteraction(interaction(USER_G, USER_F));

		return graph;
	}

}

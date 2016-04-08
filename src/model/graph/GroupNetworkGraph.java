package model.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import model.fbdata.Interaction;
import model.fbdata.User;

public class GroupNetworkGraph {

	private HashMap<User, Node> nodes = new HashMap<>();

	private Integer numEdges = 0;

	public void addNode(User user) {
		nodes.put(user, new Node(user));
	}

	public void addInteraction(Interaction interaction) {
		Node nodeFrom = nodes.get(interaction.getFrom());
		Node nodeTo = nodes.get(interaction.getTo());

		// FIXME Apparently my User export didn't get everyone.
		if (nodeFrom == null) {
			addNode(interaction.getFrom());
			nodeFrom = nodes.get(interaction.getFrom());
		}
		if (nodeTo == null) {
			addNode(interaction.getTo());
			nodeTo = nodes.get(interaction.getTo());
		}

		nodeFrom.addInteractionWith(nodeTo, interaction.getType());
		numEdges += 1;
	}

	public Set<User> getUsers() {
		return nodes.keySet();
	}

	public Collection<Node> getNodes() {
		return nodes.values();
	}

	public Integer getNumEdges() {
		return numEdges;
	}

	public Integer getNumNodes() {
		return nodes.size();
	}

}

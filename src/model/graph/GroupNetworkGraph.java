package model.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import model.fbdata.Interaction;
import model.fbdata.User;

public class GroupNetworkGraph {

	private HashMap<User, Node> nodes = new HashMap<>();

	private Integer numEdges = 0;

	/**
	 * @return <tt>true</tt> if the node was added successfully, <tt>false</tt>
	 *         otherwise
	 */
	public boolean addNode(User user) {
		if (!nodes.containsKey(user)) {
			nodes.put(user, new Node(user));
			return true;
		}
		return false;
	}

	/**
	 * Does not add self-interactions.
	 * 
	 * @return <tt>true</tt> if the interaction was added successfully,
	 *         <tt>false</tt> otherwise.
	 */
	public boolean addInteraction(Interaction interaction) {
		if (!interaction.isSelfInteraction()) {
			doAddInteraction(interaction);
			return true;
		}
		return false;
	}

	private void doAddInteraction(Interaction interaction) {
		Node nodeFrom = retrieveUserNode(interaction.getFrom());
		Node nodeTo = retrieveUserNode(interaction.getTo());
		nodeFrom.addInteractionWith(nodeTo, interaction.getType());
		numEdges += 1;
	}

	private Node retrieveUserNode(User user) {
		if (!nodes.containsKey(user)) {
			addNode(user);
		}
		return nodes.get(user);
	}

	public Set<User> getUsers() {
		return nodes.keySet();
	}

	public Collection<Node> getNodes() {
		return nodes.values();
	}

	public boolean hasNodeForUser(User user) {
		return nodes.containsKey(user);
	}

	public Node getUserNode(User user) {
		return nodes.get(user);
	}

	public Integer getNumEdges() {
		return numEdges;
	}

	public Integer getNumNodes() {
		return nodes.size();
	}

}

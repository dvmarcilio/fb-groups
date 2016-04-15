package model.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import model.fbdata.Interaction;
import model.fbdata.User;

public class GroupNetworkGraph {

	private HashMap<User, Node> nodes = new HashMap<>();

	private Integer numEdges = 0;

	private Long groupId;

	public GroupNetworkGraph(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return <tt>true</tt> if the node was added successfully, <tt>false</tt>
	 *         otherwise
	 */
	public boolean addNode(User user) {
		if (canAddNode(user)) {
			nodes.put(user, new Node(user));
			return true;
		}
		return false;
	}

	private boolean canAddNode(User user) {
		return !nodes.containsKey(user) && !isGroupUser(user);
	}

	private boolean isGroupUser(User user) {
		return user.getId().equals(groupId);
	}

	/**
	 * Does not add self-interactions.
	 * 
	 * @return <tt>true</tt> if the interaction was added successfully,
	 *         <tt>false</tt> otherwise.
	 */
	public boolean addInteraction(Interaction interaction) {
		if (canAddInteraction(interaction)) {
			doAddInteraction(interaction);
			return true;
		}
		return false;
	}
	
	private boolean canAddInteraction(Interaction interaction) {
		User groupUser = new User(groupId, "");
		return !interaction.isSelfInteraction() && !interaction.envolvesUser(groupUser);
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

	public Long getGroupId() {
		return groupId;
	}

}

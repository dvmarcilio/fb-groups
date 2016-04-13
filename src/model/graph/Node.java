package model.graph;

import java.util.HashMap;
import java.util.Set;

import model.fbdata.Interaction;
import model.fbdata.User;

public class Node {

	private User user;

	private Integer inDegree = 0;

	private Integer outDegree = 0;

	private HashMap<Node, Interactions> neighborsInteractedWith = new HashMap<>();

	protected Node(User user) {
		this.user = user;
	}

	protected void addInteractionWith(Node interactedNode,
			Interaction.Type interactionType) {
		addInteraction(interactedNode, interactionType);
		outDegree += 1;
		interactedNode.inDegree += 1;
	}

	private void addInteraction(Node interactedNode,
			Interaction.Type interactionType) {
		if (!neighborsInteractedWith.containsKey(interactedNode))
			addInteractionWithNewNeighbor(interactedNode, interactionType);
		else
			addInteractionToExistentNeighbor(interactedNode, interactionType);
	}

	private void addInteractionWithNewNeighbor(Node interactedNode,
			Interaction.Type interactionType) {
		Interactions interactions = new Interactions();
		interactions.add(interactionType);
		neighborsInteractedWith.put(interactedNode, interactions);
	}

	private void addInteractionToExistentNeighbor(Node interactedNode,
			Interaction.Type interactionType) {
		Interactions interactions = neighborsInteractedWith.get(interactedNode);
		interactions.add(interactionType);
	}

	public Set<Node> getNeighbors() {
		return neighborsInteractedWith.keySet();
	}

	public Integer getInDegree() {
		return inDegree;
	}

	public Integer getOutDegree() {
		return outDegree;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [user=" + user + ", inDegree=" + inDegree + ", outDegree="
				+ outDegree;
	}

}

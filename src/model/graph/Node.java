package model.graph;

import java.util.HashMap;
import java.util.Set;

import model.fbdata.Interaction;
import model.fbdata.User;

public class Node {

	private User user;

	private int inDegree = 0;

	private int outDegree = 0;

	private HashMap<Node, Interactions> neighborsInteractedWith = new HashMap<>();

	public Node(User user) {
		this.user = user;
	}

	public void addInteractionWith(Node interactedNode,
			Interaction.Type interactionType) {
		if (!neighborsInteractedWith.containsKey(interactedNode)) {
			Interactions interactions = new Interactions();
			interactions.add(interactionType);
			neighborsInteractedWith.put(interactedNode, interactions);
		} else {
			Interactions interactions = neighborsInteractedWith
					.get(interactedNode);
			interactions.add(interactionType);
		}

		outDegree += 1;
		interactedNode.interactedWith();
	}

	public Set<Node> getNeighbors() {
		return neighborsInteractedWith.keySet();
	}

	private void interactedWith() {
		inDegree += 1;
	}

	public int getInDegree() {
		return inDegree;
	}

	public int getOutDegree() {
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

}

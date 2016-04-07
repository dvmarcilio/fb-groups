package model.graph;

import model.fbdata.Interaction;

public class Interactions {

	private int total = 0;
	private int totalComments = 0;
	private int totalMentions = 0;

	public void add(Interaction.Type interactionType) {
		addToSpecificTypeCount(interactionType);
		total += 1;
	}

	private void addToSpecificTypeCount(Interaction.Type interactionType) {
		switch (interactionType) {
		case COMMENT:
			totalComments += 1;
			break;
		case MENTION:
			totalMentions += 1;
			break;
		}
	}

	public int getTotal() {
		return total;
	}

	public int getTotalComments() {
		return totalComments;
	}

	public int getTotalMentions() {
		return totalMentions;
	}

}

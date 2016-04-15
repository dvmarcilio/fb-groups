package model.graph;

import model.fbdata.Interaction;

public class Interactions {

	private Integer total = 0;
	private Integer totalComments = 0;
	private Integer totalTags = 0;
	private Integer totalLikes = 0;

	protected void add(Interaction.Type interactionType) {
		addToSpecificTypeCount(interactionType);
		total += 1;
	}

	private void addToSpecificTypeCount(Interaction.Type interactionType) {
		switch (interactionType) {
		case COMMENT:
			totalComments += 1;
			break;
		case TAG:
			totalTags += 1;
			break;
		case LIKE:
			totalLikes += 1;
		}
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getTotalComments() {
		return totalComments;
	}

	public Integer getTotalTags() {
		return totalTags;
	}

	public Integer getTotalLikes() {
		return totalLikes;
	}

}

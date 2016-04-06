package model;

public class Interaction {

	private User from;

	private User to;

	public Interaction() {
	}

	public Interaction(User from, User to) {
		super();
		this.from = from;
		this.to = to;
	}

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "Interaction [from=" + from + ", to=" + to + "]";
	}

}

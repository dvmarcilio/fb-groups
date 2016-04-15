package model.fbdata;

public class Interaction {

	private User from;

	private User to;

	private Type type;

	public enum Type {
		COMMENT, TAG, LIKE;
	}

	public Interaction() {
	}

	public Interaction(User from, User to, Type type) {
		super();
		this.from = from;
		this.to = to;
		this.type = type;
	}

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Interaction [from=" + from + ", to=" + to + ", type=" + type
				+ "]";
	}

	public boolean isSelfInteraction() {
		return to.equals(from);
	}

	public boolean envolvesUser(User groupUser) {
		return from.equals(groupUser) || to.equals(groupUser);
	}

}

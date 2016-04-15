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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Interaction other = (Interaction) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}

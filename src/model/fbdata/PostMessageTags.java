package model.fbdata;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class PostMessageTags {

	private List<Mention> tags = new LinkedList<>();

	public List<Mention> getTags() {
		return tags;
	}

	@JsonAnySetter
	public void set(String id, List<Mention> mention) {
		tags.addAll(mention);
	}

	@Override
	public String toString() {
		return "PostMessageTags [tags=" + tags + "]";
	}

	public int size() {
		return tags.size();
	}

}

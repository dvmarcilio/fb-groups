package model.fbdata;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class PostMessageTags {

	private List<Tag> tags = new LinkedList<>();

	public List<Tag> getTags() {
		return tags;
	}

	@JsonAnySetter
	public void set(String id, List<Tag> tag) {
		tags.addAll(tag);
	}

	@Override
	public String toString() {
		return "PostMessageTags [tags=" + tags + "]";
	}

	public int size() {
		return tags.size();
	}

}

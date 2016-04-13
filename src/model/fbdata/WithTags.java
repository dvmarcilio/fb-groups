package model.fbdata;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithTags {

	@JsonProperty("data")
	private List<Tag> tags = Collections.emptyList();

	public List<Tag> getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return "WithTags [tags=" + tags + "]";
	}

	public int size() {
		return tags.size();
	}

}

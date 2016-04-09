package model.fbdata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithTags {

	@JsonProperty("data")
	private List<Mention> tags;

	public List<Mention> getTags() {
		return tags;
	}

}

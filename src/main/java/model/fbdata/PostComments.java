package model.fbdata;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostComments {

	@JsonProperty("data")
	private List<Comment> comments = Collections.emptyList();

	public List<Comment> getComments() {
		return comments;
	}

	@Override
	public String toString() {
		return "PostComments [comments=" + comments + "]";
	}

	public int size() {
		return comments.size();
	}

}

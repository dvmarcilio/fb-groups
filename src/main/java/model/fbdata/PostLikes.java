package model.fbdata;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostLikes {

	@JsonProperty("data")
	private List<User> likesFrom = Collections.emptyList();

	public List<User> getLikesFrom() {
		return likesFrom;
	}

	@Override
	public String toString() {
		return "PostLike [likesFrom=" + likesFrom + "]";
	}

	public int size() {
		return likesFrom.size();
	}

}

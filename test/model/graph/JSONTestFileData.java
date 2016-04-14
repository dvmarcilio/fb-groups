package model.graph;

import model.fbdata.User;

public class JSONTestFileData {

	public static final Long GROUP_ID = 777L;

	public static final String POST_ID_1 = GROUP_ID + "_postId";
	
	public static final String GROUP_NAME = "GROUP";

	public static final int MURILLO_COMMENT_ID = 1111;

	public static final long DIEGO_COMMENT_ID = 1000L;

	public static final User GUSTAVO = new User(987L, "gustavo");

	public static final User MURILLO = new User(123L, "murillo");

	public static final User DIEGO = new User(666L, "diego");

	private static final String FILE_DIR = "/data/";

	public static final String POST_FILE_PATH = FILE_DIR + "post.json";

	public static final String FEED_FILE_PATH = FILE_DIR + "feed.json";

}

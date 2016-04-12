package model.graph;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import model.fbdata.Interaction;
import model.fbdata.Interaction.Type;
import model.fbdata.User;

public class GrouphNetworkGraphTest {

	private GroupNetworkGraph graph = new GroupNetworkGraph();

	@Test
	public void shouldCreateNodesIfTheyDontExistWhenAddingAnInteraction() {
		User u1 = new User(1L, "1");
		User u2 = new User(2L, "2");

		graph.addInteraction(new Interaction(u1, u2, Type.COMMENT));
		
		assertTrue(graph.hasNodeForUser(u1));
		assertTrue(graph.hasNodeForUser(u2));
	}

}

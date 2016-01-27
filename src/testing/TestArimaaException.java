package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import game.ArimaaException;

public class TestArimaaException {
	
	@Test
	public void testInitializes() {
		ArimaaException ae = new ArimaaException("ERROR: You messed up!");
		assertEquals("ERROR: You messed up!", ae.getMessage());
	}

}

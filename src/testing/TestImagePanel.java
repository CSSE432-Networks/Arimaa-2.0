package testing;

import static org.junit.Assert.*;

import javax.swing.ImageIcon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import game.ImagePanel;

public class TestImagePanel {
	
	ImagePanel testPanel;

	@Before
	public void setUp() throws Exception {
		this.testPanel = new ImagePanel(new ImageIcon("resources/BoardStoneBig.jpg").getImage());
	}

	@After
	public void tearDown() throws Exception {
		this.testPanel = null;
	}

	@Test
	public void testInitializes() {
		assertNotNull(testPanel);
	}
	
	@Test
	public void testSetRow() {
		assertTrue(testPanel.getRow() == 0);
		this.testPanel.setRow(3);
		assertTrue(testPanel.getRow() == 3);
	}

}

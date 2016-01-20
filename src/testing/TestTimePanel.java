package testing;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.swing.JLabel;

import game.GUI;
import game.Game;
import game.TimePanel;

import org.junit.Test;

public class TestTimePanel {

	@Test
	public void testInitializes() {
		TimePanel tp = new TimePanel(new GUI(), new Game(), 15, new JLabel());
		assertNotNull(tp);
	}
	
	@Test
	public void testUpdate() {
		TimePanel tp = new TimePanel(new GUI(), new Game(), 15, new JLabel());
		tp.unpause();
		//tp.update(5, 0);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals("<html> <b>" + 0 + ":" + 14 + "</b> </html>", tp.getTimerLabel().getText());
	}
	
	@Test
	public void testCancelTimer(){
		GUI gui=new GUI();
		Game game=new Game();
		TimePanel tp= new TimePanel(gui, game, 3, new JLabel());
		game.setWinner(1);
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(1, game.getWinner());
	}

	@Test
	public void testSwitchMove(){
		GUI gui=new GUI();
		Game game=new Game();
		TimePanel tp= new TimePanel(gui, game, 9, new JLabel());
		tp.unpause();
		game.setPlayerTurn(2);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		Field playerTurn = null;
		try {
			playerTurn = TimePanel.class.getDeclaredField("playerTurn");
		} catch (NoSuchFieldException | SecurityException e1) {
			//e1.printStackTrace();
		}
		playerTurn.setAccessible(true);
		int fieldValue = 0;
		try {
			fieldValue = (int)playerTurn.get(tp);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			//e.printStackTrace();
		}
		assertEquals(2,  fieldValue);
	}
}

/**
 * Adopted from code provided at http://stackoverflow.com/questions/18926839/timer-stopwatch-gui/18926890#18926890
 */
package game;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;

public class TimePanel {
	private JLabel timerLabel;
	private Timer timer;
	private int playerTurn;
	private boolean paused;

	public TimePanel(GUI gui, Game game, int startTime, JLabel label) {

		// timer = new Timer();
		playerTurn = game.getPlayerTurn();
		setTimerLabel(label);
		paused = true;

		java.util.Timer updateTimer = new java.util.Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			int s = startTime;

			@Override
			public void run() {
				if (game.getWinner() != 0) {
					updateTimer.cancel();
					return;
				}
				// update Panel text
				if (!paused){
					if (playerTurn == game.getPlayerTurn()) {
						s--;
					} else {
						s = startTime;
						playerTurn = game.getPlayerTurn();
					}
					if (s == 0) {
						int winner = 1;
						if (game.getPlayerTurn() == 1)
							winner = 2;
						game.setWinner(winner);
						gui.renderBoard();// to show winner pane
					}
				}
				int displays, m;
				m = s / 60;
				displays = s % 60;
				update(displays, m);
			}
		}, 0, 1000);
	}

	public void update(int s, int minute) {
		String sec = Integer.toString(s);
		String min = Integer.toString(minute);
		if (s < 10) {
			sec = "0" + sec;
		}

		getTimerLabel().setText("<html> <b>" + min + ":" + sec + "</b> </html>");
	}

	public JLabel getTimerLabel() {
		return timerLabel;
	}

	public void setTimerLabel(JLabel timerLabel) {
		this.timerLabel = timerLabel;
	}
	
	public void pause(){
		this.paused = true;
	}
	
	public void unpause(){
		this.paused = false;
	}
}
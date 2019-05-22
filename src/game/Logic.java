package game;

import gui.MainPanel;

public class Logic {
	
	// panel
	MainPanel mp;
	
	// game state
	int turno = 1; // the first player starts the game
	
	// dice
	Dice dice = new Dice();
	
	public Logic(MainPanel mp) {
		this.mp = mp;
	}

	/* rolls a dice */
	public void roll() {
		dice.roll();
	}
	
	public int getLastRoll() {
		return dice.getLastRoll();
	}

}

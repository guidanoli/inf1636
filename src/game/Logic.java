package game;

import java.util.Random;

import gui.MainPanel;

public class Logic {
	
	// panel
	MainPanel mp;
	
	// logic
	int turno;
	int lastRoll;
	
	public Logic(MainPanel mp) {
		this.mp = mp;
	}

	/* rolls a dice */
	public int roll() {
		lastRoll = new Random().nextInt()%6 + 1;
		return lastRoll;
	}
	

}

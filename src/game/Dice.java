package game;

import java.util.Random;

public class Dice {

	int lastRoll = 1; // default value
	
	/* rolls a dice */
	public void roll() {
		lastRoll = Math.abs(new Random().nextInt()%6) + 1;
	}
	
	public int getLastRoll() {
		return lastRoll;
	}
	
	
	
}

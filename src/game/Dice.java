package game;

import java.util.Random;

public class Dice {

	int [] lastRoll = {1,1}; // default value
	
	/* rolls a dice */
	public void roll() {
		lastRoll[0] = generateRoll();
		lastRoll[1] = generateRoll();
	}
	
	private int generateRoll() {
		return Math.abs(new Random().nextInt()%6) + 1;
	}
	
	public int [] getLastRolls() {
		return lastRoll;
	}
	
	public int getLastRollSum() {
		return lastRoll[0] + lastRoll[1];
	}
	
}

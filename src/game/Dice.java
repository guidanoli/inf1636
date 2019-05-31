package game;

import java.util.Random;

/**
 * 
 * <p>The {@code Dice} class simulates multiple <b>6-sided dice</b>
 * through very simple methods that generate random integers
 * from 1 to 6, using the {@link java.util.Random Random}
 * method {@link java.util.Random#nextInt() nextInt()}. This does <b>not</b>
 * make use of Random seeds.
 * 
 * <p>The <b>number of dice</b> is variable and is passed through the constructor.
 * 
 * <p>Rolling the dice is one action. Getting the result is another. This
 * allows for the dice to be consulted multiple times by different classes,
 * without the unnecessary passage of the end result. That way, the Dice object
 * is sufficient for both tasks.
 * 
 * <p>If needed, the array of dice can be provided through the {@link Dice#getLastRolls()
 * getLastRolls()} function, if the individual die results has to be output.
 * 
 * @author guidanoli
 * @see java.util.Random
 *
 */
public class Dice {

	protected int diceCount;
	protected final int sidesCount = 6;
	protected int [] lastRoll;
	
	/**
	 * <p><code>public Dice(int n)</code>
	 * <p>Constructs n dice, all with the side equivalent to the number one facing up. 
	 * @param n - number of dice
	 */
	public Dice(int n) {
		this.diceCount = n;
		lastRoll = new int[n];
		for(int i = 0; i < n; i++)
			lastRoll[i] = 1;
	}
	
	/**
	 * <p><code>public int getDiceCount()</code>
	 * <p>Returns the dice count provided to the constructor
	 * @return dice count
	 */
	public int getDiceCount() {
		return diceCount;
	}
	
	/**
	 * <p><code>public void roll()</code>
	 * <p>Rolls all dice, attributing to each one of them a new Random value varying from 1 to 6.
	 * <p>No Random seed is set before this very action.
	 * @see java.util.Random
	 */
	public void roll() {
		for(int i = 0 ; i < diceCount; i++)
			lastRoll[i] = generateRoll();
	}
	
	/**
	 * <p><code>public int [] getLastRolls()</code>
	 * <p>Returns an integer array of size equal to the diceCount given to the constructor and
	 * obtainable through the {@link Dice#getDiceCount() getDiceCount()} function.
	 * @return dice results from the last roll
	 */
	public int [] getLastRolls() {
		return lastRoll;
	}
	
	/**
	 * <p><code>public int getLastRollSum()</code>
	 * <p>Returns the sum of the results of each die. Being n the number of dice, this number can
	 * vary from n to 6*n, being 3.5*n the mid point of this distribution.
	 * @return sum of dice results from the last  roll 
	 */
	public int getLastRollSum() {
		int sum = 0;
		for(int diceResult : lastRoll)
			sum += diceResult;
		return sum;
	}

	private int generateRoll() {
		return Math.abs(new Random().nextInt()%sidesCount) + 1;
	}
}

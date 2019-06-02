package game;

import java.awt.Color;

/**
 * <p>A player is the user entity in the game that has
 * its personal bank account, position on the board,
 * color, and identification number.
 * <p>A player can exit the game, making it not accessible
 * by the {@link Logic} class anymore, thus, being
 * recycled by the {@code Java Garbage Collector}.
 * 
 * @author guidanoli
 *
 */
public class Player {

	protected static int count = 0;
	
	protected int id;
	protected int pos = 0;
	protected Color color;
	protected int bankAcc = 2458;
	
	/**
	 * Constructs a player
	 * @param color - players' color
	 */
	public Player(Color color) {
		this.color = color;
		this.id = count;
		Player.count ++;
	}
	
	/**
	 * @return position of player from the starting point
	 */
	public int getPos() {
		return pos;
	}
	
	/**
	 * @param pos - position of player from the starting point
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	/**
	 * @return player's color representation
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * @return bank account
	 */
	public int getBankAcc() {
		return bankAcc;
	}
	
	/**
	 * <p>Adds delta to players' bank account
	 * <p>Thus, you can either:
	 * <ul>
	 * <li>Debit (delta < 0)</li>
	 * <li>Do nothing (delta = 0)</li>
	 * <li>Credit (delta > 0)</li>
	 * </ul>
	 * <p><b>It does not allow the player to
	 * have a negative balance</b>
	 * @param delta - difference added to the
	 * players' bank account
	 * @return {@code true} if player would be
	 * broke after transfer.
	 * @see #canAfford(int)
	 */
	public boolean accountTransfer(int delta) {
		if( !canAfford(delta) ) return false;
		this.bankAcc = this.bankAcc + delta;
		return true;
	}
	
	/**
	 * <p>Checks if player will not have a negative
	 * balance after amount is deducted from its
	 * bank account
	 * <p>Serves as a pre-purchase check, in case
	 * it is not intended to manipulate the players'
	 * bank account directly 
	 * @param amount
	 * @return {@code true} if player can afford
	 * @see #accountTransfer(int)
	 * @see #getBankAcc()
	 */
	public boolean canAfford(int amount) {
		return amount <= bankAcc;
	}
	
	/**
	 * @return {@true} if player is broke and
	 * should be removed from the game.
	 */
	public boolean isBroke() { return bankAcc < 0; }
	
}

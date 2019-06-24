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
	
	private int roundsInPrisonLeft = 0;
	private ChanceCard card = null;
	
	private int pos = 0;
	private Color color;
	private String colorName;
	private long bankAcc = 2458;
	
	/**
	 * Constructs a player
	 * @param color - player's color
	 * @param colorName - player's color name
	 */
	public Player(Color color, String colorName) {
		this.color = color;
		this.colorName = colorName;
	}
	
	/**
	 * @return position of player from the starting point
	 * @see #setPos(int)
	 */
	public int getPos() {
		return pos;
	}
	
	/**
	 * @param pos - position of player from the starting point
	 * @see #getPos()
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	/**
	 * @return player's rounds in Prison Left
	 */
	public int getRoundsInPrisonLeft() {
		return roundsInPrisonLeft;
	}
	
	/**
	 * @return player's color representation
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * @param c - player color
	 */
	public void setColor(Color c) {
		this.color = c;
	}
	
	/**
	 * @return player's color name
	 */
	public String getColorName() {
		return colorName;
	}
	
	/**
	 * @param cname - player color name
	 */
	public void setColorName(String cname) {
		this.colorName = cname;
	}
	
	/**
	 * @return bank account
	 * @see #accountTransfer(int)
	 * @see #canAfford(int)
	 * @see #isBroke()
	 */
	public long getBankAcc() {
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
	 * <p><b>It allows the player to
	 * have a negative balance</b>
	 * @param delta - difference added to the
	 * players' bank account
	 * @return {@code true} if player is now
	 * broke after transfer.
	 * @see #canAfford(int)
	 * @see #getBankAcc()
	 * @see #isBroke()
	 */
	public boolean accountTransfer(int delta) {
		this.bankAcc = getBankAcc() + delta;
		return !isBroke();
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
	 * @return {@code true} if player is broke and
	 * should be removed from the game.
	 */
	public boolean isBroke() { return bankAcc < 0; }
	
	/**
	 * @return {@code true} if player has got a
	 * escape prison card
	 * @see #giveCard(ChanceCard)
	 * @see #takeCard()
	 */
	public boolean hasCard() { return card != null; }
	
	/**
	 * <p>Takes escape prison card from player and
	 * returns to be inserted again on deck 
	 * @return escape prison card
	 * @see #giveCard(ChanceCard)
	 * @see Player#hasCard()
	 */
	public ChanceCard takeCard() {
		ChanceCard temp = card;
		this.card = null;
		return temp;
	}
	
	/**
	 * <p>Gives escape prison card to player
	 * <p><b><i>It has to be assured that it got
	 * removed from the deck so there is not
	 * duplicate!!</i></b>
	 * @param card - escape prison card
	 * @see #takeCard()
	 * @see #hasCard()
	 */
	public void giveCard( ChanceCard card ) {
		if( card == null ) return;
		this.card = card;
	}
	
	/**
	 * @param inPrison - {@code true} if in prison
	 * @see #isInPrison()
	 */
	public void setInPrison( boolean inPrison ) {
		if( inPrison ) roundsInPrisonLeft = 3;
		else roundsInPrisonLeft = 0;
	}
	
	/**
	 * @param numRounds
	 */
	public void setInPrisonFor( int numRounds ) {
		roundsInPrisonLeft = numRounds;
	}
	
	/**
	 * @return {@code true} if in prison
	 * @see #setInPrison(boolean)
	 */
	public boolean isInPrison() {
		return roundsInPrisonLeft > 0;
	} 
	
	/**
	 * Updates rounds in prison counter
	 * @see #isInPrison()
	 */
	public void updateRoundsInPrisonCounter() {
		if( roundsInPrisonLeft > 0 ) roundsInPrisonLeft--;
	}

	/**
	 * Sets bank account balance
	 * @param newBalance - new balance value
	 */
	public void setBankAcc(int newBalance) {
		bankAcc = newBalance;		
	}
	
}

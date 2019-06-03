package game.cell;

import game.Player;

/**
 * <p>An ownable cell is a cell that can be bought or
 * owned by a player. One player can own as many cells
 * as there are on the board, but a single cell can be
 * owned by only one player at most.
 * <p>Initially, every cell does not have an owner, but once
 * owned, it will only be disowned when the player goes
 * bankrupt and leaves the board.
 * <p>The value charged from the player that steps on the cell
 * should be transfered to the cell owner's bank account.
 * <p>If the cell isn't owned yet, then there is no stepping fee.
 * <p>The two main children of the {@code OwnableCell} abstract
 * class are the {@link Territory} class and the {@link Service}
 * class.
 * @author guidanoli
 * @see Territory
 * @see Service
 *
 */
public abstract class OwnableCell extends AbstractCell {

	private Player owner = null;
	private int buyingFee;
	
	/**
	 * <p>Constructs ownable cell
	 * @param pos - position of cell in board from starting point.
	 */
	public OwnableCell(int pos, int buyingFee) {
		super(pos, true);
		this.buyingFee = buyingFee;
	}
	
	/**
	 * <p>Sets new owner of the cell.
	 * @param newOwner - new owner or {@code null} if current
	 * owner goes bankrupt and leaves the board.
	 */
	public void setOwner(Player newOwner) {
		owner = newOwner;
	}
	
	/**
	 * <p>Gets owner of cell.
	 * @return - owner or {@code null} if it is not
	 * currently owned by any player.
	 */
	public Player getOwner() { return owner; }

	/**
	 * @return fee charged to buy/own cell
	 */
	public int getBuyingFee() { return buyingFee; }
	
	/**
	 * Subtracts from owner the buying fee, if it is able
	 * to afford it.
	 * @param newOwner - new owner
	 * @return {@code false} if new owner cannot afford
	 * the buying fee
	 * @see #setOwner(Player)
	 */
	public boolean buy(Player newOwner) {
		if( getOwner() != null ) return false;
		if( !newOwner.accountTransfer(getBuyingFee()) ) return false;
		setOwner(newOwner);
		return true;
	}
	
	/**
	 * <p>Derived from the {@link #charge(Player, int)} method.
	 * @param player - player to be charged (not the owner)
	 * @param diceSum - the sum of the dice values
	 * @return value to be charged from the player
	 */
	abstract protected int getChargeValue(Player player, int diceSum);
	
	/**
	 * {@inheritDoc}
	 * <ul>
	 * <li><p>Transfers the amount to the owner</li>
	 * <li><p>Does <b>NOT</b> charge the cell owner.</li>
	 * <li><p>Does <b>NOT</b> charge if there is no owner.</li>
	 * </ul>
	 */
	int charge(Player player, int diceSum) {
		if( owner == null || player == owner ) return 0;
		int fee = getChargeValue(player,diceSum);
		owner.accountTransfer(fee);
		return fee;
	}
	
}

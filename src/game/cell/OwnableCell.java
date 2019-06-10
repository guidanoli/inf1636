package game.cell;

import java.awt.Image;

import game.Logic;
import game.Player;
import io.ImgList;

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
	private Image cardImage;
	
	/**
	 * <p>Constructs ownable cell
	 * @param name - name of the cell
	 * @param pos - position of cell in board from starting point.
	 */
	public OwnableCell(String name, String imgPath, int pos, int buyingFee) {
		super(name, pos, true);
		this.buyingFee = buyingFee;
		ImgList i = ImgList.getInstance();
		cardImage = i.addImg(imgPath);
	}
	
	/**
	 * <p>Sets new owner of the cell.
	 * @param newOwner - new owner or {@code null} if current
	 * owner goes bankrupt and leaves the board.
	 */
	public void setOwner(Player newOwner) { owner = newOwner; }
	
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
		int fee = getBuyingFee();
		if( !newOwner.canAfford(fee) ) return false;
		newOwner.accountTransfer(-fee);
		setOwner(newOwner);
		return true;
	}
	
	/**
	 * @return card image object
	 */
	public Image getCardImage() { return cardImage; }
	
	/**
	 * @return {@code true} is cell can be upgraded. (protected)
	 */
	abstract protected boolean isUpgradable();
	
	/**
	 * Upgrades ownable cell by one level, if possible,
	 * charging the fee automatically from the owner.
	 * @return {@code true} if upgrade was successful
	 */
	public boolean upgrade() { return isUpgradable(); }
	
	/**
	 * @return {@code true} if cell can be upgraded.
	 */
	public boolean canUpgrade() { return isUpgradable(); }
	
	/**
	 * @return fee charged to upgrade territory by one level
	 */
	public int getUpgradingFee() { return 0; }
	
	/**
	 * @return current upgrade level (0 = no upgrade)
	 */
	public int getUpgradeLevel() { return 0; }
	
	/**
	 * @return current upgrade level formatted to a string
	 * If not an upgradable cell, then {@code null} is returned.
	 */
	public String getUpgradeLevelString() {
		return isUpgradable() ? Integer.toString(getUpgradeLevel()) : null;
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
	public int charge(Player player, int diceSum) {
		if( owner == null || player == owner ) return 0;
		int fee = getChargeValue(player,diceSum);
		owner.accountTransfer(fee);
		return fee;
	}
	
	/**
	 * <p>Accounts for the buying fee and the upgrading fee(s)
	 * necessary to level to the current cell state.
	 * <p>For example:
	 * <ul>
	 * <li>if the cell hasn't been upgraded yet,
	 * it will be worth exactly its buying fee.</li>
	 * <li>On the other side of the spectrum, if a cell has been
	 * fully upgraded, it will be equal to the buying fee plus
	 * each upgrade fee.</li>
	 * </ul>
	 * @return the total value the cell is worth
	 */
	public int getWorthValue() {
		return getBuyingFee() + getUpgradeLevel()*getUpgradingFee();
	}
	
	/**
	 * <p>Sells the property for a percentage determined by
	 * the Logic module. The cell will then no longer be of
	 * possession of this player and now is open to ownership
	 * again, just like in the beginning of the game.
	 */
	public void sell() {
		Player owner = getOwner();
		if( owner == null ) return;
		int worth = getWorthValue();
		float percentage = Logic.getInstance().sellingPercentage;
		owner.accountTransfer((int) (worth*percentage));
		setOwner(null);
 	}
	
}

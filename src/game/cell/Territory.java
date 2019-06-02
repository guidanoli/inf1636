package game.cell;

import game.Player;

/**
 * <p>A territory is a cell that can be owned by 0-1 player,
 * and have its charging fee increased by the owner through
 * the act of building new establishments that cost him a fixed
 * fee each.
 * <p>A territory:
 * <ul>
 * <li>can be 'upgraded' as many times as stepping fees
 * provided by the class constructor.</li>
 * <li>when disowned, will be 'downgraded' to ground level.</li>
 * <li>can only be upgraded if the player has all its others
 * territories on the same level.</li>
 * </ul>
 * @author guidanoli
 *
 */
public class Territory extends OwnableCell {

	/**
	 * [0] = ground fee (no upgrade)
	 * [1..N] = fee with N upgrades
	 */
	private int [] steppingFees;
	private int upgradingFee;
	private int upgradeLevel = 0;
	
	/**
	 * <p>Constructs a territory cell 
	 * @param pos - position of cell in board from starting point.
	 * @param buyingFee - fee charged from the new owner in the
	 * moment of buying it.
	 * @param upgradingFee - fee charged from the owner in the
	 * moment of building a new establishment.
	 * @param groundFee - fee charged from the player that steps on the
	 * cell when on ground level.
	 * @param steppingFees - array of stepping fee. The integer of index i
	 * of this array should equal to the fee charged from the player
	 * that steps on this cell on level i+1, where level 0 means
	 * no upgrade made at all (<u>ground level</u>).<br>
	 * Be aware that the meaning of each 'level' may vary from
	 * territory and game context, but should always make the cell
	 * more costful to be stepped each new level, that is, the
	 * higher the index of the array.
	 */
	public Territory(int pos, int buyingFee, int upgradingFee, int groundFee, int... steppingFees) {
		super(pos,buyingFee);
		this.steppingFees = new int[steppingFees.length+1];
		this.steppingFees[0] = groundFee;
		for(int i = 0 ; i < steppingFees.length; i++)
			this.steppingFees[i+1] = steppingFees[i];
		this.upgradingFee = upgradingFee;
	}
	
	/**
	 * <p>Lowers upgrade level to ground level.
	 * {@inheritDoc}
	 */
	public void setOwner(Player newOwner) {
		super.setOwner(newOwner);
		upgradeLevel = 0;
	}
	
	/**
	 * @return fee charged to upgrade territory by one level
	 */
	public int getUpgradingFee() { return upgradingFee; }
	
	/**
	 * @return current upgrade level (0 = no upgrade)
	 */
	public int getUpgradeLevel() { return upgradeLevel; }
	
	/**
	 * @return {@code true} if can upgrade
	 * @see #Territory(int, int, int, int, int...) Constructor
	 */
	public boolean canUpgrade() {
		// TODO: check if:
		// - all its other buildings are on the same level
		return 	upgradeLevel + 1 < steppingFees.length;
	}
	
	/**
	 * Upgrades territory level by one, deducting from
	 * owner's bank account the building fee
	 * @return {@code false} if could not upgrade
	 * @see #canUpgrade()
	 */
	public boolean upgrade() {
		if( !canUpgrade() ) return false;
		if( !getOwner().accountTransfer(getUpgradingFee()) ) return false;
		upgradeLevel++;
		return true;
	}
	
	/**
	 * <p>Returns fee charged depending on fees established by the
	 * constructor and the current upgrade level
	 * {@inheritDoc}
	 */
	protected int getChargeValue(Player player, int diceSum) {
		return steppingFees[upgradeLevel];
	}

}
package game.cell;

import game.Player;

/**
 * <p>A service is a cell that can be owned by 0-1 player,
 * and have its charging fee increased by the dice sum of
 * the player that steps on it. The multiplier factor is
 * determined by the constructor.
 * <p>{@code Charged fee = multiplier * dice sum}
 * @author guidanoli
 *
 */
public class Service extends OwnableCell {

	private int multiplier;
	
	/**
	 * Constructs a service cell
	 * @param name - name of the cell
	 * @param pos - position of cell in board from starting point.
	 * @param buyingFee - fee charged from the new owner in the
	 * moment of buying it.
	 * @param multiplier - variable that determines the amount of
	 * money charged from the player that steps on this cell based
	 * on the sum of the dice values on its turn
	 */
	public Service(String name, int pos, int buyingFee, int multiplier) {
		super(name, pos,buyingFee);
		this.multiplier = multiplier;
	}
	
	/**
	 * <p>{@code Charged fee = multiplier * dice sum}
	 * @see #Service(String, int, int, int)
	 */
	protected int getChargeValue(Player player, int diceSum) {
		return diceSum*multiplier;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isUpgradable() { return false; }

}

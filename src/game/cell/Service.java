package game.cell;

import game.Player;

/**
 * <p>A service is a cell that can be owned by 0-1 player,
 * and have its charging fee increased by the dice sum of
 * the player that steps on it. The multiplier factor is
 * determined by the constructor.
 * @author guidanoli
 *
 */
public class Service extends OwnableCell {

	private int multiplier;
	
	public Service(int pos, int buyingFee, int multiplier) {
		super(pos,buyingFee);
		this.multiplier = multiplier;
	}
	
	protected int getChargeValue(Player player, int diceSum) {
		return diceSum*multiplier;
	}

}

package game.cell;

import game.Dice;
import game.Player;

/**
 * An abstract cell is the parent class for all the
 * cell classes in the game.cell package. It allows an extra
 * layer of abstraction and organization. It is subdivided
 * into two main children: {@link GameCell} and {@link OwnableCell}.
 * @author guidanoli
 *
 */
public abstract class AbstractCell {

	private int pos;
	private boolean isOwnable;
	
	/**
	 * Constructs an abstract cell
	 * @param pos - position of cell in board from starting point.
	 * @param isOwnable - if the cell can be owned/bought by a player.
	 * @see GameCell
	 * @see OwnableCell
	 */
	public AbstractCell(int pos, boolean isOwnable) {
		this.pos = pos;
		this.isOwnable = isOwnable;
	}
	
	/**
	 * <p>Charges player of certain amount for stepping in this cell.
	 * <p><b>This function should <b>NOT</b> subtract from the Player's balance!!</b>
	 * @param player - player that stepped on the cell
	 * @param diceSum - the sum of the dice values
	 * @return the amount that will be subtracted from the players' balance
	 * @see Dice#getLastRollSum() getLastRollSum()
	 */
	int charge(Player player, int diceSum) { return 0; }
	
	/**
	 * @return {@code true} if the cell is ownable,
	 * and {@code false} otherwise.
	 * <ul><li>Every OwnableCell is ownable.</li>
	 * <li>Every GameCell is not ownable.</li></ul>
	 * @see OwnableCell
	 * @see GameCell
	 */
	boolean isOwnable() { return isOwnable; }
	
	/**
	 * @return position of cell in board from starting point
	 */
	int getPosition() { return pos; }
	
}

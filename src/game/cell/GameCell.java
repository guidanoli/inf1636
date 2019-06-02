package game.cell;

/**
 * A game cell does not have a owner, but it can charge
 * or provide amount from/to the player that steps on it
 * just like an ownable cell.
 * 
 * @author guidanoli
 *
 */
public abstract class GameCell extends AbstractCell {

	/**
	 * <p>Constructs a game cell
	 * @param pos - position of cell in board from starting point.
	 */
	public GameCell(int pos) {
		super(pos, false);
	}

}

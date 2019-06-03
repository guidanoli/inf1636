package game.cell;

/**
 * A game cell does not have an owner, but it can charge
 * or provide amounts from/to the player that steps on it
 * just like an ownable cell.
 * 
 * @author guidanoli
 * @see OwnableCell
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

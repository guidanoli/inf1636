package game.cell;

/**
 * A game cell does not have an owner, but it can charge
 * or provide amounts from/to the player that steps on it
 * just like an ownable cell.
 * 
 * @author guidanoli
 * @see OwnableCell
 */
public class GameCell extends AbstractCell {

	/**
	 * <p>Constructs a game cell
	 * @param name - name of the cell
	 * @param pos - position of cell in board from starting point.
	 */
	public GameCell(String name, int pos) {
		super(name, pos, false);
	}

}

package game.cell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.Player;

/**
 * The {@code ActionCell} class is a concrete class that inherits from
 * {@link GameCell} and allows the developer to determine a specific
 * action to be triggered whenever the player steps into the cell.
 * 
 * @author guidanoli
 *
 */
public class ActionCell extends GameCell {

	private ActionListener al;
	
	/**
	 * Constructs an action cell
	 * @param name - name of the cell
	 * @param pos - position of the cell from the starting point
	 * @param listener - action listener that will be warned whenever
	 * a player steps on it through the method
	 * {@link ActionListener#actionPerformed(ActionEvent) actionPerformed(ActionEvent)}
	 * where {@link ActionEvent} has the {@link Player} as source object.
	 */
	public ActionCell(String name, int pos, ActionListener listener)
	{
		super(name, pos);
		al = listener;
	}
	
	public int charge(Player player, int diceSum)
	{
		ActionEvent ae = new ActionEvent(player,0,"Player stepped on the cell!");
		al.actionPerformed(ae);
		return 0;
	}
	

}

package game.cell;

import game.ChanceCard;
import game.Logic;
import game.Player;

/**
 * A chance cell will provide the player of a chance card
 * from the top of the chance card deck, and, depending on
 * the type of the card, execute an action (or store the 
 * card for later, if it is a prison escape pass card), and
 * then put it back on a random spot of the deck.
 * @author guidanoli
 * @see game.ChanceCard ChanceCard
 */
public class ChanceCell extends GameCell {
	
	public ChanceCell(String name, int pos) {
		super(name, pos);
	}
	
	public int charge(Player player, int diceSum) {
		Logic logic = Logic.getInstance();
		ChanceCard card = logic.deck.peekFirst();
		card.triggerCard(player);
		// Here, the following events can occur:
		// - money is deducted or given
		// - cards can be removed from the top of the deck
		// - the player can move to another cell
		// The card will be always show to screen
		
		// Puts first card to the end of the deck
		ChanceCard temp = logic.deck.pollFirst();
		logic.deck.offerLast(temp);
		return 0;
	}

}

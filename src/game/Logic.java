package game;

import java.awt.Color;
import java.util.ArrayList;

/**
 * <p>Logic is a class that generates a singleton that manages
 * the game mechanics, tying the game package to the gui package,
 * mainly the {@link gui.MainPanel MainPanel} class.
 * 
 */
public class Logic {
	
	/* *********
	 * VARIABLES
	 * ********* */
	
	// singleton
	private static final Logic INSTANCE = new Logic(); 
	
	// game state
	protected int turn = 0; // the first player starts the game
		
	// dice
	public final Dice dice = new Dice(2);
	
	// players
	protected final int max_player_count = 6;
	protected ArrayList<Player> players = new ArrayList<Player>();
	
	// players' color name
	protected final String [] playerColorNames = { 
		"vermelho" ,
		"azul" ,
		"laranja" ,
		"amarelo" ,
		"roxo" ,
		"cinza" ,
	};
	
	// players' color id
	protected final Color [] playerColorIds = {
		new Color(255,23,0) ,
		new Color(36,98,193) ,
		new Color(238,133,1) ,
		new Color(248,233,23) ,
		new Color(184,0,187) ,
		new Color(128,116,102)
	};
	
	/* *********
	 * SINGLETON
	 * ********* */
	
	private Logic() { }

	/**
	 * <p>Get instance of Logic
	 * @return Logic class {@code singleton}
	 */
	public static Logic getInstance() { return INSTANCE; }
	
	/* *******
	 * PLAYERS
	 * ******* */
	
	/**
	 * <p>Sets the total number of players
	 * @param numOfPlayers - number of players
	 */
	public void setNumOfPlayers(int numOfPlayers) {
		for( int i = 0; i < numOfPlayers && i < max_player_count; i++ )
			players.add( new Player(playerColorIds[i]) );
	}
	
	/**
	 * @return array of player objects 
	 */
	public ArrayList<Player> getPlayers() { return players; }
	
	/**
	 * @return array of player positions on the board, being
	 * it the distance from the starting point modulo (%) the number
	 * of cells on the board.
	 */
	public ArrayList<Integer> getPlayersPos() {
		ArrayList<Integer> playersPos = new ArrayList<Integer>();
		for( Player player : getPlayers() ) {
			playersPos.add(player.getPos());
		}
		return playersPos;
	}
	
	/**
	 * @param i - index
	 * @return position of i-th player <u>on the board</u>. The index of
	 * the player is determined by the order in which the pins are
	 * arranged within a cell and not its position on the board.
	 */
	public int getPlayerPos(int i) {
		Player p = players.get(i);
		return p.getPos();
	}

	/**
	 * @return the color of the current turn's player
	 */
	public Color getCurrentPlayerColor() {
		return playerColorIds[turn];
	}
	
	/**
	 * @return number of players on the board currently. If a player
	 * has gone bankrupt, it will be removed from the board, and,
	 * therefore, from this player count method.
	 */
	public int getNumPlayers() { return players.size(); }
	
	/* *************
	 * GAME COMMANDS
	 * ************* */

	/**
	 * <p>Rolls the dice and places the current turn's player on the
	 * correct cell of the board, triggering whichever events that
	 * the final cell may be responsible for.
	 * <p>Also, takes into account if the player crosses the starting
	 * point, rewarding him with a monetary bonus.
	 */
	public void roll() {
		canRollFlag = false;
		dice.roll();
		Player p = players.get(turn);
		int oldPos = p.getPos();
		int newPos = (oldPos + dice.getLastRollSum())%36;
		if( oldPos > newPos ) p.accountTransfer(200); // starting point bonus
		p.setPos(newPos);
	}
	
	/**
	 * <p>End current turn and pass to the next, following the order
	 * as established from the beginning of the game.
	 */
	public void endTurn() {
		canRollFlag = true;
		nextTurn();
	}
	
	private void nextTurn() {
		turn = (turn+1)%getNumPlayers();
	}
	
	/* **********************
	 * GUI BUTTONS ACTIVENESS
	 * ********************** */
	
	/**
	 * <p>Two state deterministic finite automaton A = (Q,E,d)
	 * <p><b>Q = {r,p}</b>
	 * <ul><li>r: roll</li>
	 * <li>p: player actions</li></ul>
	 * <p><b>E = {0,1}</b>
	 * <ul><li>0: roll()</li>
	 * <li>1: endTurn()</li></ul>
	 * <p><b>d:QxE->E</b>
	 * <ul><li>d(r,0)=1</li>
	 * <li>d(p,1)=0</li></ul>
	 * <p>canRoll() -> <b>current state == r ?</b>
	 * <p>canEnd() -> <b>current state == p ?</b>
	 */
	private boolean canRollFlag = true;
	
	public boolean canRoll() {
		return canRollFlag;
	}
	
	public boolean canEnd() {
		return !canRollFlag;
	}
	
	public boolean canBuy() {
		return !canRollFlag;
	}
	
//	public boolean canBuy() {
//		return !canRollFlag;
//	}
	
}

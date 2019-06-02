package game;

import java.awt.Color;
import java.util.ArrayList;

public class Logic {
	
	// singleton
	private static final Logic INSTANCE = new Logic(); 
	
	// game state
	int turn = 0; // the first player starts the game
		
	// dice
	public Dice dice = new Dice(2);
	
	// players
	final int max_player_count = 6;
	ArrayList<Player> players = new ArrayList<Player>();
	
	final String [] playerColorNames = 	{ 
											"vermelho" ,
											"azul" ,
											"laranja" ,
											"amarelo" ,
											"roxo" ,
											"cinza" ,
										};
	
	final Color [] playerColorIds = 	{
											new Color(255,23,0) ,
											new Color(36,98,193) ,
											new Color(238,133,1) ,
											new Color(248,233,23) ,
											new Color(184,0,187) ,
											new Color(128,116,102)
										};
	
	private Logic() { }
	
	public void setNumOfPlayers(int numOfPlayers) {
		for( int i = 0; i < numOfPlayers && i < max_player_count; i++ )
			players.add( new Player(playerColorIds[i]) );
	}
	
	public static Logic getInstance() { return INSTANCE; }

	/* rolls a dice */
	public void roll() {
		dice.roll();
		int oldPos = players.get(turn).getPos();
		int newPos = (oldPos + dice.getLastRollSum())%37;
		if( oldPos > newPos ) doLoopBonus();
		players.get(turn).setPos(newPos);
		nextTurn();
	}
	
	/* player does a loop in the board */
	protected void doLoopBonus() {
		System.out.println("Loop bonus!");
	}
	
	public ArrayList<Player> getPlayers() { return players; }
	
	public ArrayList<Integer> getPlayersPos() {
		ArrayList<Integer> playersPos = new ArrayList<Integer>();
		for( Player player : this.getPlayers() ) {
			playersPos.add(player.getPos());
		}
		return playersPos;
	}
	
	public int getPlayerPos(int i) {
		Player p = players.get(i);
		return p.getPos();
	}

	public Color getCurrentPlayerColor() {
		return playerColorIds[turn];
	}
	
	public int getNumPlayers() { return players.size(); }
	
	public void nextTurn() {
		turn = (turn+1)%getNumPlayers();
	}
	
}

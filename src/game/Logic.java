package game;

import java.awt.Color;
import java.util.ArrayList;

public class Logic {

	private enum STATE {
		ROLL ,
		TOASTS ,
		END
	};
	
	// game state
	int turno = 0; 				// the first player starts the game
	STATE state = STATE.ROLL; 	// the game starts with the first player rolling the dice
	
	// toast array
	ArrayList<String> toastArray = new ArrayList<String>();
	
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
	
	public Logic(int numOfPlayers) {
		for( int i = 0; i < numOfPlayers && i < max_player_count; i++ )
			players.add( new Player(playerColorIds[i]) );
	}

	/* rolls a dice */
	public void roll() {
		dice.roll();
		int oldPos = players.get(turno).getPos();
		int newPos = (oldPos + dice.getLastRollSum())%37;
		if( oldPos > newPos ) doLoopBonus();
		players.get(turno).setPos(newPos);
	}
	
	/* player does a loop in the board */
	protected void doLoopBonus() {
		addToast("+$200");
	}
	
	public String nextToast() {
		if( toastArray.isEmpty() ) return null;
		String toast = toastArray.get(0);
		toastArray.remove(0);
		return toast;
	}
	
	protected void addToast(String toast) {
		toastArray.add(toast);
	}
	
	public STATE getState() { return state; }
	
	public void emptyToast() {
		while( !toastArray.isEmpty() ) toastArray.remove(0);
	}
	
	public boolean isToastEmpty() { return toastArray.isEmpty(); }
		
	public void nextState() {
		if( state == STATE.ROLL )
		{
			// treat house events
			state = STATE.TOASTS;
		}
		else if(state == STATE.TOASTS )
		{
			state = STATE.END;
			emptyToast();
		}
		else
		{
			nextTurn();
			state = STATE.ROLL;
		}
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
		return playerColorIds[turno];
	}
	
	public int getNumPlayers() { return players.size(); }
	
	public void nextTurn() {
		turno = (turno+1)%getNumPlayers();
		addToast(String.format("É o turno do jogador %s.", playerColorNames[turno]));
	}
	
}

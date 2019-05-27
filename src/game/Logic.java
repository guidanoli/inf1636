package game;

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
	public Dice dice = new Dice();
	
	// players
	protected final int max_player_count = 6;
	ArrayList<Player> players = new ArrayList<Player>();
	final String [] player_colors = {"vermelho","azul","laranja","amarelo","roxo","cinza"};
	
	public Logic(int numOfPlayers) {
		for( int i = 0; i < numOfPlayers && i < max_player_count; i++ )
			players.add( new Player(i) );
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
	public void doLoopBonus() {
		addToast("+$200");
	}
	
	public String nextToast() {
		if( toastArray.isEmpty() ) return null;
		String toast = toastArray.get(0);
		toastArray.remove(0);
		return toast;
	}
	
	public void addToast(String toast) {
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
			emptyToast();
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

	public int getNumPlayers() { return players.size(); }
	
	public void nextTurn() {
		turno = (turno+1)%getNumPlayers();
		System.out.println(turno);
		addToast(String.format("É o turno do jogador %s!",player_colors[turno]));
	}
}

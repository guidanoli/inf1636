package game;

import java.awt.Color;
import java.util.ArrayList;

import org.w3c.dom.ranges.Range;

import com.sun.javafx.geom.Rectangle;

import gui.MainPanel;

public class Logic {
	
	// panel
	//MainPanel mp;
	
	// game state
	int turno = 0; // the first player starts the game
	
	// dice
	Dice dice = new Dice();
	
	// players
	
	ArrayList<Player> players = new ArrayList<Player>();
	
	public Logic(/*MainPanel mp*/) {
		//this.mp = mp;
	}

	/* rolls a dice */
	public void roll() {
		dice.roll();
		int oldPos = players.get(turno).getPos();
		int newPos = (oldPos + getLastRoll())%37;
		players.get(turno).setPos( newPos );
	}
	
	public int getLastRoll() {
		return dice.getLastRoll();
	}
	
	public Player addPlayer() {
		int addedPlayerId;
		if( this.getNumPlayers() > 5 ) {
			System.out.println("Impossível adicionar mais jogadores");
			return null;
		} else {
			addedPlayerId = this.getNumPlayers() + 1;
			this.players.add( new Player( addedPlayerId) );
			return players.get(addedPlayerId - 1);
		}
	}
	
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	public ArrayList<Integer> getPlayersPos() {
		ArrayList<Integer> playersPos = new ArrayList<Integer>();
		for( Player player : this.getPlayers() ) {
			playersPos.add(player.getPos());
		}
		return playersPos;
	}

	public int getNumPlayers() {
		return this.players.size();
	}

}

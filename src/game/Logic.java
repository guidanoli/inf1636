package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import game.cell.*;
import io.LocalResources;

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
		new Color(242,181,12) ,
		new Color(184,0,187) ,
		new Color(128,116,102)
	};
	
	// cells
	public final int numOfCells = 36; 
	private AbstractCell [] cells = new AbstractCell[numOfCells];
	
	/* *********
	 * SINGLETON
	 * ********* */
	
	private Logic() {
		loadCells();
	}

	/**
	 * <p>Gets instance of Logic
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
	 * @return the name of the color of the current turn's player
	 */
	public String getCurrentPlayerColorName() {
		return playerColorNames[turn];
	}
		
	/**
	 * @return current player object
	 */
	public Player getCurrentPlayer() {
		return players.get(turn);
	}

	public String [] getCurrentPlayerCellsNames() {
		ArrayList<OwnableCell> cells = getCurrentPlayerCells();
		String [] cellNames = new String[cells.size()];
		Iterator<OwnableCell> iterator = cells.iterator();
		int index = 0;
		while( iterator.hasNext() ) {
			OwnableCell cell = iterator.next();
			cellNames[index] = cell.getName();
			index++;
		}
		return cellNames;
	}
	
	/**
	 * @return number of players on the board currently. If a player
	 * has gone bankrupt, it will be removed from the board, and,
	 * therefore, from this player count method.
	 */
	public int getNumPlayers() { return players.size(); }
		
	/* *****
	 * CELLS
	 * ***** */
	
	private void loadCells() {
		String cellInfoPath = LocalResources.metaFolder + "cells_info.csv";
		ArrayList<ArrayList<String>> argList = CSVReader.read(cellInfoPath, true);
		Iterator<ArrayList<String>> iterator = argList.iterator();
		while(iterator.hasNext()) {
			ArrayList<String> cell = iterator.next();
			assert(cell.size() >= 3); //bad format!
			int pos = Integer.parseInt(cell.get(0));
			String name = cell.get(1);
			int type = Integer.parseInt(cell.get(2));
			AbstractCell newCell = null;
			switch(type) {
			case 0:
				// inert cell
				break;
			case 1:
				// chance
				break;
			case 2:
				// go to prison
				break;
			case 3:
				// transaction cell
				break;
			case 4:
				// territory
				assert(cell.size() >= 7);
				int [] additionalFees = null;
				if( cell.size() > 8 ) {
					additionalFees = new int[cell.size()-7];
					for(int i = 0 ; i < additionalFees.length; i++)
						additionalFees[i] = Integer.parseInt(cell.get(i+7));
				}
				newCell = new Territory(name,
										pos,
										Integer.parseInt(cell.get(4)),
										Integer.parseInt(cell.get(5)),
										Integer.parseInt(cell.get(6)),
										additionalFees);
				break;
			case 5:
				// service
				assert(cell.size() == 6);
				newCell = new Service(	name,
										pos,
										Integer.parseInt(cell.get(4)),
										Integer.parseInt(cell.get(5)));
				break;
			}
			cells[pos] = newCell;
		}
	}
	
	public void clickedOnCell(int pos) {
		System.out.printf("You clicked on cell %d\n",pos);
	}
	
	/**
	 * @return array of cells owned by current player
	 */
	public ArrayList<OwnableCell> getCurrentPlayerCells() {
		ArrayList<OwnableCell> playerCells = new ArrayList<OwnableCell>();
		Player player = getCurrentPlayer();
		for(int i = 0 ; i < numOfCells; i++) {
			AbstractCell cell = cells[i];
			if( cell == null ) continue; // TODO: instantiate all cells
			if( cell.isOwnable() ) {
				OwnableCell ownableCell = (OwnableCell) cell;
				if( player == ownableCell.getOwner() ) {
					playerCells.add(ownableCell);
				}
			}
		}
		return playerCells;
	}
	
	private AbstractCell getCurrentPlayerSteppingCell() {
		return cells[getCurrentPlayer().getPos()];
	}
	
	/* *************
	 * GAME COMMANDS
	 * ************* */

	/**
	 * <p>Rolls the dice and places the current turn's player on the
	 * correct cell of the board, triggering whichever events that
	 * the final cell may be responsible for.
	 * <p>Also, takes into account if the player crosses the starting
	 * point, rewarding him with a monetary bonus.
	 * <p>This method can only be called if {@link #canRoll()} == true
	 */
	public void roll() {
		if( !canRoll() ) return; // never trust the user
		canRollFlag = false;
		dice.roll();
		Player p = players.get(turn);
		int oldPos = p.getPos();
		int newPos = (oldPos + dice.getLastRollSum())%numOfCells;
		if( oldPos > newPos ) p.accountTransfer(200); // starting point bonus
		p.setPos(newPos);
	}
	
	/**
	 * <p>End current turn and pass to the next, following the order
	 * as established from the beginning of the game.
	 * <p>This method can only be called if {@link #canEndTurn()} == true
	 */
	public void endTurn() {
		if( !canEndTurn() ) return; // never trust the user
		canRollFlag = true;
		nextTurn();
	}
	
	/**
	 * <p>Buys currently stepping cell property and assigns its ownership
	 * to the current turn's player.
	 * <p>This method can only be called if {@link #canBuy()} == true
	 */
	public void buy() {
		if( !canBuy() ) return; // never trust the user
		// canBuy() == true guarantees:
		// - current player stepping cell is ownable cell
		// - current player can afford the buying fee
		
		OwnableCell cell = (OwnableCell) getCurrentPlayerSteppingCell();
		int fee = cell.getBuyingFee();
		Player player = getCurrentPlayer();
		player.accountTransfer(-fee);
		cell.setOwner(player);
	}
	
	/**
	 * <p>Upgrades currently stepping cell property by one level IF possible
	 * <p>This method can only be called if {@link #canUpgrade()} == true
	 */
	public void upgrade() {
		if( !canUpgrade() ) return; // never trust the user
		// canUpgrade() == true guarantees:
		// - current player stepping cell is ownable cell and owned by it
		// - current player can afford the upgrade fee
		
		OwnableCell cell = (OwnableCell) getCurrentPlayerSteppingCell();
		cell.upgrade();
	}
	
	/**
	 * <p>Cycles the turn counter to the next player
	 */
	private void nextTurn() {
		turn = (turn+1)%getNumPlayers();
	}
	
	/* **********************
	 * GUI BUTTONS ACTIVENESS
	 * ********************** */
	
	/**
	 * <p>Two state deterministic finite automaton A = (Q,E,d)
	 * <p><b>Q = {R,P}</b>
	 * <ul><li>R: roll</li>
	 * <li>P: player actions</li></ul>
	 * <p><b>E = {0,1}</b>
	 * <ul><li>0: roll()</li>
	 * <li>1: endTurn()</li></ul>
	 * <p><b>d:QxE->Q</b>
	 * <ul><li>d(R,0)=P</li>
	 * <li>d(P,1)=R</li></ul>
	 * <p><b>Enabled when current state is R:</b>
	 * <ul><li>canRoll()</li></ul>
	 * <p><b>Enabled when current state is P:</b>
	 * <ul><li>canEndTurn()</li>
	 * <li>canBuy()</li>
	 * <li>canUpgrade()</li></ul>
	 */
	private boolean canRollFlag = true;
	
	/**
	 * @return {@code true} if dice can be rolled.
	 */
	public boolean canRoll() {
		return canRollFlag;
	}
	
	/**
	 * @return {@code true} if turn can be ended
	 */
	public boolean canEndTurn() {
		return !canRollFlag;
	}
	
	/**
	 * @return {@code true} if current turn's player can buy the
	 * cell it is currently stepping in.
	 */
	public boolean canBuy() {
		if( canRollFlag ) return false; // waiting to roll dice first! 
		AbstractCell steppingCell = getCurrentPlayerSteppingCell();
		if( steppingCell == null ) return false; // TODO: implemented all cell types
		if( !steppingCell.isOwnable() ) return false; // can't be owned
		OwnableCell ownableCell = (OwnableCell) steppingCell;
		if( ownableCell.getOwner() != null ) return false; // is already owned (maybe even by the current player itself)
		int fee = ownableCell.getBuyingFee();
		return getCurrentPlayer().canAfford(fee); // can the current player afford it?
	}
	
	/**
	 * @return {@code true} if current turn's player can upgrade the
	 * cell it is currently stepping in.
	 * @see OwnableCell#canUpgrade()
	 */
	public boolean canUpgrade() {
		if( canRollFlag ) return false; // waiting to roll dice first! 
		AbstractCell steppingCell = getCurrentPlayerSteppingCell();
		if( steppingCell == null ) return false; // TODO: implemented all cell types
		if( !steppingCell.isOwnable() ) return false; // game cell!
		OwnableCell ownableCell = (OwnableCell) steppingCell;
		if( ownableCell.getOwner() != getCurrentPlayer() ) return false; // not your cell!
		return ownableCell.canUpgrade(); // can the current player upgrade it?
	}
	
}

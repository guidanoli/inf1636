package game;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.StringJoiner;

import javax.swing.JOptionPane;

import game.cell.*;
import gui.ImageDialog;
import gui.PropertyDialog;
import io.CSVReader;
import io.LocalResources;

import io.StateManager;
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
	
	// frame output
	private Frame frame; 
	
	// game state
	private int turn = 0; // the first player starts the game
		
	// dice - accessible but final
	public final Dice dice = new Dice(2);
	
	// card deck
	public final Deque<ChanceCard> deck = new ArrayDeque<ChanceCard>();
	
	// players
	private final int max_player_count = 6;
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Player> podium = new ArrayList<Player>(); 
	
	// players' color name
	private final String [] playerColorNames = { 
		"vermelho" ,
		"azul" ,
		"laranja" ,
		"amarelo" ,
		"roxo" ,
		"cinza" ,
	};
	
	// players' color id
	private final static Color [] playerColorIds = {
		new Color(255,23,0) ,
		new Color(36,98,193) ,
		new Color(238,133,1) ,
		new Color(242,181,12) ,
		new Color(184,0,187) ,
		new Color(128,116,102)
	};
	
	// cells
	public final float sellingPercentage = 0.9f;
	public final int numOfCells = 36; 
	private AbstractCell [] cells = new AbstractCell[numOfCells];
	
	/* *********
	 * SINGLETON
	 * ********* */
	
	private Logic() {
		loadDeck();
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
			players.add( new Player(playerColorIds[i], playerColorNames[i]) );
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
		return getCurrentPlayer().getColor();
	}
	
	/**
	 * @return the name of the color of the current turn's player
	 */
	public String getCurrentPlayerColorName() {
		return getCurrentPlayer().getColorName();
	}
		
	/**
	 * @param id - offset in pos and color arrays
	 * @return current position in array (of those who are still on the board)
	 */
	public int getPlayerPinId(int id) {
		Player p = players.get(id);
		int offset = 0;
		for( String color : playerColorNames )
		{
			if( p.getColorName().equals(color) )
				return offset;
			offset++;
		}
		return 0;
	}
	
	/**
	 * @return current player object
	 */
	public Player getCurrentPlayer() {
		return players.get(turn);
	}

	/**
	 * @return array of all the current player's owned cells' names
	 */
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
	 * Sums player's fortunes, accounting for bank account and
	 * each of its owned cells' values
	 * @param player
	 * @return fortune sum
	 * @see OwnableCell#getWorthValue() getWorthValue()
	 */
	public long getPlayerFortuneSum(Player player) {
		long sum = player.getBankAcc();
		ArrayList<OwnableCell> cells = getPlayerCells(player);
		for( OwnableCell cell : cells ) sum += cell.getWorthValue();
		return sum;
	}
	
	/**
	 * @return player color ids (Color objects)
	 */
	public static Color[] getPlayerColorIds() {
		return playerColorIds;
	}

	/**
	 * Check if all the territories of group are owned by current player
	 * @param group - group identification
	 * @return {@code true} if player has control of group
	 */
	public boolean currentPlayerHasGroup(int group) {
		ArrayList<OwnableCell> playerCells = getCurrentPlayerCells();
		int playerGroupCount = 0, groupCount = 0;
		for( OwnableCell playerCell : playerCells )
		{
			if( playerCell instanceof Territory )
			{
				Territory territory = (Territory) playerCell;
				if( territory.getGroup() == group ) playerGroupCount++;
			}
		}
		for( AbstractCell cell : cells )
		{
			if( cell instanceof Territory )
			{
				Territory territory = (Territory) cell;
				if( territory.getGroup() == group ) groupCount++;
			}
		}
		return groupCount == playerGroupCount;
	}
	
	private void removeCurrentPlayer() {
		Player currentPlayer = getCurrentPlayer();
		ArrayList<OwnableCell> ownedCells = getCurrentPlayerCells();
		for( OwnableCell cell : ownedCells ) cell.setOwner(null);
		if( currentPlayer.hasCard() )
		{
			ChanceCard card = currentPlayer.takeCard();
			deck.offerLast(card);
		}
		podium.add(currentPlayer);
		players.remove(currentPlayer);
		updateTurn();
		if( players.size() == 1 )
		{
			Player winner = players.remove(0);
			podium.add(winner);
			showPodium();
			System.exit(0);
		}
	}
	
	/**
	 * <p>Displays players in podium in order
	 * <p>That is, the last player to be added to the podium
	 * is highest on the podium.
	 * <p>Make sure all players are on the podium!
	 */
	private void showPodium() {
		Frame outputFrame = getFrame();
		final String nl = "\n";
		final String comma = ", ";
		if( outputFrame != null )
		{
			StringJoiner podiumSJ = new StringJoiner(nl);
			StringJoiner placeSJ = new StringJoiner(comma);
			int place = 1;
			long previous_fortune = -1;
			while(!podium.isEmpty())
			{
				Player p = podium.remove(podium.size()-1);
				long current_fortune = getPlayerFortuneSum(p);
				if( previous_fortune == -1 ) previous_fortune = current_fortune;
				if( current_fortune != previous_fortune
					|| previous_fortune <= 0 )
				{
					String placeStr = placeSJ.toString();
					podiumSJ.add(String.format("%dº lugar - %s ($ %d)",place,placeStr,previous_fortune));
					placeSJ = new StringJoiner(comma);
					place++;
				}
				placeSJ.add(p.getColorName());
				previous_fortune = current_fortune; 
			}
			if( ! placeSJ.toString().equals("") )
			{
				String placeStr = placeSJ.toString();
				podiumSJ.add(String.format("%dº lugar - %s ($ %d)",place,placeStr,previous_fortune));
			}
			JOptionPane.showMessageDialog(outputFrame,podiumSJ.toString(),"Fim de jogo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * @return number of players on the board currently. If a player
	 * has gone bankrupt, it will be removed from the board, and,
	 * therefore, from this player count method.
	 */
	public int getNumPlayers() { return players.size(); }
		
	/* ****
	 * DECK
	 * **** */
	
	private void loadDeck() {
		String cardsInfoPath = LocalResources.metaFolder + "chance_cards.csv";
		ArrayList<ArrayList<String>> argList = CSVReader.read(cardsInfoPath, true);
		Collections.shuffle(argList); // shuffles deck first!
		Iterator<ArrayList<String>> iterator = argList.iterator();
		while(iterator.hasNext()) {
			ArrayList<String> cardInfo = iterator.next();
			assert(cardInfo.size() >= 2); // bad format!
			String imagePath = LocalResources.chanceCardsFolder + cardInfo.get(0) + ".jpg";
			int type = Integer.parseInt(cardInfo.get(1));
			ActionListener listener = null;
			int amount;
			switch(type) {
			case 0:
				// misfortune
				amount = Integer.parseInt(cardInfo.get(2));
				listener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showCardToScreen();
						Player player = (Player) e.getSource();
						player.accountTransfer(-amount);
					}
				};
				break;
			case 1:
				// fortune
				amount = Integer.parseInt(cardInfo.get(2));
				listener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showCardToScreen();
						Player player = (Player) e.getSource();
						player.accountTransfer(amount);
					}
				};
				break;
			case 2:
				// go to prison card
				listener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showCardToScreen();
						Player player = (Player) e.getSource();
						if( player.hasCard() )
						{
							ChanceCard card = player.takeCard();
							deck.offerLast(card);
						}
						else
						{
							sendToPrison(player);
						}
					}
				};
				break;
			case 3:
				// escape prison card
				listener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showCardToScreen();
						Player player = (Player) e.getSource();
						ChanceCard card = deck.pollFirst();
						player.giveCard(card);
					}
				};
				break;
			case 4:
				// fortune (for each player)
				amount = Integer.parseInt(cardInfo.get(2));
				listener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showCardToScreen();
						Player player = (Player) e.getSource();
						for(Player p : players)
						{
							player.accountTransfer(amount);
							p.accountTransfer(-amount);
						}
					}
				};
				break;
			}
			deck.offerFirst(new ChanceCard(imagePath, listener));
		}
	}
	
	private void showCardToScreen() {
		if( getFrame() == null ) return;
		ChanceCard card = deck.peekFirst();
		Image cardImg = card.getCardImage();
		new ImageDialog(getFrame(), "Sorte ou Revés", cardImg);
	}
	
	/* *****
	 * CELLS
	 * ***** */
	
	private void loadCells() {
		String cellsInfoPath = LocalResources.metaFolder + "cells.csv";
		ArrayList<ArrayList<String>> argList = CSVReader.read(cellsInfoPath, true);
		Iterator<ArrayList<String>> iterator = argList.iterator();
		while(iterator.hasNext()) {
			ArrayList<String> cellInfo = iterator.next();
			assert(cellInfo.size() >= 3); // bad format!
			int pos = Integer.parseInt(cellInfo.get(0));
			String name = cellInfo.get(1);
			int type = Integer.parseInt(cellInfo.get(2));
			AbstractCell cell = null;
			String imgName;
			switch(type) {
			case 0:
				// inert cell
				cell = new GameCell(	name,
										pos		);
				break;
			case 1:
				cell = new ChanceCell(	name,
										pos	);
				// chance
				break;
			case 2:
				// go to prison
				cell = new ActionCell ( name,
										pos,
										new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												Logic logic = Logic.getInstance();
												Player player = (Player) e.getSource();
												logic.sendToPrison(player);
											}
										});
				break;
			case 3:
				// transaction cell
				int amount = Integer.parseInt(cellInfo.get(4));
				cell = new ActionCell ( name,
										pos,
										new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												Player player = (Player) e.getSource();
												player.accountTransfer(amount);
											}
										});
				break;
			case 4:
				// territory
				assert(cellInfo.size() >= 8);
				int [] additionalFees = null;
				if( cellInfo.size() >= 9 ) {
					additionalFees = new int[cellInfo.size()-8];
					for(int i = 0 ; i < additionalFees.length; i++)
						additionalFees[i] = Integer.parseInt(cellInfo.get(i+8));
				}
				imgName = cellInfo.get(3);
				cell = new Territory(	name,
										LocalResources.territoriesFolder + imgName + ".jpg",
										pos,
										Integer.parseInt(cellInfo.get(4)),
										Integer.parseInt(cellInfo.get(5)),
										Integer.parseInt(cellInfo.get(6)),
										Integer.parseInt(cellInfo.get(7)),
										additionalFees	);
				break;
			case 5:
				// service
				assert(cellInfo.size() == 6);
				imgName = cellInfo.get(3);
				cell = new Service(	name,
									LocalResources.servicesFolder + imgName + ".jpg",
									pos,
									Integer.parseInt(cellInfo.get(4)),
									Integer.parseInt(cellInfo.get(5))	);
				break;
			}
			cells[pos] = cell;
		}
	}
	
	public void clickedOnCell(int pos) {
		if( getFrame() == null ) return;
		AbstractCell clickedCell = cells[pos];
		if( ! (clickedCell instanceof OwnableCell) ) return;
		OwnableCell ownableCell = (OwnableCell) clickedCell;
		Image cellImg = ownableCell.getCardImage();
		String cellName = ownableCell.getName();
		new ImageDialog(getFrame(), cellName, cellImg);
	}
	
	/**
	 * @param cellName - cell name detailed in the CSV file
	 * @return cell object with such name or {@code null} if search was unsuccessful
	 * @see #getCurrentPlayerCellsNames()
	 */
	public AbstractCell getCellByName( String cellName ) {
		if( cellName == null ) return null;
		for(int i = 0 ; i < cells.length; i++)
		{
			if( cellName.equals(cells[i].getName()) )
				return cells[i];
		}
		return null;
	}
	
	/**
	 * @return array of cells owned by current player
	 */
	public ArrayList<OwnableCell> getPlayerCells(Player player) {
		ArrayList<OwnableCell> playerCells = new ArrayList<OwnableCell>();
		for(int i = 0 ; i < numOfCells; i++) {
			AbstractCell cell = cells[i];
			if( cell instanceof OwnableCell ) {
				OwnableCell ownableCell = (OwnableCell) cell;
				if( player == ownableCell.getOwner() ) {
					playerCells.add(ownableCell);
				}
			}
		}
		return playerCells;
	}
	
	public ArrayList<OwnableCell> getCurrentPlayerCells()
	{
		return getPlayerCells(getCurrentPlayer());
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
		Player player = players.get(turn);
		if( player.isInPrison() )
		{
			Frame outputFrame = getFrame();
			if( dice.gotEqualSidesUp() )
			{
				player.setInPrison(false);
				if( outputFrame != null )
				JOptionPane.showMessageDialog(	outputFrame, "Parabéns! Você conseguiu sair da prisão!",
												"Prisão", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				player.updateRoundsInPrisonCounter();
				if( outputFrame != null )
				JOptionPane.showMessageDialog(	outputFrame, "Não foi dessa vez... Tente na próxima rodada",
												"Prisão", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			int oldPos = player.getPos();
			int diceSum = dice.getLastRollSum();
			int newPos = (oldPos + diceSum)%numOfCells;
			if( oldPos > newPos ) player.accountTransfer(200); // starting point bonus
			player.setPos(newPos);
			AbstractCell currentCell = cells[newPos];
			int money = currentCell.charge(player, diceSum);
			player.accountTransfer(-money);
		}
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
		checkDebt();
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
	 * <p>Ends game even if there is still more than one player on the board
	 * <p>Sums up all fortunes and list the players in a podium displayed on screen
	 */
	public void endGame() {
		int initialPodiumSize = podium.size();
		ArrayList<Player> auxPlayerList = new ArrayList<Player>(players);
		while( !auxPlayerList.isEmpty() )
		{
			Player poorer = auxPlayerList.get(0);
			long smallest_fortune = getPlayerFortuneSum(poorer);
			for( Player player : auxPlayerList )
			{
				long fortune = getPlayerFortuneSum(player);
				if( fortune < smallest_fortune )
				{
					poorer = player;
					smallest_fortune = fortune;
				}
			}
			podium.add(poorer);
			auxPlayerList.remove(poorer);
		}
		showPodium();
		int currentPodiumSize = podium.size();
		while( currentPodiumSize > initialPodiumSize )
		{
			podium.remove( currentPodiumSize - 1 );
			currentPodiumSize = podium.size();
		}
	}
	
	/**
	 * <p>Cycles the turn counter to the next player
	 */
	private void nextTurn() {
		turn += 1;
		updateTurn();
	}
	
	/**
	 * <p>Caps turn counter to current number of players.
	 * It is useful when removing players and making sure
	 * the turn counter is within the boundaries of the
	 * current number of players count (that has been
	 * recently updated).
	 */
	private void updateTurn() { 
		turn %= getNumPlayers();
	}
	
	/**
	 * Sends player to prison
	 * @param player
	 */
	private void sendToPrison(Player player) {
		player.setInPrison(true);
		player.setPos(9);
	}
	
	/**
	 * <p>Checks if the current turn's player is on debt
	 * with the bank. If so, it will prompt a dialog
	 * that will oblige him to sell as many properties
	 * as necessary to be on green again. If the sum
	 * of all the players' fortunes isn't enough to
	 * pay the debt, it will be automatically removed
	 * from the player pool.
	 */
	private void checkDebt() {
		Player player = getCurrentPlayer();
		if( !player.isBroke() ) return; // isn't broke
		long totalWorth = getPlayerFortuneSum(player);
		Frame outputFrame = getFrame();
		if( totalWorth < 0 )
		{
			// can't pay
			if( outputFrame != null )
			{
				JOptionPane.showMessageDialog( outputFrame,
				String.format("O jogador %s faliu. Seu patrimônio foi vendido e poderá ser comprado pelos seus adversários.",getCurrentPlayerColorName()),
				"Falência", JOptionPane.WARNING_MESSAGE);
			}
			removeCurrentPlayer();
		}
		else
		{
			// can pay
			if( outputFrame != null )
			{
				// ask which properties are to be sold
				JOptionPane.showMessageDialog( outputFrame,
				String.format("O jogador %s está devendo $ %d ao banco. Preste suas contas com o banco, vendendo propriedades!",getCurrentPlayerColorName(),-player.getBankAcc()),
				"Dívidas", JOptionPane.WARNING_MESSAGE);
				PropertyDialog dlg = new PropertyDialog(getFrame(), false);
				dlg.setVisible(true);
			}
			else
			{
				// sell as few properties as possible to pay debt
				ArrayList<OwnableCell> cells = getCurrentPlayerCells();
				for( OwnableCell cell : cells )
				{
					if( !player.isBroke() ) break;
					System.out.printf("Jogador %s vendeu %s por $ %d.\n",
					player.getColorName(),cell.getName(),cell.getWorthValue());
					cell.sell();
				}
			}
		}
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
		if( !steppingCell.isOwnable() ) return false; // game cell!
		OwnableCell ownableCell = (OwnableCell) steppingCell;
		if( ownableCell.getOwner() != getCurrentPlayer() ) return false; // not your cell!
		return ownableCell.canUpgrade(); // can the current player upgrade it?
	}
	
	/* *************
	 * GUI CALLBACKS
	 * ************* */
	
	/**
	 * Sets frame from which dialogs can be constructed.
	 * @param frame - a Frame that is visible to the user and
	 * which it can be interacted with
	 * @see #getFrame()
	 */
	public void setFrame( Frame frame ) { this.frame = frame; }
	
	/**
	 * <p>Assures there is a GUI frame that the user can
	 * interact with. If there isn't, that is, if this
	 * function returns {@code null}, then the user cannot
	 * interact with any GUI whatsoever. Therefore, some
	 * decisions have to be made automatically.  
	 * @return frame for visual call backs
	 * @see #setFrame(Frame)
	 */
	public Frame getFrame() { return frame; }
	
	public void saveStateToFile(File f) {
		ArrayList<Player> cellsOwners = new ArrayList<Player>();
		ArrayList<Integer> cellsLevels = new ArrayList<Integer>();
		for( AbstractCell cell : cells ) {
			if( cell instanceof OwnableCell ) {	
				OwnableCell ownableCell = (OwnableCell)cell;
				cellsOwners.add(ownableCell.getOwner());
				Integer lvl = Integer.valueOf(ownableCell.getUpgradeLevel());
				cellsLevels.add(lvl);
			}
		}
		StateManager s = new StateManager(
				getNumPlayers(),
				turn,
				players,
				dice,
				deck,
				cellsOwners,
				cellsLevels
		);
		s.writeProperties(f);
	}
	
	@SuppressWarnings("static-access")
	public void loadStateFromFile(File f) {
		StateManager sm;
		try {
			sm = StateManager.loadProperties(f);
			setNumOfPlayers(sm.numPlayers);
			turn = sm.turn;
			players = sm.players;
			
			// loading deque state
			int cardOwnerId = Integer.parseInt(sm.cardOwner);
			ChanceCard escapeCard = null;
			String escapeCardPath = "resources\\sprites\\sorteReves\\sorte03.jpg";
			
			// Take escape card from players 
			for( Player player : players )
			{
				if( player.hasCard() )
					escapeCard = player.takeCard();
			}
			
			// If the escape card belongs to a player, give it to him
			// If the card is in the deck, take from it first
			if( cardOwnerId != -1 )
			{
				if( escapeCard == null )
				{
					while( !(escapeCard=deck.pollFirst()).getImagePath().equals(escapeCardPath) )
					{
						deck.offerLast(escapeCard);
					}
				}
				players.get(cardOwnerId).giveCard(escapeCard);
			}
			
			// Make the top cards match
			String topCardPath = sm.cardImgPath;
			while( !deck.peekFirst().getImagePath().equals(topCardPath) ) {
				deck.offerLast(deck.pollFirst());
			}
			
			// loading cells states
			int auxIndex = 0;
			for( int i = 0; i < numOfCells; i++)
			{
				if( cells[i] instanceof OwnableCell)
				{
					OwnableCell ownableCell = (OwnableCell) cells[i];
					String cellOwnerColorName = sm.cellsOwners.get(auxIndex);
					if( !cellOwnerColorName.equals("0") )
					{
						for( Player p : players )
						{
							if( p.getColorName().equals(cellOwnerColorName) )
							{
								ownableCell.setOwner(p);
								break;
							}
						}
						if ( cells[i] instanceof Territory )
						{
							Territory territory = (Territory) ownableCell; 
							int upgradeLevel = Integer.parseInt(sm.cellsLevels.get(auxIndex));
							territory.setUpgradeLevel(upgradeLevel);
						}
					}
					auxIndex++;
				}
			}
		} catch (Exception e) {
			if( getFrame() != null )
			JOptionPane.showMessageDialog(getFrame(), "O arquivo pode estar corrompido.", "Erro ao carregar arquivo", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}

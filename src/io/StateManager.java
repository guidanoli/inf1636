package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Properties;

import game.ChanceCard;
import game.Dice;
import game.Logic;
import game.Player;



/**
 * State class is used to read or write
 * properties files values and stores them
 * in memory.
 *
 */
public class StateManager extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	public int numPlayers;
	public int turn;
	public static ArrayList<Player> players;
	
	public Dice dice;
	public int[] lastRoll;
	
	public Deque<ChanceCard> deck;
	public String cardImgPath;
	public String cardOwner;
	
	public final int numOfCells = 36; 
	public static ArrayList<Player> cellsOwners;
	public static ArrayList<Integer> cellsLevels;
	
	private StateManager() {
		
	}
	
	@SuppressWarnings("static-access")
	public StateManager(int numPlayers, int turn,
						ArrayList<Player> players, Dice dice, Deque<ChanceCard> deck,
						ArrayList<Player> cellsOwners1, ArrayList<Integer> cellsLevels1) {
		this.numPlayers = numPlayers;
		this.turn = turn;
		this.players = players;
		this.dice = dice;
		this.deck = deck;
		this.cellsLevels = cellsLevels1;
		this.cellsOwners = cellsOwners1;
	}
	
	public void writeProperties(File f) {
		 try (OutputStream output = new FileOutputStream(f.getAbsolutePath())) {
	            Properties prop = new Properties();
	            int escapeCardOwner = -1;
	            // General game state properties values
	            prop.setProperty("logic.numPlayers", String.format("%d", numPlayers));
	            prop.setProperty("logic.turn",String.format("%d", turn ));
	            // Dice state properties values
	            prop.setProperty("dice.sideup.0", String.format("%d", dice.getLastRolls()[0]));
	            prop.setProperty("dice.sideup.1", String.format("%d", dice.getLastRolls()[1]));
	            // Each player properties values
	            for (int i = 0; i < players.size(); i++) {
	            	prop.setProperty(String.format("player.%d.pos", i), String.format("%d", players.get(i).getPos()));
	            	prop.setProperty(String.format("player.%d.bankacc", i), String.format("%d", players.get(i).getBankAcc()));
	            	prop.setProperty(String.format("player.%d.color", i), players.get(i).getColorName());
	            	prop.setProperty(String.format("player.%d.roundsinprisonleft", i),  String.format("%d",  players.get(i).getRoundsInPrisonLeft()));
	            	if( players.get(i).hasCard() ) {
	            		escapeCardOwner = i;
	            	}
	            }
	            // Deque state properties values
	            prop.setProperty("deck.first",deck.getFirst().getImagePath());
	            prop.setProperty("deck.escapecardowner", String.format("%d", escapeCardOwner));
	            
	            // cells state properties values
	            for( int i = 0; i < cellsOwners.size(); i++ ) {
	            	if( cellsOwners.get(i) == null ) {
	            		prop.setProperty(String.format("ownablecell.%d.owner", i), "null");
	            	} else {
	            		prop.setProperty(String.format("ownablecell.%d.owner", i), String.format("%d", cellsOwners.get(i).getColor().getRGB()));
	            	}
		            prop.setProperty(String.format("ownablecell.%d.upgradelevel", i), cellsLevels.get(i).toString());
	            }
	            // Save properties
	            prop.store(output, null);

	            System.out.println(prop);
		 } catch (IOException io) {
	            io.printStackTrace();
		 }
	}
	
	@SuppressWarnings("static-access")
	public static StateManager loadProperties(File f) throws IOException {
		StateManager sm = new StateManager();
		FileReader reader=new FileReader(f.getAbsolutePath());
		Properties prop = new Properties();
		prop.load(reader);
		// logic values
        sm.numPlayers = Integer.parseInt(prop.getProperty("logic.numPlayers"));
        sm.turn = Integer.parseInt(prop.getProperty("logic.turn"));
	    // card values
	    sm.cardImgPath = prop.getProperty("deck.first");
	    sm.cardOwner = prop.getProperty("deck.escapecardowner");
	        
	    // player value
	    sm.players = new ArrayList<Player>();
	    for (int i = 0; i < sm.numPlayers; i++) {
	    	Player p = new Player(Logic.getPlayerColorIds()[i], prop.getProperty(String.format("player.%d.color", i)));
            p.setPos(Integer.parseInt(prop.getProperty(String.format("player.%d.pos", i))));
            p.setBankAcc((Integer.parseInt(prop.getProperty(String.format("player.%d.bankacc", i)))));
            p.setInPrisonFor( Integer.parseInt(prop.getProperty(String.format("player.%d.roundsinprisonleft", i))));
            if( p.getColorName() == sm.cardOwner ) {
            	p.giveCard( new ChanceCard( sm.cardImgPath, null));
            }
            if( p != null ) {
            	sm.players.add(p);
            }
		}
        // cell values
	    sm.cellsOwners = new ArrayList<Player>();
	    sm.cellsLevels = new ArrayList<Integer>();
        for (int i = 0; i < sm.cellsOwners.size(); i++) {
        	sm.cellsOwners.add( new Player((prop.getProperty(String.format("player.%d.color", i)))));
        	sm.cellsLevels.add(Integer.parseInt(prop.getProperty(String.format("ownablecell.%d.upgradelevel", i))));
		}
                    
           // dice values
        sm.lastRoll = new int[2];
        sm.lastRoll[0] = Integer.parseInt(prop.getProperty("dice.sideup.0"));
        sm.lastRoll[1] = Integer.parseInt(prop.getProperty("dice.sideup.1"));       
        return sm;
	}
}

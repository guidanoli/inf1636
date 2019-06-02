package gui;
import javax.swing.*;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import game.Logic;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements MouseListener {
	
	/* *********
	 * VARIABLES
	 * ********* */
		
	// parent frame
	MainFrame frame;
		
	// logic
	Logic logic = Logic.getInstance();
	
	// swing components
	private JButton rollBtn = new JButton("Rolar dados");
	private JButton endTurnBtn = new JButton("Terminar turno");
	private JButton buyBtn = new JButton("Comprar");
	private JButton showDeckBtn = new JButton("Mostrar deque");
	private JButton showBalanceBtn = new JButton("Mostrar saldo");
	JButton [] btnGrid = {
			rollBtn ,
			buyBtn ,
			showDeckBtn ,
			showBalanceBtn ,
			endTurnBtn
	};
	
	// graphical components
	private ImgList l;
	private Image bgimg;
	private Image dice1, dice2;
	private ArrayList<Image> playersimg = new ArrayList<Image>();

	// panel mouse listener and image bounds
	private PanelMouseListener listener = new PanelMouseListener();
	private Rectangle dice1_ret = new Rectangle(320, 780, 70, 70);
	private Rectangle dice2_ret = new Rectangle(400, 780, 70, 70);
	private Rectangle bgimg_ret = new Rectangle(1000,1000);
	
	// board measures
	private final int longMeasure = 121;
	private final int shortMeasure = 94;
	private final int numOfCells = 36;
	
	public MainPanel( MainFrame frame, int numOfPlayers )
	{
		super();
		this.frame = frame;
		this.logic.setNumOfPlayers(numOfPlayers);
		l = new ImgList();
		addMouseListener(listener);
		setBackground(Color.WHITE);
		loadSprites("sprites");
		storeSprites();
		setAreaListeners();
		addComponents();
		repaint();
	}
	
	/* **********
	 * COMPONENTS
	 * ********** */
	
	private void addComponents()
	{
		for(int i = 0 ; i < btnGrid.length; i++) {
			JButton btn = btnGrid[i];
			btn.setBounds(150, 830-40*i, 150, 30);
			btn.addMouseListener(this);
			add(btn);
		}
	}
	
	/* **************
	 * AREA LISTENERS
	 * ************** */
	
	private void setAreaListeners()
	{
		
	}
	
	/* ****
	 * DICE
	 * **** */
	
	private void UpdateDice()
	{
		int [] rollResult = logic.dice.getLastRolls();
		dice1 = l.getImg(String.format("sprites_dados_die_face_%d",rollResult[0]));
		dice2 = l.getImg(String.format("sprites_dados_die_face_%d",rollResult[1]));
	}
	
	/* ******************
	 * PAINTING FUNCTIONS
	 * ****************** */
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		paintBoard(g);
		paintDice(g);
		paintPlayers(g);
	}
	
	private void paintGameImage(Graphics g, Image i, Rectangle r)
	{
		g.drawImage(i, r.x, r.y, r.width+r.x, r.height+r.y,
					0, 0, i.getWidth(this), i.getHeight(this), this);
	}
	
	private void paintDice(Graphics g)
	{
		int margin = 10;
		g.setColor(logic.getCurrentPlayerColor());
		g.fillRect(
			(int) (dice1_ret.getMinX() - margin),
			(int) (dice1_ret.getMinY() - margin),
			(int) (dice2_ret.getMaxX() - dice1_ret.getMinX() + 2*margin),
			(int) (dice2_ret.getMaxY() - dice1_ret.getMinY() + 2*margin)
		); // player turn indicator
		paintGameImage(g,dice1,dice1_ret);
		paintGameImage(g,dice2,dice2_ret);
	}
	
	private void paintPlayers(Graphics g) 
	{
		int pId = 0;
		for( Integer pos : logic.getPlayersPos()) {
			Rectangle rect = getPlayerRect(pos, pId);
			Image i = playersimg.get(pId);
			paintGameImage(g, i , rect);
			pId++;
		}
	}
	
	private void paintBoard(Graphics g)
	{
		paintGameImage(g,bgimg,bgimg_ret);
	}
	
	/* ***************
	 * SPRITES LOADING
	 * *************** */
	
	private void storeSprites()
	{
		bgimg = l.getImg("sprites_tabuleiroRJ");
		UpdateDice();
		for( int pId = 0; pId < this.logic.getNumPlayers(); pId++ )
			playersimg.add(l.getImg(String.format("sprites_pinos_pin%d", pId)));
	}
	
	private void loadSprites(String spritesDir)
	{
		File dir = new File(spritesDir);
		try (Stream<Path> paths = Files.walk(Paths.get(dir.getPath()))) {
			paths
				.filter(f -> Files.isRegularFile(f) && accept(f.toString()))
				.forEach(this::addImgThroughPath);
		} catch (IOException e) { System.out.println(e.getMessage()); }
	}
	
	private void addImgThroughPath(Path path) {
		l.addImg(path.toString());
		System.out.printf("Image '%s' added.\n",path.toString());
	}
	
	private boolean accept(String name) {
		String [] extensions = {"jpg","png"};
		for( final String ext : extensions )
			if( name.endsWith("."+ext) )
				return true;
		return false;
	}
	
	/* ****************************
	 * BOARD HOUSE POSITION MAPPING
	 * **************************** */
	
	private Rectangle getPlayerRect(int pos, int pId) {
		Rectangle cellOffset = getCellOffset(pos);
		Rectangle playerOffset = getPlayerOffset(pos,pId);
		Rectangle rect = new Rectangle(20,30);
		rect.setLocation(cellOffset.x + playerOffset.x - rect.width/2 , cellOffset.y + playerOffset.y - rect.height);
		return rect;
	}
	
	private Rectangle getCellOffset(int pos) {
		int x = 7, y = 7;
		int [] const_coord = {7,7,871,871};
		double adjustedShortMeasure = shortMeasure + 2.5; /* accounts for borders */
		int side = ( pos % numOfCells ) / 9;
		int posInLine = pos % 9;
		switch(side)
		{
		case 0:
			x = const_coord[side];
			if( posInLine != 0 ) y += longMeasure + (8-posInLine)*adjustedShortMeasure;
			else y = const_coord[(side+3)%4];
			break;
		case 1:
			y = const_coord[side];
			if( posInLine != 0 ) x += longMeasure + (posInLine-1)*adjustedShortMeasure;
			else x = const_coord[(side+3)%4];
			break;
		case 2:
			x = const_coord[side];
			if( posInLine != 0 ) y += longMeasure + (posInLine-1)*adjustedShortMeasure;
			else y = const_coord[(side+3)%4];
			break;
		case 3:
			y = const_coord[side];
			if( posInLine != 0 ) x += longMeasure + (8-posInLine)*adjustedShortMeasure;
			else x = const_coord[(side+3)%4];
			break;
		}
		return new Rectangle(x,y,0,0);
	}
	
	private Rectangle getPlayerOffset(int pos, int pId) {
		Rectangle cellRect = getCellFormatFromPos(pos);
		int row = pId % 3 + 1;
		int col = pId / 3 + 1;
		int row_size = cellRect.width / 4;
		int col_size = cellRect.height / 3;
		int width = (row)*row_size;
		int height = (col)*col_size;
		return new Rectangle(width,height,0,0);
	}
	
	private Rectangle getCellFormatFromPos(int pos) {
		if( pos % 9 == 0 )
			return new Rectangle(longMeasure,longMeasure); /* corner */
		int side = pos / 9;
		if( side % 2 == 0 )
			return new Rectangle(longMeasure,shortMeasure); /* left & right */
		else
			return new Rectangle(shortMeasure,longMeasure); /* upper & lower */
	}

	/* ***************
	 * MOUSE LISTENERS
	 * *************** */
	
	public void mouseClicked(MouseEvent e) {
		
		/* if source is an inactive button, ignore */
		if( e.getSource() instanceof JButton  &&
			((JButton) e.getSource()).isEnabled() )
		{
			if( e.getSource() == rollBtn )
				rollBtnAction();
		}
	}

	private void rollBtnAction()
	{
		logic.roll();
		UpdateDice();
		repaint();
	}
	
	// Unimplemented methods
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	
	/* END */
	
}

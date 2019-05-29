package gui;
import javax.swing.*;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import game.Logic;
import img.ImgList;

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
		
	// frame
	MainFrame frame;
		
	// logic
	Logic logic;

	// listener
	private PanelMouseListener listener = new PanelMouseListener();
	
	// swing components
	private JButton rollBtn = new JButton("Rolar dados");
	private JButton nextBtn = new JButton("Próxima mensagem");
	private JButton endTurnBtn = new JButton("Encerrar turno");
	
	// graphical components
	private String toast = new String("");
	private Color toastColor = Color.BLACK;
	private ImgList l;
	private Image bgimg;
	private Image dice1, dice2;
	private ArrayList<Image> playersimg = new ArrayList<Image>();

	// images borders
	private Rectangle dice1_ret = new Rectangle(150, 687, 70, 70);
	private Rectangle dice2_ret = new Rectangle(240, 687, 70, 70);
	private Rectangle bgimg_ret = new Rectangle(1000,1000);
	
	// board measures
	private final int longMeasure = 121;
	private final int shortMeasure = 94;
	private final int numOfCells = 36;
	
	public MainPanel( MainFrame frame, int numOfPlayers )
	{
		super();
		logic = new Logic(numOfPlayers);
		this.frame = frame;
		l = new ImgList();
		addMouseListener(listener);
		setBackground(Color.WHITE);
		loadSprites("sprites");
		storeSprites();
		setAreaListeners();
		addComponents();
		setComponentsListeners();
		repaint();
	}
	
	/* **********
	 * COMPONENTS
	 * ********** */
	
	private void addComponents()
	{
		rollBtn.setBounds(150, 780, 150, 30);
		nextBtn.setBounds(310, 780, 150, 30);
		endTurnBtn.setBounds(470, 780, 150, 30);
		add(rollBtn);
		add(nextBtn);
		add(endTurnBtn);
	}
	
	private void setComponentsListeners()
	{
		rollBtn.addMouseListener(this);
		nextBtn.addMouseListener(this);
		endTurnBtn.addMouseListener(this);
	}
	
	/* **************
	 * AREA LISTENERS
	 * ************** */
	
	public void setAreaListeners()
	{
		
	}
	
	/* *****
	 * TOAST
	 * ***** */
	
	public void setToast(String msg, Color color) {
		toastColor = color;
		toast = msg;
	}
	
	public void updateToast() {
		if( getToast() == "" )
		{
			String t = logic.nextToast();
			if( t != null )
				setToast(t,logic.getCurrentPlayerColor());
		}
	}
	
	public void setToast(String msg) { setToast(msg,toastColor); }
	public String getToast() { return toast; }
	
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
		paintToast(g);
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
		
	private void paintToast(Graphics g)
	{
		g.setFont(new Font("Verdana", Font.BOLD, 24));
		g.setColor(toastColor.darker());
		g.drawString(toast, 144, 846);
		g.setColor(toastColor);
		g.drawString(toast, 145, 845);
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
			else y = const_coord[(posInLine+3)%4];
			break;
		case 1:
			y = const_coord[side];
			if( posInLine != 0 ) x += longMeasure + (posInLine-1)*adjustedShortMeasure;
			else x = const_coord[(posInLine+3)%4];
			break;
		case 2:
			x = const_coord[side];
			if( posInLine != 0 ) y += longMeasure + (posInLine-1)*adjustedShortMeasure;
			else y = const_coord[(posInLine+3)%4];
			break;
		case 3:
			y = const_coord[side];
			if( posInLine != 0 ) x += longMeasure + (8-posInLine)*adjustedShortMeasure;
			else x = const_coord[(posInLine+3)%4];
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
		
		/* if source is a inactive button, ignore */
		if( e.getSource() instanceof JButton  &&
			((JButton) e.getSource()).isEnabled() )
		{
			if( e.getSource() == rollBtn )
				rollBtnAction();
			else if( e.getSource() == nextBtn )
				toastBtnAction();
			else if( e.getSource() == endTurnBtn )
				endTurnBtnAction();
			updateToast();
		}
	}

	public void rollBtnAction()
	{
		// Logic
		logic.roll();
		logic.nextState();
		// GUI
		rollBtn.setEnabled(false);
		nextBtn.setEnabled(true);
		endTurnBtn.setEnabled(true);
		toastBtnAction();
		UpdateDice();
		repaint();
	}
	
	public void toastBtnAction()
	{
		String current_toast = logic.nextToast();
		// Logic
		if( current_toast != null )
		{
			updateToast();
			if(!logic.isToastEmpty())
				return;
		}
		logic.nextState();
		// GUI
		nextBtn.setEnabled(false);
	}
	
	public void endTurnBtnAction()
	{
		// Logic
		logic.nextState();
		// GUI
		rollBtn.setEnabled(true);
		nextBtn.setEnabled(false);
		endTurnBtn.setEnabled(false);
		setToast("");
	}
	
	// Unimplemented methods
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	
	/* END */
	
}

package gui;
import javax.swing.*;

import java.awt.Rectangle;
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
public class MainPanel extends JPanel {
	
	// frame
	MainFrame frame;
	
	// components
	JLabel toast = new JLabel("toast");
	
	// logic
	Logic logic;
	
	// image list
	private ImgList l;
	
	// images
	private Image bgimg;
	private Image dice;
	private ArrayList<Image> playersimg = new ArrayList<Image>();
		
	// board cell measures
	private final int longMeasure = 121;
	private final int shortMeasure = 94;
	private final int numOfCells = 36;
	
	// images borders
	private Rectangle dice_ret = new Rectangle(220, 650, 100, 100);
	private Rectangle bgimg_ret = new Rectangle(1000,1000);
	
	// listener
	private PanelMouseListener listener = new PanelMouseListener();
	
	public MainPanel( MainFrame frame, int numOfPlayers )
	{
		super();
		logic = new Logic(numOfPlayers);
		this.frame = frame;
		l = new ImgList();
		loadSprites("sprites");
		bgimg = l.getImg("sprites_tabuleiroRJ");
		dice = l.getImg(String.format("sprites_dados_die_face_%d",this.logic.getLastRoll()));
		for( int pId = 0; pId < this.logic.getNumPlayers(); pId++ ) {
			playersimg.add(l.getImg(String.format("sprites_pinos_pin%d", pId)));
		}
		addMouseListener(listener);
		setBackground(Color.WHITE);
		setObjectListeners();
		repaint();
	}
	
	public void setObjectListeners()
	{
		listener.addArea(dice_ret, new ObjectClickListener() {
			public void action() {
				logic.roll();
				dice = l.getImg(String.format("sprites_dados_die_face_%d",logic.getLastRoll()));
				repaint();
			}
		});
	}
	
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
		paintGameImage(g,dice,dice_ret);
	}
	
	private void paintPlayers(Graphics g) 
	{
		int pId = 0;
		for( Integer pos : logic.getPlayersPos()) {
//			Rectangle rect = posAsSquare(pos, pId);
			Rectangle rect = getPlayerRect(pos, pId);
			Image i = playersimg.get(pId);
			paintGameImage(g, i , rect);
			pId++;
		}
	}
		
	private void paintToast(Graphics g)
	{
		g.setFont(new Font("Verdana", Font.BOLD, 24));
		g.setColor(Color.RED);
		g.drawString("ALGO DO TIPO SUA VEZ", 145, 845);
	}
	
	private void paintBoard(Graphics g)
	{
		paintGameImage(g,bgimg,bgimg_ret);
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
	
	private Rectangle posAsSquare(int pos, int pId) {
		Rectangle rect =  new Rectangle();
		int coeficiente = 10;
		if( pId > 3) pId = pId -3;
		if(  0 <= pos && pos <= 9 ) { // left houses
			rect.setBounds( 15 + pId*coeficiente , 880 - pos*92, 20, 30 );
		} else if(  10 <= pos && pos <= 18 ) { // upper houses
			pos = pos-10;
			rect.setBounds( 140 + pos*92 , 45, 20, 30);
		} else if(  19 <= pos && pos <= 28 ) { // right houses
			pos = pos-20;
			rect.setBounds( 880 - pId*coeficiente , 40 + pos*92 + 30, 20, 30);
		} else if (  29 <= pos && pos <= 36 ) { // lower houses
			pos = pos-30;
			rect.setBounds( 788 - pos*92 , 880 - pId*coeficiente + 30, 20, 30);
		} else {
			// error
		}
		return rect;
	}
	
	public void rollDice()
	{
		
	}
	
}

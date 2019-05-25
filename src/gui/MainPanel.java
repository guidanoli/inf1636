package gui;
import javax.swing.*;

import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;


import game.Logic;
import game.Player;
import img.ImgList;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	// frame
	MainFrame frame;
	
	// components
	JLabel toast = new JLabel("toast");
	
	// logic
	Logic logic;;
	
	// image list
	private ImgList l;
	
	// images
	private Image bgimg;
	private Image dice;
	private ArrayList<Image> playersimg = new ArrayList<Image>();
	
	
	// images borders
	private Rectangle dice_ret = new Rectangle(220, 650, 100, 100);
	private Rectangle bgimg_ret = new Rectangle(1000,1000);
	
	// listener
	private PanelMouseListener listener = new PanelMouseListener();
	
	public MainPanel(MainFrame frame, Logic logic)
	{
		super();
		this.frame = frame;
		this.logic = logic;
		l = new ImgList();
		loadSprites("sprites");
		bgimg = l.getImg("sprites_tabuleiroRJ");
		dice = l.getImg(String.format("sprites_dados_die_face_%d",this.logic.getLastRoll()));
		for( int pId = 1; pId <= this.logic.getNumPlayers(); pId++ ) {
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
			Rectangle rect = posAsSquare(pos, pId);
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
	
	private Rectangle posAsSquare(int pos, int pId) {
		Rectangle rect =  new Rectangle();
		int coeficiente = 10;
		if( pId > 3) pId = pId -3;
		if(  0 <= pos && pos <= 9 ) { // se estiver na coluna da esquerda
			rect.setBounds( 15 + pId*coeficiente , 880 - pos*92, 20, 30 );
		} else if(  10 <= pos && pos <= 18 ) { // se estiver na linha de cima
			pos = pos-10;
			rect.setBounds( 140 + pos*92 , 45, 20, 30);
		} else if(  19 <= pos && pos <= 28 ) { // se estiver na coluna da direita
			pos = pos-20;
			rect.setBounds( 880 - pId*coeficiente , 40 + pos*92 + 30, 20, 30);
		} else if (  29 <= pos && pos <= 36 ) { // se estiver na linha de baixo
			pos = pos-30;
			rect.setBounds( 788 - pos*92 , 880 - pId*coeficiente + 30, 20, 30);
		} else {
			// posição com erro.
		}
		return rect;
	}
	
	public void rollDice()
	{
		
	}
	
}

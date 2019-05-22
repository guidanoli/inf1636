package gui;
import javax.swing.*;

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
	Logic logic = new Logic(this);
	
	// image list
	private ImgList l;
	
	// images
	private Image bgimg;
	private Image dice;
	
	// images borders
	private Rectangle dice_ret = new Rectangle(220, 650, 100, 100);
	private Rectangle bgimg_ret = new Rectangle(1000,1000);
	
	// listener
	private PanelMouseListener listener = new PanelMouseListener();
	
	public MainPanel(MainFrame frame)
	{
		super();
		this.frame = frame;
		l = new ImgList();
		loadSprites("sprites");
		bgimg = l.getImg("sprites_tabuleiroRJ");
		dice = l.getImg(String.format("sprites_dados_die_face_%d",logic.getLastRoll()));
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
	
	public void rollDice()
	{
		
	}
	
}

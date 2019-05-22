package gui;
import javax.swing.*;

import game.Logic;
import img.ImgList;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	// frame
	MainFrame frame;
	
	// logic
	Logic logic = new Logic(this);
	
	// image list
	private ImgList l;
	
	// images
	private Image bgimg;
	
	public MainPanel(MainFrame frame)
	{
		super();
		this.frame = frame;
		l = new ImgList();
		loadSprites("sprites");
		bgimg = l.getImg("sprites_tabuleiroRJ");
		addMouseListener(new MyMouseListener());
		setLayout(new FlowLayout());
		setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(bgimg, 0, 0, this.getWidth(), this.getHeight(), 0, 0, bgimg.getWidth(null), bgimg.getHeight(null), null);
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

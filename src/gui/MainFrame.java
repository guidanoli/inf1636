package gui;
import javax.swing.*;

import game.Logic;

import java.awt.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// size parameters
	public final int DEF_H = 1000;
	public final int DEF_W = 1000;
	public final int H_INC = 27;
	public final int W_INC = 0;
	//public final int W_INC = 16; -- if resizable = true
	
	// containers
	JPanel p;
		
	// components
	
	// managers
	Logic logic = Logic.getInstance();
	
	// none yet --		
	public MainFrame(String name, int numOfPlayers, ImageIcon icon) {
		super(name);
		logic.setFrame(this);
		setIconImage(icon.getImage());
		setFrameSizeAndPos();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		p = new MainPanel(this, numOfPlayers);
		getContentPane().add(p);
		p.setLayout(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void setFrameSizeAndPos() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
			int x = (d.width - DEF_W - W_INC)/2;
		int y = (d.height - DEF_H - H_INC)/2;
		setBounds(x, y, DEF_W + W_INC, DEF_H + H_INC);
	}
	
}
package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// size parameters
	public final int DEF_H = 400;
	public final int DEF_W = 400;
	
	// containers
	JPanel p = new MainPanel();
	
	// components
	
	public MainFrame(String name) {
		// set frame name
		super(name);
		// set bounds in the middle
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int s_w = d.width;
		int s_h = d.height;
		int x = (s_w - DEF_W)/2;
		int y = (s_h - DEF_H)/2;
		setBounds(x,y,DEF_W,DEF_H);
		// exit on close
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// set panel
		p.setBackground(Color.WHITE);
		getContentPane().add(p);
	}

	public static void main(String[] args) {
		MainFrame painel = new MainFrame("Doge");
		painel.setVisible(true);
		//code
	}
	
}

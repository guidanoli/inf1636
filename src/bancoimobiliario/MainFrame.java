package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// size parameters
	public final int DEF_H = 600;
	public final int DEF_W = 800;
	
	// containers
	JPanel p = new MainPanel();
	
	// components
	JButton b1 = new JButton("Botão 1");
	JButton b2 = new JButton("Botão 2");
	
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
		p.add(b1);
		p.add(b2);
		p.setBackground(Color.WHITE);
		getContentPane().add(p);
	}

	public static void main(String[] args) {
		MainFrame painel = new MainFrame("Banco Imobiliário");
		painel.setVisible(true);
		//code
	}
	
}

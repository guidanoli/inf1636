package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// size parameters
	public final int DEF_H = 800;
	public final int DEF_W = 800;
	public final int H_INC = 27;
	public final int W_INC = 0;
	
	// containers
	JPanel p = new MainPanel(this);
	
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
		setBounds(x,y,DEF_W + W_INC,DEF_H + H_INC);
		// exit on close
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// set panel layout
		p.setLayout(new FlowLayout());
		// set bg color as WHITE
		p.setBackground(Color.WHITE);
		// add main panel to content pane
		getContentPane().add(p);
		// set look and feel
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Class not found.");
		}
		catch (UnsupportedLookAndFeelException e)
		{
			System.out.println("Unsupported look & feel.");
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		MainFrame painel = new MainFrame("Doge");
		painel.setVisible(true);
		//code
	}
	
}

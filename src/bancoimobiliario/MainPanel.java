package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
	
	public static final int x = 0;
	public static final int y = 10;
	
	public MainPanel()
	{
		super();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawString("Minha string", x, y);
	}
	
}

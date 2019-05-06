package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	public static final int text_x = 0;
	public static final int text_y = 10;
	
	public MainPanel()
	{
		super();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		ListaImagens l = new ListaImagens();
		l.novaImagem();
		Image i = l.getImagem()[0];
		g.drawImage(i, 0, 0, 400, 400, 0, 0, 100, 100, null);
	}
	
}

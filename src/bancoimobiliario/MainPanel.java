package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	public MainPanel()
	{
		super();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		ListaImagens l = new ListaImagens();
		Image i = l.novaImagem("sprites/img.png");
		g.drawImage(i, 0, 0, 400, 400, 0, 0, i.getWidth(null), i.getHeight(null), null);
	}
	
}

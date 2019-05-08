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
		Image bgimg = l.novaImagem("sprites/img.png"); // background image
		g.drawImage(bgimg, 0, 0, 400, 400, 0, 0, bgimg.getWidth(null), bgimg.getHeight(null), null);
	}
	
}

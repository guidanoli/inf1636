package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	// frame
	MainFrame frame;
	
	// image list
	private ListaImagens l = new ListaImagens();
	
	// images
	private Image bgimg;
	
	public MainPanel(MainFrame frame)
	{
		super();
		this.frame = frame;
		bgimg = l.novaImagem("sprites/BI.jpg");
		addMouseListener(new MyMouseListener());
		setLayout(new FlowLayout());
		setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int DEF_W = frame.getWidth() - frame.W_INC;
		int DEF_H = frame.getHeight() - frame.H_INC;
		g.drawImage(bgimg, 0, 0, DEF_W, DEF_H, 0, 0, bgimg.getWidth(null), bgimg.getHeight(null), null);
	}
	
}

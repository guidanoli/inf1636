package bancoimobiliario;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	// frame
	MainFrame frame;
	
	// image list
	private ImgList l;
	
	// images
	private Image bgimg;
	
	public MainPanel(MainFrame frame)
	{
		super();
		this.frame = frame;
		try {
			l = new ImgList(100);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		bgimg = l.addImg("sprites/tabuleiroRJ.jpg");
		addMouseListener(new MyMouseListener());
		setLayout(new FlowLayout());
		setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(bgimg, 0, 0, this.getWidth(), this.getHeight(), 0, 0, bgimg.getWidth(null), bgimg.getHeight(null), null);
	}
	
}

package bancoimobiliario;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

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
		g.drawString("Minha string", text_x, text_y);
		Graphics2D g2d = (Graphics2D) g;
		// draws a rectangle
		double leftX=100.0;
		double topY=100.0;
		double larg=200.0;
		double alt=150.0;
		Rectangle2D rt=new Rectangle2D.Double(leftX,topY,larg,alt);
		g2d.draw(rt);
		// draws an ellipsis inside the rectangle
		Ellipse2D e=new Ellipse2D.Double();
		e.setFrame(rt);
		g2d.draw(e);
		// draws the rectangle's diagonal
		Point2D p1=new Point2D.Double(leftX,topY);
		Point2D p2=new Point2D.Double(leftX+larg,topY+alt);
		g2d.draw(new Line2D.Double(p1,p2));
		// draws a centered circle
		double cX=rt.getCenterX();
		double cY=rt.getCenterY();
		double raio=150.0;
		Ellipse2D circ=new Ellipse2D.Double();
		circ.setFrameFromCenter(cX,cY,cX+raio,cY+raio);
		g2d.draw(circ);

	}
	
}

package bancoimobiliario;
import java.awt.event.*;
import javax.swing.*;

public class MyMouseListener implements MouseListener {

	private JButton b;
	
	public MyMouseListener( JButton button ) {
		// TODO Auto-generated constructor stub
		b = button;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if( isInsideButton(e.getX(),e.getY()) )
		{
			System.out.println("Clicked inside button '"+b.getText()+"'.");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isInsideButton( int x , int y ) {
		int bw = b.getWidth(), bh = b.getHeight();
		return x <= bw && y <= bh; 
	}

}

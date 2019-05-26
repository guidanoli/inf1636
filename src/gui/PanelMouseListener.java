package gui;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;

public class PanelMouseListener extends MouseAdapter {
	ArrayList<Rectangle> areas = new ArrayList<Rectangle>();
	ArrayList<AreaMouseListener> listeners = new ArrayList<AreaMouseListener>();
	public void addArea( Rectangle area , AreaMouseListener listener ) {
		areas.add(area);
		listeners.add(listener);
	}
	public void mouseClicked(MouseEvent e) {
		System.out.println(String.format("x = %d y = %d", e.getX(), e.getY()));
		for( Rectangle area : areas )
		{
			if( area.contains(e.getPoint()) )
			{
				int index = areas.indexOf(area);
				listeners.get(index).action();
			}
		}
	}
}

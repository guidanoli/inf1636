package gui;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 
 * <p>The {@code PanelMouseListener} class deals with interacting Rectangles called
 * Areas. Mouse events occurred inside a specific Area produce different outcomes depending 
 * on the <code>AreaMouseListener</code> linked to it.
 * 
 * <p>Areas can be added to the listener through the
 * {@link PanelMouseListener#addArea(Rectangle, AreaMouseListener)} function.
 * 
 * <p>Since Areas can overlap, multiple <code>AreaMouseListener</code> can be triggered on
 * the same event context. This does not add asynchronism due to the fact that the implementation
 * of the overwritten {@link java.awt.event.MouseListener MouseListener} functions checks for
 * every Area stored and checks if the mouseEvent comprehends its boundaries, then executes its
 * action. Thus, no concurrent events occur within this very function context. 
 * 
 * @author guidanoli
 *
 */
public class PanelMouseListener extends MouseAdapter {
	
	protected ArrayList<Rectangle> areas = new ArrayList<Rectangle>();
	protected ArrayList<AreaMouseListener> listeners = new ArrayList<AreaMouseListener>();
	
	/**
	 * <p>{@code public void addArea( Rectangle rectangle , AreaMouseListener listener )}
	 * <p>Register a Rectangle and a corresponding AreaMouseListener implementation
	 * to construct an area.
	 * <p><b>Disclaimer:</b> if one of the parameters is null, nothing is done.
	 * @param rectangle - rectangle object containing boundary information (x, y, width, height)
	 * @param listener - mouse event listener concerning said area
	 */
	public void addArea( Rectangle rectangle , AreaMouseListener listener ) {
		if( rectangle == null || listener == null ) return;
		areas.add(rectangle);
		listeners.add(listener);
	}
	
	public void mouseClicked(MouseEvent e) {
		System.out.println(String.format("x = %d y = %d", e.getX(), e.getY())); //DEBUG
		for( Rectangle area : areas ) {
			if( area.contains(e.getPoint()) ) {
				int index = areas.indexOf(area);
				listeners.get(index).action();
			}
		}
	}
	
}

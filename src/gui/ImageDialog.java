package gui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>The {@code ImageDialog} class builds a JDialog that
 * displays just the image provided to fit perfectly to
 * the dialog size and places it in the middle of the
 * owner dialog. 
 * @author guidanoli
 *
 */
@SuppressWarnings("serial")
public class ImageDialog extends JDialog {

	/**
	 * Constructs the image dialog
	 * @param owner - parent frame
	 * @param title - dialog title
	 * @param image - image to be displayed
	 */
	public ImageDialog(Frame owner, String title, Image image)
	{
		super(owner,title,true);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		JLabel imgLabel = new JLabel();
		imgLabel.setIcon(new ImageIcon(image));
		panel.add(imgLabel);
		getContentPane().add(panel);
		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
	}
	
}

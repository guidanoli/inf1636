package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.Logic;
import game.cell.AbstractCell;
import game.cell.OwnableCell;

/**
 * Shows current player properties and bank account balance
 * 
 * @author guidanoli
 *
 */
@SuppressWarnings("serial")
public class PropertyDialog extends JDialog implements ActionListener {

	private Logic logic = Logic.getInstance();
	private JComboBox<String> comboBox;
	private JLabel imgLabel = new JLabel();
	
	/**
	 * Constructs property dialog relative to parent
	 * @param parent - parent frame
	 */
	public PropertyDialog(Frame parent) {
		super(parent,"Meu Patrimônio",true);
		buildDialog();
		pack();
		setLocationRelativeTo(parent);
	}
	
	private void buildDialog() {
		/* Creating panel and margins */
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		panel.setBackground(Color.WHITE);
		
		/* Labels */
		JLabel nameLabel = new JLabel("Jogador "+logic.getCurrentPlayerColorName());
		nameLabel.setForeground(logic.getCurrentPlayerColor());
		int balance = logic.getCurrentPlayer().getBankAcc();
		JLabel balanceLabel = new JLabel(String.format("$ %d", balance));
		if(balance <= 0) balanceLabel.setForeground(Color.RED);
		
		/* Combo Box */
		JLabel cellComboLabel = new JLabel("Propriedades:");
		cellComboLabel.setFont(new Font(Font.DIALOG,Font.BOLD,12));
		String [] cellNames = logic.getCurrentPlayerCellsNames();
		comboBox = new JComboBox<String>(cellNames);
		comboBox.addActionListener(this);
		if(cellNames.length == 0)
		{
			imgLabel.setText("Você não possui propriedades.");
			comboBox.setPreferredSize(new Dimension(100,25));
		}
		else
		{
			addImageByCellName(comboBox.getItemAt(0));
		}
		
		/* Adding components with proper formatting */
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(nameLabel,c);
		c.insets = new Insets(0,10,0,0);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(balanceLabel,c);
		c.insets = new Insets(10,0,0,0);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(cellComboLabel,c);
		c.insets = new Insets(10,10,0,0);
		c.gridx = 1;
		c.gridy = 1;
		panel.add(comboBox,c);
		c.insets = new Insets(10,0,0,0);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		panel.add(imgLabel,c);
		
		/* Add panel to root pane from dialog */
		rootPane.getContentPane().add(panel);
	}

	public void actionPerformed(ActionEvent e) {
		int index = comboBox.getSelectedIndex();
		String cellName = comboBox.getItemAt(index);
		addImageByCellName(cellName);
	}

	private void addImageByCellName(String name) {
		AbstractCell cell = logic.getCellByName(name);
		if( cell instanceof OwnableCell )
		{
			OwnableCell ownableCell = (OwnableCell) cell;
			Image cardImg = ownableCell.getCardImage();
			int width = cardImg.getWidth(this);
			int height = cardImg.getHeight(this);
			int newWidth = 300;
			int newHeight = (int) ((float)height * ((float)newWidth/(float)width));
			Image resizedCardImage = cardImg.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
			ImageIcon icon = new ImageIcon(resizedCardImage);
			imgLabel.setIcon(icon);
			pack();
		}
	}
	
}

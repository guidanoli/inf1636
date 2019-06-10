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
import javax.swing.JButton;
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
	private JButton sellBtn = new JButton("Vender");
	private JLabel imgLabel = new JLabel();
	private JLabel balanceLabel = new JLabel();
	private JLabel propertyInfoLabel = new JLabel();
	
	/**
	 * Constructs property dialog relative to parent
	 * @param parent - parent frame
	 */
	public PropertyDialog(Frame parent) {
		super(parent,"Meu Patrimônio",true);
		boolean onDebt = logic.getCurrentPlayer().isBroke();
		buildDialog(onDebt);
		updateState();
		if( onDebt ) setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(parent);
	}
	
	private void buildDialog(boolean onDebt) {
		/* Creating panel and margins */
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		panel.setBackground(Color.WHITE);
		
		/* Labels */
		JLabel nameLabel = new JLabel("Jogador "+logic.getCurrentPlayerColorName());
		nameLabel.setForeground(logic.getCurrentPlayerColor());
		
		/* Combo Box */
		JLabel cellComboLabel = new JLabel("Selecione uma carta:");
		cellComboLabel.setFont(new Font(Font.DIALOG,Font.BOLD,12));
		String [] cellNames = logic.getCurrentPlayerCellsNames();
		comboBox = new JComboBox<String>(cellNames);
		comboBox.addActionListener(this);
		
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
		c.gridx = 0;
		c.gridy = 3;
		panel.add(propertyInfoLabel, c);
		
		if( onDebt )
		{
			/* Sell button */
			sellBtn.addActionListener(this);
			c.gridx = 0;
			c.gridy = 4;
			panel.add(sellBtn, c);
		}
		
		/* Add panel to root pane from dialog */
		rootPane.getContentPane().add(panel);
	}

	private void updateState() {
		int balance = logic.getCurrentPlayer().getBankAcc();
		balanceLabel.setText(String.format("$ %d", balance));
		if(balance < 0) balanceLabel.setForeground(Color.RED);
		if(comboBox.getItemCount() == 0)
		{
			imgLabel.setIcon(null);
			imgLabel.setText("Você não possui propriedades.");
			propertyInfoLabel.setText("");
			comboBox.setPreferredSize(new Dimension(100,25));
		}
		else
		{
			int index = comboBox.getSelectedIndex();
			String cellName = comboBox.getItemAt(index);
			AbstractCell cell = logic.getCellByName(cellName);
			addCardImage(cell);
			propertyInfoLabel.setText(getPropertyInfo(cell));
		}
		revalidate();
		pack();
	}
	
	private String getPropertyInfo(AbstractCell cell) {
		OwnableCell ownableCell = (OwnableCell) cell;
		int worth = ownableCell.getWorthValue();
		String upgrades = ownableCell.getUpgradeLevelString();
		if( upgrades == null )
			return String.format("Valor: $ %d", worth);
		else
			return String.format("Valor: $ %d (%s)", worth, upgrades);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if( source == comboBox )
		{
			updateState();
		}
		else if( source == sellBtn )
		{
			int index = comboBox.getSelectedIndex();
			String cellName = comboBox.getItemAt(index);
			OwnableCell cell = (OwnableCell) logic.getCellByName(cellName);
			cell.sell(); // selling the cell
			if( comboBox.getItemCount() == 0 ||
				!logic.getCurrentPlayer().isBroke() )
			{
				dispose();
				return;
			}
			comboBox.removeItemAt(index);
			updateState();
		}
	}

	private void addCardImage(AbstractCell cell) {
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

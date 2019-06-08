package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.Logic;

/**
 * Shows current player properties and bank account balance
 * 
 * @author guidanoli
 *
 */
@SuppressWarnings("serial")
public class PropertyDialog extends JDialog {

	private Logic logic = Logic.getInstance();
	
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
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(layout);
		int margin = 10;
		panel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
		
		JLabel nameLabel = new JLabel("Jogador "+logic.getCurrentPlayerColorName());
		nameLabel.setForeground(logic.getCurrentPlayerColor());
		int balance = logic.getCurrentPlayer().getBankAcc();
		JLabel balanceLabel = new JLabel(String.format("$ %d", balance));
		if(balance <= 0) balanceLabel.setForeground(Color.RED);
		
		JLabel cellComboLabel = new JLabel("Propriedades:");
		cellComboLabel.setFont(new Font(Font.DIALOG,Font.BOLD,12));
		String [] cellNames = logic.getCurrentPlayerCellsNames();
		JComboBox<String> comboBox = new JComboBox<String>(cellNames);
		if(cellNames.length == 0) comboBox.setPreferredSize(new Dimension(100,25));
		
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(nameLabel,c);
		c.gridy = 1;
		panel.add(balanceLabel,c);
		c.insets = new Insets(10,0,0,0);
		c.gridwidth = 1;
		c.gridy = 2;
		panel.add(cellComboLabel,c);
		c.insets = new Insets(10,10,0,0);
		panel.add(comboBox,c);
		
		rootPane.getContentPane().add(panel);
	}

}

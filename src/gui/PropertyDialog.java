package gui;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
		setLocationRelativeTo(parent);
		pack();
	}
	
	private void buildDialog() {
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel,BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		int margin = 10;
		panel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
		
		JLabel nameLabel = new JLabel("Jogador "+logic.getCurrentPlayerColorName());
		nameLabel.setForeground(logic.getCurrentPlayerColor());
		int balance = logic.getCurrentPlayer().getBankAcc();
		JLabel balanceLabel = new JLabel(String.format("$ %d", balance));
		if(balance <= 0) balanceLabel.setForeground(Color.RED);
		
		panel.add(nameLabel);
		panel.add(balanceLabel);
		
		rootPane.getContentPane().add(panel);
	}

}

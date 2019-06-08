package gui;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class EntryPoint {

	public EntryPoint() {}
	
	public static void main(String[] args) {
		String[] options = {"2", "3", "4", "5", "6"};
        ImageIcon icon = new ImageIcon("resources/sprites/pinos/pin0.png");
        String decision = (String)JOptionPane.showInputDialog(null, "Quantos serão os jogadores?", 
                "Novo Jogo", JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);
        if (decision==null) return;
        int numOfPlayers = Integer.parseInt(decision);
		new MainFrame("Banco Imobiliário",numOfPlayers,icon);
	}
	
}

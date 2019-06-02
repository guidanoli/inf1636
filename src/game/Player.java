package game;

import java.awt.Color;

public class Player {

	protected static int count = 0;
	
	protected int id;
	protected int pos = 0;
	protected Color color;
	protected int bankAcc = 2458;
	
	public Player(Color color) {
		this.color = color;
		this.id = count;
		Player.count ++;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public Color getColor() {
		return color;
	}
	public int getBankAcc() {
		return bankAcc;
	}
	
	public void accountTransfer(int delta) {
		this.bankAcc = this.bankAcc + delta;
	}
}

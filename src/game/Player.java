package game;

public class Player {
	
	protected int pos = 0;
	protected int color;
	protected int bankAcc;
	protected static int id;
	
	public Player(int color) {
		this.color = color;
		Player.id ++;
		//this.bankAcc = %valorinicial%
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public int getColor() {
		return color;
	}
	public int getBankAcc() {
		return bankAcc;
	}
	
	public void accountTransfer(int delta) {
		this.bankAcc = this.bankAcc + delta;
	}
}

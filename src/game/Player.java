package game;

public class Player {
	
	protected int pos = 0;
	protected int color;
	
	public Player(int color) {
		this.color = color;
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
	
}

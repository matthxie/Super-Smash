import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Platform { //extends JPanel
	int topCorner, leftCorner;
	int longWise, fatWise;
	private boolean hanging, right;
	
	public Platform(int leftCorner, int topCorner, int longWise, int fatWise, boolean hanging, boolean right) {
		this.topCorner = topCorner;
		this.leftCorner = leftCorner;
		this.longWise = longWise;
		this.fatWise = fatWise;
		
		this.hanging = hanging;
		this.right = right;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(this.leftCorner, this.topCorner, this.longWise, 1);
	}
	
	public boolean getHanging() {
		return hanging;
	}
	
	public boolean getOrientation() {
		return right;
	}
	
	public int getTopCornerX() {
		return leftCorner;
	}
	
	public int getTopCornerY() {
		return topCorner;
	}
	
	public int getLength() {
		return longWise;
	}
	
	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;		
		gg.fillRect(leftCorner, topCorner, longWise, fatWise);
	}
}

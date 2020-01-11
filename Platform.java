import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Platform { //extends JPanel
	private int topCornerY, topCornerX;
	private int longWise, fatWise;
	private boolean hanging, right;
	private PhysicsObject occupant;

	public Platform(int topCornerX, int topCornerY, int longWise, int fatWise, boolean hanging, boolean right) {
		this.topCornerY = topCornerY;
		this.topCornerX = topCornerX;
		this.longWise = longWise;
		this.fatWise = fatWise;

		this.hanging = hanging;
		this.right = right;
		
		this.occupant = null;
	}

	public Rectangle getBounds() {
		return new Rectangle(this.topCornerX, this.topCornerY, this.longWise, 1);
	}

	public boolean getHanging() {
		return hanging;
	}

	public boolean getOrientation() {
		return right;
	}
	
	public void setOccupant(PhysicsObject o) {
		occupant = o;
	}
	
	public PhysicsObject getOccupant() {
		return occupant;
	}

	public int getTopCornerX() {
		return topCornerX;
	}

	public int getTopCornerY() {
		return topCornerY;
	}

	public int getLength() {
		return longWise;
	}
	
	public int getThickness() {
		return fatWise;
	}

	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;		
		gg.fillRect(topCornerX, topCornerY, longWise, fatWise);
	}
}

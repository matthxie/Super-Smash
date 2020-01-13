import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Platform { //extends JPanel
	private int x, y;
	private int width, height;
	private boolean hanging, right;
	private PhysicsObject occupant;

	public Platform(int x, int y, int width, int height, boolean hanging, boolean right) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.hanging = hanging;
		this.right = right;
		
		this.occupant = null;
	}

	public Rectangle getBounds() {
		return new Rectangle(this.x, this.y, this.width, this.height);
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
		return x;
	}

	public int getTopCornerY() {
		return y;
	}

	public int getLength() {
		return width;
	}
	
	public int getThickness() {
		return height;
	}

	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;		
		gg.fillRect(x, y, width, height);
	}
}

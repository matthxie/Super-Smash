import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Platform { //extends JPanel
	private int y, x;
	private int width, height;
	private boolean hanging, right;
	boolean specialPlat;
	private PhysicsObject occupant;

	public Platform(int x, int y, int width, int height, boolean hanging, boolean right, boolean special) {
		this.y = y;
		this.x = x;
		this.width = width;
		this.height = height;

		this.hanging = hanging;
		this.right = right;
		
		this.occupant = null;
		
		this.specialPlat = special;
	}

	public Rectangle getBounds() {
		return new Rectangle(this.x, this.y, this.width, 1);
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

	public int getPlatX() {
		return x;
	}

	public int getPlatY() {
		return y;
	}

	public int getPlatWidth() {
		return width;
	}
	
	public int getPlatHeight() {
		return height;
	}

	public boolean getSpecialPlat() {
		return specialPlat;
	}
	
	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;		
		gg.fillRect(x, y, width, height);
	}
}
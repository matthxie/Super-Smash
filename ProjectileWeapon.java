import java.awt.*;

public class ProjectileWeapon {
	private Image img;
	private PhysicsObject initializer;

	private int lastX; 
	private int lastY;
	
	private int projectileW;
	private int projectileH;
	
	private double speed;
	private double damage;
	private double mass;
	
	private int orientation;
	
	public ProjectileWeapon(Image img, PhysicsObject initializer, int x, int y, int width, int height, double speed, double damage, double mass, int orientation) {
		this.img = img;
		this.initializer = initializer;
		
		this.lastX = x;
		this.lastY = y;
		
		this.projectileW = width;
		this.projectileH = height;
		
		this.speed = speed;
		this.damage = damage; 
		this.mass = mass;		
		
		this.orientation = orientation;
	}

	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;
		gg.drawImage(img, lastX, lastY, projectileW, projectileH, null);
	}

	public boolean attack() {
		if(orientation<0) this.lastX -= speed;
		else this.lastX += speed;
		
		if(lastX < 0 || lastX > 900) return true;
		return false;
	}
	
	public int getX() {
		return lastX;
	}
	
	public int getY() {
		return lastY;
	}
	
	public PhysicsObject getOwner() {
		return initializer;
	}
	
	public void setImg(Image image) {
		img = image;
	}
	
	public Image getImg() {
		return img;
	}
	
	public double getDamage() {
		return damage;
	}
}

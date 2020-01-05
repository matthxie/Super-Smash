import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ProjectileWeapon {
	private Image img;

	private int lastX; 
	private int lastY;
	
	private int weaponW;
	private int weaponH;
	
	private double speed;
	private double damage;
	private double mass;
	
	private int orientation;
	
	public ProjectileWeapon(String imgName, int x, int y, int width, int height, double speed, double damage, double mass, int orientation) {
		try { img = ImageIO.read(new File(imgName)); 
		img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
		} catch (IOException e) {}

		this.lastX = x;
		this.lastY = y;
		
		this.weaponW = width;
		this.weaponH = height;
		
		this.speed = speed;
		this.damage = damage; 
		this.mass = mass;		
		
		this.orientation = orientation;
	}

	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;	
		gg.drawImage(img, lastX, lastY, weaponW, weaponH, null);
	}

	public boolean attack() {
		if(orientation<0) this.lastX -= speed;
		else this.lastX += speed;
		
		if(lastX < 0 || lastX > 900) return true;
		return false;
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
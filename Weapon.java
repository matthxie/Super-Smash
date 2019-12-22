import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Weapon {
	private Image img;

	private int lastX; 
	private int lastY;
	
	private int weaponW;
	private int weaponH;

	private int angle;
	
	private double speed;
	private double damage;
	private double mass;

	public Weapon(String imgName, int x, int y, int width, int height, double speed, double damage, double mass) {
		try { img = ImageIO.read(new File(imgName)); 
		img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
		} catch (IOException e) {}

		this.lastX = x;
		this.lastY = y;
		
		this.weaponW = width;
		this.weaponH = height;
		
		this.angle = 90;

		this.speed = speed;
		this.damage = damage; 
		this.mass = mass;
	}

	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;	
		AffineTransform old = new AffineTransform();
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(angle), lastX+weaponW, lastY+(weaponH/16)+(90-angle));
		//at.translate(lastX-weaponW, lastY+weaponH);
		gg.setTransform(at);
		gg.drawImage(img, lastX, lastY, weaponW, weaponH, null);
		at.setToIdentity();
		gg.setTransform(old);		
	}

	public void setX(int x) {
		lastX = x;
	}

	public void setY(int y) {
		lastY = y;
	}
	
	public void setImg(Image image) {
		img = image;
	}
	
	public Image getImg() {
		return img;
	}
	
	public boolean swingDown() {
		angle-=3;
		if(angle <= 35) return true;
		return false;
	}
	
	public boolean swingUp() {
		angle+=3;
		if(angle >= 90) return true;
		return false;
	}
}
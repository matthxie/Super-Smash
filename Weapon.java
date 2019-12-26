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
	private int flipped;
	
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
		this.flipped = 0;

		this.speed = speed;
		this.damage = damage; 
		this.mass = mass;
	}

	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;	
		
		AffineTransform at = new AffineTransform();
		if(flipped == 0) at.rotate(Math.toRadians(angle), lastX+weaponW, lastY+(90-angle));
		else {
			at.rotate(Math.toRadians(angle), lastX+weaponH+150, (lastY-weaponW)-145);
			//at.translate(90, 80);
		}
	
		gg.setTransform(at);
		if(flipped == 235) gg.drawImage(img, lastX+145, lastY-155, weaponW, weaponH, null);
		else gg.drawImage(img, lastX, lastY, weaponW, weaponH, null);
		at.setToIdentity();
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
		angle-=5;
		if(angle <= 35+flipped) return true;
		return false;
	}
	
	public boolean swingUp() {
		angle+=5;
		if(angle >= 90+flipped) return true;
		return false;
	}
	
	public void setFlipped() {
		if(flipped == 235) {
			flipped = 0;
			angle = 90;
		}
		else {
			flipped = 235;
			angle = 270;
		}
	}
	
	public boolean getFlipped() {
		if(flipped == 235) return true;
		else return false;
	}
	
	public double getDamage() {
		return damage;
	}
}

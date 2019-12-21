import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PhysicsObject extends JPanel {
	private Weapon weapon;

	private int objectW;	//Object dimensions
	private int objectH;

	private int lastX;	//Current X value
	private int lastY;	//Current Y value

	private double fallSpeed;	//How fast the object is falling (Gravity)
	private double moveSpeed;	//How fast the object moves

	private boolean falling;	//Whether the object IS falling
	private boolean friction; 	//Whether is object should be rubbing

	private double fallingTime;	//How long the object has BEEN falling (for very not real gravitational acceleration)

	private boolean swingWeapon;
	private boolean swingDown;

	private Image img;

	public PhysicsObject(String file, String weaponName, int x, int y, int width, int height) {		
		this.objectW = width;
		this.objectH = height;

		this.lastX = x;
		this.lastY = y;

		this.weapon = new Weapon(weaponName, lastX-(objectW/2), lastY+(objectH/2), 40, 40, 2, 20, 10);

		this.fallSpeed = -10;
		this.moveSpeed = 0;

		this.falling = true;
		this.friction = false;

		this.fallingTime = 1;

		this.swingWeapon = false;
		this.swingDown = true;

		try { this.img = ImageIO.read(new File(file));
		img = img.getScaledInstance(objectW, objectH, Image.SCALE_SMOOTH); 
		} catch(IOException e) {}
	}

	public void draw(Graphics g) {	//The object's own draw method (this is what whiteboard from the physics class calls to draw onto panel)
		Graphics2D gg = (Graphics2D) g;

		//Make the object fall
		if(falling && !platformCollision() && !objectCollision(lastX, lastY)) {
			if(fallSpeed < 0) fallSpeed += 0.5;	//Slow down upward speed until it becomes 0
			else {
				fallSpeed = 2;	//After the object starts falling, its speed is modified by multiplying fallSpeed with fallingTime (acceleration) 
				fallingTime += 0.21;	//Increase fallingTime (not based on any clocks, this is just a value)
			}
		}

		//Check to see if object should fall
		if(lastY > (Physics.height-50 - objectH) || platformCollision() || objectCollision(lastX, lastY)) {	//Check if object is at a certain height (for ground)
			if(fallSpeed > 0) {
				falling = false;	//Stop falling if on ground
				fallingTime = 1;	//Reset the fallingTime (has to be always at least 1, otherwise would start at a lower falling speed)
			}
			else falling = true;
		}
		else if(lastY <= 0) {
			fallSpeed = 0.5;
			falling = true;
		}
		else falling = true;	//Fall if not on ground

		//Fall only if falling boolean says true
		if(falling) lastY += fallSpeed * fallingTime;	//fallingTime is a modifier

		//Move if not at left or right boundaries
		if(lastX + moveSpeed > 0 && lastX + moveSpeed < Physics.width-18 - objectW && !objectCollision(lastX+moveSpeed, lastY)) {
			if(!friction) lastX += moveSpeed;	//Move if there is no friction
			else {
				if(moveSpeed > 0 && !falling) moveSpeed -= 0.4;	//Increase or decrease moveSpeed until it's 0
				else if(moveSpeed > 0) moveSpeed -= 0.1;	//This line basically makes sure the object's lateral speed still exists in midair

				if(moveSpeed < 0 && !falling) moveSpeed += 0.4;
				else if(moveSpeed < 0) moveSpeed += 0.1;

				if(moveSpeed != 0) lastX += moveSpeed;	//Move the object until the moveSpeed becomes 0
				if(moveSpeed == 0) friction = false;
			}
		}

		if(swingWeapon) {
			if(swingDown && weapon.swingDown()) swingDown = false;
			if(!swingDown)
				if(weapon.swingUp()) {
					swingWeapon = false;
					swingDown = true;
				}
		}

		weapon.setX(lastX-objectW);
		weapon.setY(lastY+(objectH/2));

		gg.drawImage(img, lastX, lastY, null);
	}

	public void moveX(double dx) {	//Increase moveSpeed, which is how much the object moves with each refresh (0 means not moving)
		if(dx != 0) {
			friction = false;	//No friction while user is pressing the move key
			moveSpeed = dx;
		}
		else friction = true;	//Friction comes in after the user releases the move key
	}

	public void moveY(double dy) {	//Same thing as moveSpeed, but with fallSpeed in there for gravity
		if(dy < 0) {
			lastY -= 10;
			fallSpeed = dy;
		}
	}

	public boolean objectCollision(double lastX, double lastY) {	//Check if there's a collision (and bounce both objects based on their average of their moveSpeeds)
		for(int i=0; i<Physics.physicsObjectList.size(); i++) {
			if(Physics.physicsObjectList.get(i) != this) {
				PhysicsObject temp = Physics.physicsObjectList.get(i);
				if((lastY <= temp.lastY + temp.objectH && lastY >= temp.lastY)||(lastY + objectH <= temp.lastY + temp.objectH && lastY+ objectH >= temp.lastY))
					if((lastX >= temp.lastX && lastX <= temp.lastX + temp.objectW) ||(lastX + objectW >= temp.lastX && lastX + objectW <= temp.lastX + temp.objectW))
						return true;
			}
		}
		return false;
	}

	public boolean platformCollision() {
		for(int i = 0; i < Physics.platformList.size(); i++) {
			Platform temp =Physics.platformList.get(i);
			if((temp.topCorner) <= lastY+objectH && (temp.topCorner+temp.fatWise) >= lastY+objectH) 
				if(temp.leftCorner <= lastX + objectW && temp.leftCorner+temp.longWise >= lastX)
					return true;
		}
		return false;
	}

	public Weapon getweapon() {
		return weapon;
	}

	public void swingWeapon() {
		swingWeapon = true;
	}

	public boolean fallingStatus() {
		return falling;
	}

	public void setFallSpeed(double speed) {
		fallSpeed = speed;
	}

	public void setMoveSpeed(double speed) {
		moveSpeed = speed;
	}
}

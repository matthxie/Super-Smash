import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;

public class PhysicsObject extends JPanel {
	private int lastX;	//Current X value
	private int lastY;	//Current Y value

	private double fallSpeed;	//How fast the object is falling (Gravity)
	private double moveSpeed;	//How fast the object moves

	private boolean falling;	//Whether the object IS falling
	private boolean friction; 	//Whether is object should be rubbing

	private double fallingTime;	//How long the object has BEEN falling (for very not real gravitational acceleration)

	private Color color;

	int objectW = 20;	//Object dimensions
	int objectH = 20;

	public PhysicsObject(int x, int y, Color color) {    	
		this.lastX = x;
		this.lastY = y;

		this.fallSpeed = -10;
		this.moveSpeed = 0;

		this.falling = true;
		this.friction = false;

		this.fallingTime = 1;

		this.color = color;
	}

	public void draw(Graphics g) {	//The object's own draw method (this is what whiteboard from the physics class calls to draw onto panel)
		Graphics2D gg = (Graphics2D) g;	//This is some next level stuff all I know is that changing gg's stuff pisses Java off

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
		if(lastX + moveSpeed > 0 && lastX + moveSpeed < Physics.width-18 - objectW && !objectCollision(lastX, lastY)) {
			if(!friction) lastX += moveSpeed;	//Move if there is no friction
			else {
				if(moveSpeed > 0 && !falling) moveSpeed -= 0.2;	//Increase or decrease moveSpeed until it's 0
				else if(moveSpeed > 0) moveSpeed -= 0.05;	//This line basically makes sure the object's lateral speed still exists in midair

				if(moveSpeed < 0 && !falling) moveSpeed += 0.2;
				else if(moveSpeed < 0) moveSpeed += 0.05;

				if(moveSpeed != 0) lastX += moveSpeed;	//Move the object until the moveSpeed becomes 0
				if(moveSpeed == 0) friction = false;
			}
		}
		else if(lastX + moveSpeed > 0 && lastX + moveSpeed < Physics.width-18 - objectW && objectCollision(lastX, lastY)) {
			if(!objectCollision(lastX + 2, lastY)) lastX += moveSpeed;
		}

		gg.setColor(color);	//Object colour and dimensions
		gg.fillRect(lastX, lastY, objectW, objectH);	//(top left corner coordinates, how far left *X value* and down *Y* it goes)
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
					if((lastX >= temp.lastX && lastX <= temp.lastX + temp.objectW) ||(lastX + objectW >= temp.lastX && lastX + objectW <= temp.lastX + temp.objectW)) {
						if(moveSpeed != 0 || temp.getMoveSpeed() != 0) {	//Bounce each object off each other by pushing each back
							lastX += moveSpeed+temp.getMoveSpeed() / -40;
							moveSpeed = moveSpeed+temp.getMoveSpeed() / -2;
							friction = true;
							
							temp.lastX += moveSpeed+temp.getMoveSpeed() / -40;
							temp.moveSpeed = moveSpeed+temp.getMoveSpeed() / -2;
							temp.friction = true;
						}
						return true;
					}
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

	public boolean fallingStatus() {	//Check if object is falling
		return falling;
	}

	public int getLastX() {	//Get current X value
		return lastX;
	}

	public int getLastY() {	//Get current Y
		return lastY;
	}

	public int getObjectW() {	//Get object width
		return objectW;
	}

	public int getObjectH() {	//Get object height (of the rectangle, not how far it is off the ground)
		return objectH;
	}

	public double getFallSpeed() {
		return fallSpeed;
	}

	public double getMoveSpeed() {
		return moveSpeed;
	}

	public void setFallSpeed(double speed) {
		fallSpeed = speed;
	}

	public void setMoveSpeed(double speed) {
		moveSpeed = speed;
	}
}

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
		if(this.falling) {
			if(this.fallSpeed < 0) this.fallSpeed += 0.5;	//Increases the fall speed with each refresh
			else {
				this.fallSpeed = 2;	//After the object starts falling, its speed is modified by multiplying fallSpeed with fallingTime (acceleration) 
				this.fallingTime += 0.21;	//Increase fallingTime (not based on any clocks, this is just a value)
			}
		}

		//Check to see if object should fall (ignore collision)
		if(this.lastY > (Physics.height-50 - objectH)) {	//Check if object is at a certain height (for ground)
			objectCollision();
			this.falling = false;	//Stop falling if on ground
			this.fallingTime = 1;	//Reset the fallingTime (has to be always at least 1, otherwise would start at a lower falling speed)
		}
		else this.falling = true;	//Fall if not on ground

		//Fall only if falling boolean says true
		if(falling) this.lastY += this.fallSpeed * this.fallingTime;	//fallingTime is a modifier
		
		//Move if not at left or right boundaries
		if(lastX + moveSpeed > 0 && lastX + moveSpeed < Physics.width-18 - objectW && !objectCollision()) {
			if(!friction) lastX += moveSpeed;	//Move if there is no friction
			else {
				if(moveSpeed > 0 && !falling) moveSpeed -= 0.2;	//Increase or decrease moveSpeed until it's 0
				else if(moveSpeed > 0) moveSpeed -= 0.05;	//This line basically makes sure the object's lateral speed still exists in midair
				
				if(moveSpeed < 0 && !falling) moveSpeed += 0.2;
				else if(moveSpeed < 0) moveSpeed += 0.05;
				
				if(moveSpeed != 0) lastX += moveSpeed;	//Move the object until the moveSpeed because 0
				if(moveSpeed == 0) friction = false;
			}
		}

		gg.setColor(this.color);	//Object colour and dimensions
		gg.fillRect(this.lastX, this.lastY, objectW, objectH);	//(top left corner coordinates, how far left *X value* and down *Y* it goes)
	}

	public void moveX(double dx) {	//Increase moveSpeed, which is how much the object moves with each refresh (0 means not moving)
		if(dx != 0) {
			this.friction = false;	//No friction while user is pressing the move key
			this.moveSpeed = dx;
		}
		else this.friction = true;	//Friction comes in after the user releases the move key
	}

	public void moveY(double dy) {	//Same thing as moveSpeed, but with fallSpeed in there for gravity
		if(dy < 0) {
			this.lastY = Physics.height-50 - objectH;
			this.fallSpeed = dy;
		}
	}

	public boolean objectCollision() {	//Check if there's a collision (and bounce both objects based on their average of their moveSpeeds)
		for(int i=0; i<Physics.physicsObjectList.size(); i++) {
			if(Physics.physicsObjectList.get(i) != this) {
				//System.out.println("(" + this.lastX + ", " + this.lastY + ")");
				if(this.lastY >= Physics.physicsObjectList.get(i).getLastY() && this.lastY <= Physics.physicsObjectList.get(i).getLastY()+Physics.physicsObjectList.get(i).getObjectH())
					if(this.lastX <= Physics.physicsObjectList.get(i).getLastX()+Physics.physicsObjectList.get(i).getObjectW() && this.lastX >= Physics.physicsObjectList.get(i).getLastX()) {
						if(this.moveSpeed != 0 || Physics.physicsObjectList.get(i).getMoveSpeed() != 0) {
							this.moveSpeed = (this.moveSpeed+Physics.physicsObjectList.get(i).getMoveSpeed()) / -2;
							Physics.physicsObjectList.get(i).setMoveSpeed((this.moveSpeed+Physics.physicsObjectList.get(i).getMoveSpeed()) / -2);
						}
						return true;
					}
				return false;
			}
		}
		return false;
	}

	public boolean fallingStatus() {	//Check if object is falling
		return this.falling;
	}
	
	public int getLastX() {	//Get current X value
		return this.lastX;
	}
	
	public int getLastY() {	//Get current Y
		return this.lastY;
	}
	
	public int getObjectW() {	//Get object width
		return this.objectW;
	}
	
	public int getObjectH() {	//Get object height (of the rectangle, not how far it is off the gorund)
		return this.objectH;
	}
	
	public double getFallSpeed() {
		return this.fallSpeed;
	}
	
	public double getMoveSpeed() {
		return this.moveSpeed;
	}
	
	public void setFallSpeed(double speed) {
		this.fallSpeed = speed;
	}
	
	public void setMoveSpeed(double speed) {
		this.moveSpeed = speed;
	}
}

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.*;

public class PhysicsObject extends JPanel {
	private int lastX;	//Current X value
	private int lastY;	//Current Y value

	private int collisionDirection;	//Supposed to be the direction from which a collision occurs *WoRk iN pRogReSs*

	private double fallSpeed;	//How fast the object is falling (Gravity)
	private double moveSpeed;	//How fast the object moves

	private boolean falling;	//Whether the object IS falling
	private boolean movingBack;
	private double fallingTime;	//How long the object has BEEN falling (for very not real gravitational acceleration)
	private double backTime;
	private Color color;

	int objectW = 20;	//Object dimensions
	int objectH = 20;

	public PhysicsObject(int x, int y, Color color) {    	
		this.lastX = x;
		this.lastY = y;

		this.collisionDirection = -1;

		this.fallSpeed = -10;
		this.moveSpeed = 0;

		this.movingBack = false;
		this.backTime = 0;

		this.falling = true;

		this.fallingTime = 1;


		this.color = color;
	}

	public void draw(Graphics g) {	//The object's own draw method (this is what whiteboard from the physics class calls to draw onto panel)
		Graphics2D gg = (Graphics2D) g;	//This is some next level stuff all I know is that changing gg's stuff pisses Java off

		if(falling && (platformCollision() || objectCollision())) {
			this.falling = false;
			fallingTime = 1.0;
			fallSpeed = 2.0;
		}
		//Make the object fall
		if(this.falling && !platformCollision() && !objectCollision()) {
			if(this.fallSpeed < 0) this.fallSpeed += 0.5;	//Increases the fall speed with each refresh
			else {
				this.fallSpeed = 2;	//After the object starts falling, its speed is modified by multiplying fallSpeed by fallingTime (acceleration) 
				this.fallingTime += 0.21;	//Increase fallingTime (not based on any clocks, this is just a value)
			}
		}

		//Check to see if object should fall (ignore collision)
		if(this.lastY > (Physics.height-50 - objectH) || platformCollision() || objectCollision()) {	//Check if object is at a certain height (for ground)
			this.falling = false;
			this.fallingTime = 1;
		}
		else this.falling = true;		//Stop falling if on ground

		//Move if not at left or right boundaries
		if((lastX + moveSpeed > 0 && lastX + moveSpeed < Physics.width-18 - objectW )&& !objectCollision() && !movingBack) lastX += moveSpeed;
		else if((lastX + moveSpeed > 0 && lastX + moveSpeed < Physics.width-18 - objectW )&& objectCollision()) { 
			movingBack = true;
			backTime = 10;
		}
		if(backTime == 0) {
			movingBack = false;
		}
		if(movingBack) {
			if(moveSpeed < 0 && (lastX + 5 > 0 && lastX + 5 < Physics.width-18 - objectW )) {
				//trying to go left
				lastX += 5;
				backTime -= 2;
			}
			else if(moveSpeed > 0 && (lastX - 5 > 0 && lastX - 5 < Physics.width-18 - objectW )) {
				lastX -= 5;
				backTime-= 2;
			}
		}
		//System.out.println(fallSpeed);
		//Fall only if falling boolean says true
		if(falling) this.lastY += this.fallSpeed * this.fallingTime;	//fallingTime is a modifier

		gg.setColor(this.color);	//Object colour and dimensions
		gg.fillRect(this.lastX, this.lastY, objectW, objectH);	//(top left corner coordinates, how far left *X value* and down *Y* it goes)
	}

	public void moveX(double dx) {	//Increase moveSpeed, which is how much the object moves with each refresh (0 means not moving)
		this.moveSpeed = dx;
	}

	public void moveY(double dy) {	//Same thing as moveSpeed, but with fallSpeed in there for gravity
		if(dy < 0 ) {
			System.out.println(falling);
			this.lastY -= 10;
			this.fallSpeed = dy/1.5;
		}
	}

	public boolean objectCollision() {	//*WoRK iN pRoGResS********
		//System.out.println(this.lastY);
		for(int i=0; i<Physics.physicsObjectList.size(); i++) {
			if(Physics.physicsObjectList.get(i) != this) {
				PhysicsObject temp = Physics.physicsObjectList.get(i);
				if((this.lastX >= temp.lastX && this.lastX <= temp.lastX + temp.objectW) ||(this.lastX + this.objectW >= temp.lastX && this.lastX + this.objectW <= temp.lastX + temp.objectW)) {
					if((this.lastY <= temp.lastY + temp.objectH && this.lastY >= temp.lastY)||(this.lastY + this.objectH <= temp.lastY + temp.objectH && this.lastY+ this.objectH >= temp.lastY)) {
						return true;
					}

				}
				//				if(Physics.physicsObjectList.get(i).getBounds().intersects(this.getBounds())) {
				//					//System.out.println("Collision");
				//					//if(this.lastY > Physics.physicsObjectList.get(i).lastY) this.collisionDirection = 0;
				//					//else if(this.lastY < Physics.physicsObjectList.get(i).lastY) this.collisionDirection = 1;
				//					//else if(this.lastX > Physics.physicsObjectList.get(i).lastX) this.collisionDirection = 2;
				//					//else if(this.lastX < Physics.physicsObjectList.get(i).lastX) this.collisionDirection = 3;
				//					//System.out.println("collision, " + this.collisionDirection + ", " + this.lastY);
				//					return true;
				//				}
				//				else if(Physics.physicsObjectList.get(i).lastY+Physics.physicsObjectList.get(i).objectH == this.lastY) {
				//					return true;
				//				}
			}
		}
		return false;
	}

	public boolean platformCollision() {
		//System.out.println("hu");
		for(int i = 0; i < Physics.platformList.size(); i++) {
			Platform temp =Physics.platformList.get(i);
			//if(temp.getBounds().intersects(this.getSpecialBounds())) {
			if((temp.topCorner) <= this.lastY+this.objectH && (temp.topCorner+temp.fatWise) >= this.lastY+this.objectH) 
				if(temp.leftCorner <= this.lastX + this.objectW&& temp.leftCorner+temp.longWise >= this.lastX)
					return true;
			//				if(temp.getBounds().intersects(this.getSpecialBounds())) {
			//
			//
			//					return true;
			//				}
			//}
		}


		//System.out.println("Not a platform");
		return false;

	}

	public Rectangle getSpecialBounds() {
		return new Rectangle(this.lastX, this.lastY+ this.objectH, this.objectW, 8);


		//	public boolean onPlatform() {
		//		for(int i = 0; i < Physics.platformList.size(); i++) {
		//			if(this.lastY >= Physics.platformList.get(i).topCorner && this.lastX >= Physics.platformList.get(i).topCorner && this.lastX <= Physics.platformList.get(i).topCorner + Physics.platformList.get(i).longWise) {
		//				System.out.println("hu");
		//
		//				return true;
		//			}
		//		}
		//		
		//		
		//		return false;
		//		
	}
	public Rectangle getBounds() {	//Currently not working
		return new Rectangle(this.lastX, this.lastY, this.objectW, this.objectH);
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
}

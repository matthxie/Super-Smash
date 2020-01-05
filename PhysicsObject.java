import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PhysicsObject extends JPanel {
	private int numDeath;
	private boolean deadRightNow = false;
	private long tempTime;
	
	private int objectW;	//Object dimensions
	private int objectH;
	
	private int lastX;	//Current X value
	private int lastY;	//Current Y value
	
	private MeleeWeapon weapon;	//Weapon to be used by the player object

	private double fallSpeed;	//How fast the object is falling (Gravity)
	private double moveSpeed;	//How fast the object moves

	private boolean falling;	//Whether the object IS falling
	private boolean friction; 	//Whether is object should be rubbing

	private double fallingTime;	//How long the object has BEEN falling (for very not real gravitational acceleration)

	private boolean swingWeapon;	//Tells when the player is attacking
	private boolean swingDown;		//Tells when the player is during the first of the two-phase attack animation

	private int rightOrientation;	//Which way the player is facing

	private double damagePercentage;	//Damage percentage of the player, decides how far the player flies when hit
	
	private PhysicsObject hitObject;	//Stores the player object that has been hit by this player object

	private Image img;	//The image used for this player object

	public PhysicsObject(String file, String weaponName, int x, int y, int width, int height) {
		this.objectW = width;
		this.objectH = height;

		this.lastX = x;
		this.lastY = y;

		this.weapon = new MeleeWeapon(weaponName, lastX-(objectW/2), lastY+(objectH/2), 40, 40, 2, 0.2, 10);
		
		this.fallSpeed = -10;
		this.moveSpeed = 0;

		this.falling = true;
		this.friction = true;

		this.fallingTime = 1;

		this.swingWeapon = false;
		this.swingDown = true;

		this.rightOrientation = -1;

		this.damagePercentage = 0;

		try { this.img = ImageIO.read(new File(file));
		img = img.getScaledInstance(objectW, objectH, Image.SCALE_SMOOTH); 
		} catch(IOException e) {}
	}

	public void draw(Graphics g) {	//The object's own draw method (this is what canvas from the physics class calls to draw onto panel)
		Graphics2D gg = (Graphics2D) g;
				
		if(!deadRightNow) {
			if(this.lastX > Physics.width || this.lastX < 0 || this.lastY > Physics.height) {
				this.numDeath++;
				tempTime = System.currentTimeMillis();
				deadRightNow=true;
			}

			if(falling && !platformCollision() && !objectCollision(lastX, lastY, false)) { //Make the object fall
				if(fallSpeed < 0) fallSpeed += 0.5;	//Slow down upward speed until it becomes 0
				else {
					fallSpeed = 2;	//After the object starts falling, its speed is modified by multiplying fallSpeed with fallingTime (acceleration) 
					fallingTime += 0.21;	//Increase fallingTime (not based on any clocks, this is just a value)
				}
			}

			if(platformCollision() || objectCollision(lastX, lastY, false)) {	//Object will fall if not standing on platform or collision
				if(fallSpeed > 0) {
					falling = false;	//Stop falling if on a platform
					fallingTime = 1;	//Reset the fallingTime (has to be always at least 1, otherwise would start at a lower falling speed)
				}
				else falling = true;
			}
			else falling = true;	//Fall if not on ground

			if(falling) lastY += fallSpeed * fallingTime;	//Fall only if falling boolean says true, fallingTime is a modifier

			if(!objectCollision(lastX+moveSpeed, lastY, false)) {	//Move if movement will not result in a collision
				if(!friction) lastX += moveSpeed;	//Move if there is no friction
				else {
					if(moveSpeed > 0 && !falling) moveSpeed -= 0.8;	//Ground friction: decrease moveSpeed until it's 0 (when going right)
					else if(moveSpeed > 0) moveSpeed -= 0.04;	//Air friction: rate of lateral speed decrease while in midair (going right)

					if(moveSpeed < 0 && !falling) moveSpeed += 0.8; //Ground friction: increase moveSpeed until it's 0 (when going left)
					else if(moveSpeed < 0) moveSpeed += 0.04;	//Air friction while going left

					if(moveSpeed != 0) lastX += moveSpeed;	//Move the object until the moveSpeed becomes 0 (indicating move key released)
				}
			}

			if(swingWeapon) {	//Swing weapon of player object and check if hit
				if(swingDown && objectCollision(lastX+11, lastY, true) && rightOrientation > 0) {	//Deal damage to the right
					hitObject.moveSpeed += (hitObject.damagePercentage*weapon.getDamage());	//Push object right
					hitObject.fallSpeed -= (1.5*hitObject.damagePercentage*weapon.getDamage());	//Push object up 
					hitObject.damagePercentage += weapon.getDamage();	//Add to other player's damage percentage
				}
				else if(swingDown && objectCollision(lastX-11, lastY, true) && rightOrientation < 0) {	//Deal damage to the left
					hitObject.moveSpeed -= (hitObject.damagePercentage*weapon.getDamage());	
					hitObject.fallSpeed -= (1.5*hitObject.damagePercentage*weapon.getDamage());
					hitObject.damagePercentage += weapon.getDamage();
				}

				if(!weapon.getFlipped()) {	//Attack animation when weapon is facing left
					if(swingDown && weapon.swingDown()) swingDown = false;
					if(!swingDown) {
						if(weapon.swingUp()) {
							swingWeapon = false;
							swingDown = true;
						}
					}
				}
				else {	//Attack animation when weapon is facing right
					if(swingDown && weapon.swingUp()) swingDown = false;
					if(!swingDown) {
						if(weapon.swingDown()) {
							swingWeapon = false;
							swingDown = true;
						}
					}
				}
			}

			if(friction) {	//Flip the image to face the other object if this object is not being moved by player
				for(int i=0; i<Physics.physicsObjectList.size(); i++) {	//Check if this is currently facing the other player, if not flip
					PhysicsObject temp = Physics.physicsObjectList.get(i);
					if(temp!=this && (temp.lastX<lastX && rightOrientation>0) || (temp.lastX>lastX && rightOrientation<0)) {
						img = flip(toBufferedImage(img), true);	
						weapon.setImg(flip(toBufferedImage(weapon.getImg()), false));
					}
				}
			}

			if(rightOrientation<0) {
				weapon.setX(lastX-objectW);
				weapon.setY(lastY+(objectH/2));
			}
			else {
				weapon.setX(lastX-objectW-170);
				weapon.setY(lastY+(objectH/2)+140);
			}
			
			gg.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
			gg.setColor(Color.red);
			gg.drawImage(img, lastX, lastY, null);
			gg.drawString(Double.toString(Math.round(damagePercentage*100)/100)+"%", lastX, lastY-5);
		}
		else if(numDeath > 3) {
			Physics.paused = true;
			
			int goOrNot = JOptionPane.showConfirmDialog(Physics.frame,
					"You Died Three Times. Exit?",
					"Game Over",
					JOptionPane.YES_NO_OPTION);
		
			if(goOrNot == 0) System.exit(0);
			else Physics.quit = true;
			
			System.out.println(goOrNot);
		}
		else if(this.tempTime+1000<System.currentTimeMillis()) {	//Respawn the player at the top of the screen
			lastX = ThreadLocalRandom.current().nextInt(100, 750 + 1);
			lastY = 0;
			
			damagePercentage = 0;
			moveSpeed = 0;
			fallSpeed = 0;
			falling = true;
			numDeath++;
			deadRightNow = false;
		}
	}

	public void moveX(double dx) {	//Increase moveSpeed, which is how much the object moves with each refresh (0 means not moving)
		if(dx != 0) {
			friction = false;	//No friction while user is pressing the move key
			moveSpeed = dx;

			if(dx<0 && rightOrientation>0 || dx>0 && rightOrientation<0) {
				img = flip(toBufferedImage(img), true);
				weapon.setImg(flip(toBufferedImage(weapon.getImg()), false));
			}
		}
		else friction = true;	//Friction comes in after the user releases the move key
	}

	public void moveY(double dy) {	//Same thing as moveSpeed, but with fallSpeed in there for gravity
		if(dy < 0) {
			lastY -= 10;
			fallSpeed = dy;
		}
	}

	public boolean objectCollision(double lastX, double lastY, boolean hit) {	//Check if there's a collision
		for(int i=0; i<Physics.physicsObjectList.size(); i++) {
			if(Physics.physicsObjectList.get(i) != this) {
				PhysicsObject temp = Physics.physicsObjectList.get(i);
				if((lastY <= temp.lastY + temp.objectH && lastY >= temp.lastY)||(lastY + objectH <= temp.lastY + temp.objectH && lastY+ objectH >= temp.lastY))
					if((lastX >= temp.lastX && lastX <= temp.lastX + temp.objectW) ||(lastX + objectW >= temp.lastX && lastX + objectW <= temp.lastX + temp.objectW)) {
						if(temp.falling) {	//If other player lands on top this player, slide the other player off to the side
							if(temp.lastX+temp.objectW > lastX+objectW) temp.lastX += objectW;
							else temp.lastX -= objectW;
							temp.falling = true;
						}
						if(hit) hitObject = temp;
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

	Image flip(BufferedImage sprite, boolean player) {	//Flip image in parameter
		if(player) rightOrientation *= -1;	
		else weapon.setFlipped();

		BufferedImage img = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int i = sprite.getWidth()-1; i>0; i--) 
			for(int j=0; j<sprite.getHeight(); j++)
				img.setRGB(sprite.getWidth()-i, j, sprite.getRGB(i, j));
		return img;
	}
	
	public BufferedImage toBufferedImage(Image img) {	//Convert Image into BufferedImage
		if(img instanceof BufferedImage) return (BufferedImage) img;

		BufferedImage temp = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = temp.createGraphics();
		graphics.drawImage(img, 0, 0, null);
		graphics.dispose();

		return temp;
	}

	public int getX() {
		return lastX;
	}
	
	public int getY() {
		return lastY;
	}
	
	public MeleeWeapon getweapon() {
		return weapon;
	}

	public void swingWeapon() {
		Physics.projectileList.add(new ProjectileWeapon(Physics.fireball, lastX-(objectW/2), lastY+(objectH/8), 50, 30, 2, 0.2, 10, rightOrientation));
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
	public double getDamagePercentage() {
		return damagePercentage;
	}
}

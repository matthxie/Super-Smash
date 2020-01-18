import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PhysicsObject extends JPanel {
	private int playerNumber;	//The number of this player (either 1 or 2)
	private String characterName;	//The name of this character (ie "yoshi")

	private int numDeath;	//How many times this character has died
	private boolean deadRightNow = false;	//Whether the character is currently dead
	private long tempTime;	//How long player has been "dead" for, so they can be respawned after a certain duration has passed

	//private boolean onLadder;
	
	private int objectW;	//Object dimensions
	private int objectH;

	private int lastX;	//Current X value
	private int lastY;	//Current Y value7

	private boolean hanging;	//The platform this character is currently standing on
	private Platform hangingPlatform;	//The platform used for hanging this character is currently standing on

	private double mass;	//How heavy this character is
	private int runSpeed;	//How fast this character moves

	private boolean melee;
	private MeleeWeapon weapon;	//Weapon to be used by the player object

	private String projectileName;	//The projectile this character uses
	private String weaponName;	//The melee weapon this character uses

	private long fireTime;	//Used to keep a time limit between how often the player can fire
	private long hitTime;	//Used to store how long it has been since the player was last hit, player cannot control character within this time

	private double fallSpeed;	//How fast the object is falling (Gravity)
	private double moveSpeed;	//How fast the object moves

	private boolean falling;	//Whether the object IS falling
	private boolean friction; 	//Whether is object should be rubbing

	private double fallingTime;	//How long the object has BEEN falling (for very not real gravitational acceleration)

	private boolean swingWeapon;	//Tells when the player is attacking
	private boolean swingDown;		//Tells when the player is during the first of the two-phase attack animation

	private int numJumps;	//How many jumps have been taken in a row, used for double jumps

	private int orientation;	//Which way the player is facing

	private double damagePercentage;	//Damage percentage of the player, decides how far the player flies when hit
	private double damageTaken;	

	private PhysicsObject hitObject;	//Stores the player object that has been hit by this player object
	private Platform platform;

	private Image img;	//The image used for this player object
	
	private int frameNum; 	//The frame number used to play death effect frame by frame

	public PhysicsObject(String file, String weaponName, String projectileName, boolean melee, int x, int y, int width, int height, double mass, int runSpeed) {
		this.characterName = file.substring(0, file.length()-4);

		this.objectW = width;
		this.objectH = height;

		this.lastX = x;
		this.lastY = y;

		this.hanging = false;

		this.mass = mass;
		this.runSpeed = runSpeed;

		this.melee = melee;
		this.weapon = new MeleeWeapon(weaponName, melee, lastX-(objectW/2), lastY+(objectH/2), 40, 40, 2, 0.2, 10);

		this.projectileName = projectileName;
		this.weaponName = weaponName.substring(0, weaponName.length()-4);

		this.fireTime = System.currentTimeMillis();

		this.fallSpeed = -10;
		this.moveSpeed = 0;

		this.falling = true;
		this.friction = true;

		this.fallingTime = 1;

		this.swingWeapon = false;
		this.swingDown = true;

		this.numJumps = 0;

		this.orientation = -1;

		this.damagePercentage = 0;
		this.damageTaken = 0;

		this.frameNum = 1;

		try { this.img = ImageIO.read(new File(file));
		img = img.getScaledInstance(objectW, objectH, Image.SCALE_SMOOTH); 
		} catch(IOException e) {}
	}

	public void draw(Graphics g) {	//The object's own draw method (this is what canvas from the physics class calls to draw onto panel)
		Graphics2D gg = (Graphics2D) g;

		if(!deadRightNow) {
			if(this.lastX > Physics.width+75 || this.lastX < -75 || this.lastY > Physics.height+20) {
				this.numDeath++;
				tempTime = System.currentTimeMillis();
				
				deadRightNow=true;
			}
			
			/*if(onLadder && this.lastY > 190) {
			System.out.println("Ha");
			this.lastY-= 2;
			this.falling = false;
			
			}*/

			if(falling && !platformCollision() && !objectCollision(lastX, lastY, false)) { //Make the object fall
				if(fallSpeed < 0) fallSpeed += 0.5;	//Slow down upward speed until it becomes 0
				else {
					fallSpeed = 2;	//After the object starts falling, its speed is modified by multiplying fallSpeed with fallingTime (acceleration) 
					if(fallingTime < 14) fallingTime += 0.21;	//Increase fallingTime (not based on any clocks, this is just a value)
				}
			}

			if(platformCollision() || objectCollision(lastX, lastY, false) || hanging) {	//Object will fall if not standing on platform or collision
				if(fallSpeed > 0) {
					falling = false;	//Stop falling if on a platform
					fallingTime = 1;	//Reset the fallingTime (has to be always at least 1, otherwise would start at a lower falling speed)
					numJumps = 0;
				}
				else falling = true;
			}
			else {
				falling = true;	//Fall if not on ground
			}

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
				if(swingDown && objectCollision(lastX+11, lastY, true) && orientation > 0 || objectCollision(lastX-11, lastY, true) && orientation < 0) 	//Deal damage to the right
					dealDamage(weapon.getDamage(), hitObject);

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
					if(temp!=this && (temp.lastX<lastX && orientation>0) || (temp.lastX>lastX && orientation<0)) {
						img = Physics.flip(Physics.toBufferedImage(img));	
						orientation *= -1;

						weapon.setImg(Physics.flip(Physics.toBufferedImage(weapon.getImg())));
						weapon.setFlipped();
					}
				}
			}

			if(orientation<0) {
				weapon.setX(lastX-objectW);
				weapon.setY(lastY+(objectH/2));
			}
			else {
				weapon.setX(lastX-objectW-170);
				weapon.setY(lastY+(objectH/2)+140);
			}

			if(!melee) weapon.setVisible(swingWeapon);

			if(playerNumber == 1) gg.setColor(Color.red);
			else gg.setColor(Color.blue);
			gg.setFont(new Font("Comic Sans MS", Font.BOLD, 15));

			if(hanging && !platform.getOrientation()) gg.drawImage(Physics.imageMap.get("hand"), lastX-objectW/3, lastY-objectH/4, 30, 30, null);
			else if(hanging && platform.getOrientation()) gg.drawImage(Physics.imageMap.get("handFlipped"), lastX+objectW/3, lastY-objectH/4, 30, 30, null);

			damagePercentage = Math.round(((damageTaken/2.0)*100)*100)/100;

			gg.drawImage(img, lastX, lastY, null);
			
			if(playerNumber == 1) {
				gg.drawImage(Physics.imageMap.get(characterName), Physics.width-(600), Physics.height-70, null);
				gg.drawString(Long.toString(Math.round(((damageTaken/2.0)*100)*100)/100)+"%", Physics.width-600, Physics.height-70);
				gg.drawString(characterName, Physics.width-550, Physics.height-70);
			}
			else if(playerNumber == 2) {
				gg.drawImage(Physics.imageMap.get(characterName), Physics.width-(300), Physics.height-70, null);
				gg.drawString(Long.toString(Math.round(((damageTaken/2.0)*100)*100)/100)+"%", Physics.width-300, Physics.height-70);
				gg.drawString(characterName, Physics.width-250, Physics.height-70);
			}

			gg.drawString("Player " + playerNumber, lastX, lastY-10);
		}
		else if(numDeath > 4) {
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

			frameNum = 1;
			damageTaken = 0;
			moveSpeed = 0;
			fallSpeed = 0;
			fallingTime = 0;
			falling = true;
			numDeath++;
			deadRightNow = false;
		}
		else {
			Physics.playSound("death");
			if(frameNum <= 11) {
				if(lastX < 0) gg.drawImage(Physics.flip(Physics.toBufferedImage(Physics.imageMap.get("frame"+frameNum))), 0, lastY, null);
				else gg.drawImage(Physics.imageMap.get("frame"+frameNum), Physics.width-400, lastY, null);
				frameNum++;
			}
		}
	}

	public void moveX(double dx) {	//Horizontal movement
		if(dx != 0 && !hanging && hitTime+500<System.currentTimeMillis()) {
			friction = false;	//No friction while user is pressing the move key
			moveSpeed = dx;

			if(dx<0 && orientation>0 || dx>0 && orientation<0) {
				img = Physics.flip(Physics.toBufferedImage(img));
				orientation *= -1;

				weapon.setImg(Physics.flip(Physics.toBufferedImage(weapon.getImg())));
				weapon.setFlipped();
			}
		}
		else friction = true;	//Friction comes in after the user releases the move key
	}

	public void moveY(double dy) {	//Vertical movement
		if(dy < 0 && hitTime+500<System.currentTimeMillis()) {
			numJumps++;
			fallingTime = 1;
			fallSpeed = dy;
			Physics.playSound("jump.wav");
		}
		else if(dy > 0 && platform.getPlatHeight() < 25 && hitTime+500<System.currentTimeMillis()){
			lastY += platform.getPlatHeight();
			falling = true;
		}

		if(hanging) {
			hangingPlatform.setOccupant(null);
			hangingPlatform = null;
			hanging = false;
		}
	}

	public boolean objectCollision(double lastX, double lastY, boolean hit) {	//Check if there's a collision between two player objects
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

	public boolean platformCollision() {	//Check if player object has come into contact with a platform
		for(int i = 0; i < Physics.platformList.size(); i++) {
			Platform temp = Physics.platformList.get(i);	//Create a temporal Platform object 
			if((temp.getPlatX()) <= lastY+objectH && (temp.getPlatY()+temp.getPlatHeight()) >= lastY+objectH) 
				if(temp.getPlatX() <= lastX + objectW && temp.getPlatX()+temp.getPlatWidth() >= lastX) {

					platform = temp;	//Set the current platform as the one being stepped on

					if(temp.getPlatY() <= lastY+objectH && (temp.getPlatY()+temp.getPlatHeight()) >= lastY+objectH) {
						if(!temp.getSpecialPlat())
							return true;
					}
					
					if(this.lastX >= temp.getPlatX() && this.lastX<=temp.getPlatX()+temp.getPlatWidth()) {
						if(this.lastY+this.objectH <= temp.getPlatY()) {//+ this.objectH 
							if(this.lastY + this.objectH+(fallSpeed * fallingTime)>= temp.getPlatY()) {
								this.lastY = temp.getPlatY()-this.objectH;
								if(!temp.getSpecialPlat())
									return true;
							}
						}
					}
					
					if(temp.getHanging() && fallSpeed>=0 && temp.getOccupant()==null) { //Operations for if or if not someone else is hanging on
						hanging = true;
						moveSpeed = 0;
						lastY = temp.getPlatY();

						temp.setOccupant(this);
						hangingPlatform = temp;

						if(!temp.getOrientation()) lastX = temp.getPlatX();
						else lastX = temp.getPlatX()+temp.getPlatWidth();
					}
					else if(temp.getHanging() && fallSpeed>=0 && temp.getOccupant() != null && temp.getOccupant().hangingPlatform == temp) {
						PhysicsObject o = temp.getOccupant();
						o.falling = true;
						o.moveY(-10);
						o.friction = true;

						if(!temp.getOrientation()) o.moveSpeed -= 3;
						else o.moveSpeed += 3;

					}
					return true;
				}
		}
		return false;
	}
	
	public boolean checkIfOnSpecial() {
		for(int i = 0; i < Physics.platformList.size(); i++) {
			Platform temp =Physics.platformList.get(i);
			if(temp.getSpecialPlat()) {
				System.out.println("special");
				if(this.lastX +this.objectW>temp.getPlatX()&& this.lastX<temp.getPlatX()+temp.getPlatWidth()) {
					if(this.lastY< temp.getPlatY()+temp.getPlatHeight()&&this.lastY > temp.getPlatY()+200) {//&&this.lastY+this.objectH<=temp.getPlatY()+temp.getPlatHeight()
						return true;
					}
				}
			}
		}
		return false;
	}

	public void swingWeapon(boolean block, boolean attack, boolean heavy) {	//Attack with this object, whether with melee or with projectile
		if(!hanging) {
			if(!melee && attack && fireTime + 400 < System.currentTimeMillis()) {	//With projectile				
				Physics.playSound(projectileName);
				Physics.projectileList.add(new ProjectileWeapon(projectileName, this, lastX-(objectW/2), lastY+(objectH/8), 50, 30, 0.2, 10, orientation));
				fireTime = System.currentTimeMillis();
			}
			if(!melee && heavy && fireTime + 300<System.currentTimeMillis()) {
				if(!swingWeapon) Physics.playSound(weaponName);
				swingWeapon = true;
				fireTime = System.currentTimeMillis();
			}

			if(melee && attack && fireTime + 300<System.currentTimeMillis()) {
				if(!swingWeapon) Physics.playSound(weaponName);
				swingWeapon = true;
				fireTime = System.currentTimeMillis();
			}
			if(melee && heavy && fireTime + 300<System.currentTimeMillis()) {
				if(!swingWeapon) Physics.playSound(weaponName);
				swingWeapon = true;
				fireTime = System.currentTimeMillis();
			}
		}
	}

	public void dealDamage(double damage, PhysicsObject o) {	//Deal damage the object that has been hit
		o.moveSpeed += ((o.damagePercentage/3)*damage) * this.orientation;	//Push object right
		if(o.damagePercentage < 90) o.fallSpeed -= (1.5*(o.damagePercentage/3)*damage);	//Push object up 
		else o.fallSpeed -= o.damagePercentage/3*damage;
		o.damageTaken += damage;	//Add to other player's damage percentage
		o.hanging = false;

		if(Math.abs(o.fallSpeed)>4 || Math.abs(o.moveSpeed)>3) o.hitTime = System.currentTimeMillis();
	}

	public int getX() {
		return lastX;
	}

	public int getY() {
		return lastY;
	}

	public int getNumJumps() {
		return numJumps;
	}

	public int getOrientation() {
		return orientation;
	}

	public MeleeWeapon getWeapon() {
		return weapon;
	}

	public boolean getType() {
		return melee;
	}

	public boolean fallingStatus() {
		return falling;
	}

	public void setFallSpeed(double speed) {
		fallSpeed = speed;
	}

	public void setMoveSpeed(double speed) {
		if(speed < 0) moveSpeed = -runSpeed;
		else if(speed > 0) moveSpeed = runSpeed;
		else moveSpeed = 0;
	}

	public double getDamageTaken() {
		return damageTaken;
	}

	public void setPlayerNumber(int num) {
		playerNumber = num;
	}
}
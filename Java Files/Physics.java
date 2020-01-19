import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Physics implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	public static final int height = 600;	//Window dimensions
	public static final int width = 900;
	
	//Put any and all objects you create into an ArrayList, the Canvas method will draw their contents onto the panel
	public static ArrayList<PhysicsObject> physicsObjectList = new ArrayList<PhysicsObject>();	//All physics objects
	public static ArrayList<Platform> platformList = new ArrayList<Platform>();	//All platform objects
	
	public static ArrayList<MeleeWeapon> weaponList = new ArrayList<MeleeWeapon>();	//All weapon objects
	public static ArrayList<ProjectileWeapon> projectileList = new ArrayList<ProjectileWeapon>(); //All projectiles
	
	public static HashMap<String, PhysicsObject> physicsObjectMap = new HashMap<String, PhysicsObject>();	//Stores available characters
	public static HashMap<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();
	
	public static HashMap<String, Clip> soundMap = new HashMap<String, Clip>();	//Sound clips
	
	public static Map currentMap;	//Map from map selection menu
	
	public static boolean paused = false;
	public static boolean quit = false;

	public static boolean P1IsShooting, P1HeavyAttack, P1Block = false;	//Whether player one is shooting
	public static boolean P2IsShooting, P2HeavyAttack, P2Block = false;	//Whether player two is shooting

	public static Image backgroundImage = Toolkit.getDefaultToolkit().createImage("better.jpg").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);
	
	public static JFrame frame;
	public static JPanel panel = new Canvas();	//Canvas is a method which creates a panel that you can "draw" objects onto

	public Physics() {
		frame = new JFrame("Super Smash");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);

		backgroundImage = currentMap.getBack();
		
		panel.setLayout(null);
		
		//Store information about all playable characters
		physicsObjectMap.put("mario", new PhysicsObject("mario.png", "sword.png", "fireball", false, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 40, 50, 33.3, 10));
		physicsObjectMap.put("donkey", new PhysicsObject("donkey.png", "sword.png", "fireball", true, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 40, 50, 33.3, 10));
		physicsObjectMap.put("link", new PhysicsObject("link.png", "sword.png", "arrow", false, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 40, 50, 33.3, 10));
		physicsObjectMap.put("samus", new PhysicsObject("samus.png", "sword.png", "laser", false, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 40, 50, 33.3, 10));
		physicsObjectMap.put("yoshi", new PhysicsObject("yoshi.png", "sword.png", "fireball", false, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 40, 50, 33.3, 10));
		physicsObjectMap.put("kirby", new PhysicsObject("kirby.png", "sword.png", "fireball", true, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 40, 50, 33.3, 10));
		physicsObjectMap.put("fox", new PhysicsObject("fox.png", "sword.png", "laser", false, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 40, 50, 33.3, 10));
		physicsObjectMap.put("pikachu", new PhysicsObject("pikachu.png", "sword.png", "lightning", false, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 40, 45, 33.3, 10));
		
		//Which two characters are to be used
		physicsObjectList.add(physicsObjectMap.get(ChooseCharacterMenu.playerOneChar));
		physicsObjectList.add(physicsObjectMap.get(ChooseCharacterMenu.playerTwoChar));
		
		//Set character numbs (1 or 2)
		physicsObjectList.get(0).setPlayerNumber(1);
		physicsObjectList.get(1).setPlayerNumber(2);
		
		//Add their weapons so they will be rendered
		weaponList.add(physicsObjectList.get(0).getWeapon());
		weaponList.add(physicsObjectList.get(1).getWeapon());
			
		//The selected map to be played on
		Platform[] tempPlat = currentMap.getPlatformArray();
		for(Platform p: tempPlat)
			platformList.add(p);

		try {	//Load in all required sprites and images
			imageMap.put("fireball", ImageIO.read(new File("fireball.png")));
			imageMap.put("sword", ImageIO.read(new File("sword.png")));
			imageMap.put("axe", ImageIO.read(new File("axe.png")));
			imageMap.put("laser", ImageIO.read(new File("laser.png")));
			imageMap.put("arrow", ImageIO.read(new File("arrow.png")));
			imageMap.put("arrowFlipped", toBufferedImage(flip(ImageIO.read(new File("arrow.png")))));
			imageMap.put("lightning", ImageIO.read(new File("thunderbolt.png")));
			imageMap.put("hand", ImageIO.read(new File("hand.png")));
			imageMap.put("handFlipped", toBufferedImage(flip(ImageIO.read(new File("hand.png")))));
			
			imageMap.put("frame1", toBufferedImage(ImageIO.read(new File("frame2.gif")).getScaledInstance(560, 230, Image.SCALE_SMOOTH)));
			imageMap.put("frame2", toBufferedImage(ImageIO.read(new File("frame3.gif")).getScaledInstance(560, 170, Image.SCALE_SMOOTH)));
			imageMap.put("frame3", toBufferedImage(ImageIO.read(new File("frame4.gif")).getScaledInstance(560, 160, Image.SCALE_SMOOTH)));	
			imageMap.put("frame4", toBufferedImage(ImageIO.read(new File("frame5.gif")).getScaledInstance(560, 150, Image.SCALE_SMOOTH)));	
			imageMap.put("frame5", toBufferedImage(ImageIO.read(new File("frame6.gif")).getScaledInstance(560, 140, Image.SCALE_SMOOTH)));	
			imageMap.put("frame6", toBufferedImage(ImageIO.read(new File("frame7.gif")).getScaledInstance(560, 130, Image.SCALE_SMOOTH)));	
			imageMap.put("frame7", toBufferedImage(ImageIO.read(new File("frame8.gif")).getScaledInstance(560, 130, Image.SCALE_SMOOTH)));	
			imageMap.put("frame8", toBufferedImage(ImageIO.read(new File("frame9.gif")).getScaledInstance(560, 130, Image.SCALE_SMOOTH)));	
			imageMap.put("frame9", toBufferedImage(ImageIO.read(new File("frame10.gif")).getScaledInstance(560, 130, Image.SCALE_SMOOTH)));	
			imageMap.put("frame10", toBufferedImage(ImageIO.read(new File("frame11.gif")).getScaledInstance(560, 130, Image.SCALE_SMOOTH)));	
			imageMap.put("frame11", toBufferedImage(ImageIO.read(new File("frame12.gif")).getScaledInstance(560, 130, Image.SCALE_SMOOTH)));	
			
			imageMap.put("yoshi", toBufferedImage(ImageIO.read(new File("yoshi.png")).getScaledInstance(40, 50, Image.SCALE_SMOOTH)));
			imageMap.put("mario", toBufferedImage(ImageIO.read(new File("mario.png")).getScaledInstance(40, 50, Image.SCALE_SMOOTH)));
			imageMap.put("link", toBufferedImage(ImageIO.read(new File("link.png")).getScaledInstance(40, 50, Image.SCALE_SMOOTH)));
			imageMap.put("donkey", toBufferedImage(ImageIO.read(new File("donkey.png")).getScaledInstance(40, 50, Image.SCALE_SMOOTH)));
			imageMap.put("kirby", toBufferedImage(ImageIO.read(new File("kirby.png")).getScaledInstance(40, 50, Image.SCALE_SMOOTH)));
			imageMap.put("samus", toBufferedImage(ImageIO.read(new File("samus.png")).getScaledInstance(40, 50, Image.SCALE_SMOOTH)));
			imageMap.put("fox", toBufferedImage(ImageIO.read(new File("fox.png")).getScaledInstance(40, 50, Image.SCALE_SMOOTH)));
			imageMap.put("pikachu", toBufferedImage(ImageIO.read(new File("pikachu.png")).getScaledInstance(40, 50, Image.SCALE_SMOOTH)));
		} catch(IOException e) {}
		
		frame.add(panel);

		frame.setLocationRelativeTo(null);	//Make the frame visible
		frame.setVisible(true);

		Thread closeThread = new Thread(new Runnable() {
			public void run() {
				while(!quit) {
					try { Thread.sleep(10);} catch (InterruptedException e) {}
					
				}
				frame.dispose();
				new MainMenu();
			}
		});
		Thread animationThread = new Thread(new Runnable() {	//The main loop
			public void run() {
				while(!quit) {
					while (!paused) {
						if(P1IsShooting || P1HeavyAttack) physicsObjectList.get(0).swingWeapon(P1Block, P1IsShooting, P1HeavyAttack);
						if(P2IsShooting || P2HeavyAttack) physicsObjectList.get(1).swingWeapon(P2Block, P2IsShooting, P2HeavyAttack);

						frame.repaint();	//Refresh frame and panel
						panel.repaint();
						try {Thread.sleep(17);} catch (Exception ex) {}	//10 millisecond delay between each refresh
					}
				}
			}
		});			
		closeThread.start();
		animationThread.start();	//Start the main loop
	}

	public static void closeAll() {
		quit = true;
		paused = true;
	}

	public static Image flip(BufferedImage original) {	//Flip image in parameter, then return it
		BufferedImage img = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int i = original.getWidth()-1; i>0; i--)
			for(int j=0; j<original.getHeight(); j++)
				img.setRGB(original.getWidth()-i, j, original.getRGB(i, j));
		return img;
	}

	public static BufferedImage toBufferedImage(Image img) {	//Convert Image into BufferedImage, returns Image
		if(img instanceof BufferedImage) return (BufferedImage) img;

		BufferedImage temp = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = temp.createGraphics();
		graphics.drawImage(img, 0, 0, null);
		graphics.dispose();

		return temp;
	}

	public void clearAll() {	//Clear all components from panel
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
	}

	public void keyTyped(KeyEvent e) {}		//KeyListener is an interface so must implement all empty methods, this one is just useless

	public void keyPressed(KeyEvent e) {	//When the keys are pressed (when they're released is the method after this one)
		//Arrow keys for object1
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) physicsObjectList.get(0).moveX(4);	// "VK_RIGHT" --> Right arrow 
		if(e.getKeyCode() == KeyEvent.VK_LEFT) physicsObjectList.get(0).moveX(-4);	//Get the first object from physicsObjectList, positive X moves right

		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(physicsObjectList.get(0).getNumJumps()<=1) physicsObjectList.get(0).moveY(-10);	//Negative Y moves up
		}
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN)	//Move character down past ramp
			physicsObjectList.get(0).moveY(10);

		if(e.getKeyCode() == KeyEvent.VK_PERIOD && !P1HeavyAttack && !P1Block)	P1IsShooting = true;	//Attack keys for player 1
		if(e.getKeyCode() == KeyEvent.VK_SLASH && !P1IsShooting && !P1Block) P1HeavyAttack = true;
		if(e.getKeyCode() == KeyEvent.VK_COMMA && !P1HeavyAttack && !P1HeavyAttack) P1Block = true;

		//WASD for object2
		if(e.getKeyCode() == KeyEvent.VK_D) physicsObjectList.get(1).moveX(4);	//"VK_*LETTER*" --> "D" key
		if(e.getKeyCode() == KeyEvent.VK_A) physicsObjectList.get(1).moveX(-4);	//physicsObjectList.get(1): gets second obejct (the only two are the boxes)

		if(e.getKeyCode() == KeyEvent.VK_W) {
			if(physicsObjectList.get(1).getNumJumps()<=1) physicsObjectList.get(1).moveY(-10);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_S)
			physicsObjectList.get(1).moveY(10);

		if(e.getKeyCode() == KeyEvent.VK_X && !P2HeavyAttack && !P2Block)	P2IsShooting = true;	//Attack keys for player 2
		if(e.getKeyCode() == KeyEvent.VK_C && !P2IsShooting && !P2Block) P2HeavyAttack = true;
		if(e.getKeyCode() == KeyEvent.VK_Z && !P2HeavyAttack && !P2HeavyAttack) P2Block = true;
	}

	public void keyReleased(KeyEvent e) {	//When the keys are released
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) physicsObjectList.get(1).moveY(0);
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) physicsObjectList.get(0).moveY(0);
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) physicsObjectList.get(0).moveX(0); //Object1
		if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) physicsObjectList.get(1).moveX(0); //Object2
		
		if(e.getKeyCode() == KeyEvent.VK_COMMA)	{
			P1Block = false;
			physicsObjectList.get(0).stopBlocking();
		}
		if(e.getKeyCode() == KeyEvent.VK_PERIOD) P1IsShooting = false;
		if(e.getKeyCode() == KeyEvent.VK_SLASH) P1HeavyAttack = false;
		
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			P2Block = false;
			physicsObjectList.get(1).stopBlocking();
		}
		if(e.getKeyCode() == KeyEvent.VK_X) P2IsShooting = false;
		if(e.getKeyCode() == KeyEvent.VK_C) P2HeavyAttack = false;
	}

	public static void playSound(String name) {	//Play a sound
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(name + ".wav"));
			soundMap.put(name, AudioSystem.getClip());
			soundMap.get(name).open(audioInputStream);
			
			if(name.equalsIgnoreCase("main theme") && Settings.music == 0) soundMap.get(name).loop(Clip.LOOP_CONTINUOUSLY);
			if(name.equalsIgnoreCase("main theme") && Settings.music != 0) if(Physics.soundMap.get("main theme") != null) Physics.soundMap.get("main theme").stop();
			
			if(Settings.currentChoiceEffects==2 && !name.equalsIgnoreCase("main theme")) soundMap.get(name).start();
		} catch(Exception ex) {}
	}
	
	public static class Canvas extends JPanel {	//Make a new JPanel which unlike regular JPanels you can draw objects onto 
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.drawImage(backgroundImage, 0, 0, null);
			
			for(int i=0; i<physicsObjectList.size(); i++) //Draw objects from physicsObjectList
				physicsObjectList.get(i).draw(g);

			for(int l=0; l<projectileList.size(); l++) {	//Draw projectiles
				if(projectileList.get(l).attack()) projectileList.remove(l);	//Returns true if at edge of screen, removes object
				else {
					projectileList.get(l).draw(g);	//If not at edge of screen, continue drawing object
					for(int j=0; j<physicsObjectList.size(); j++) {	//Checks if projectile has hit one of the player objects
						for(int k=0; k<projectileList.size(); k++) {
							if(Math.abs(projectileList.get(k).getX()-physicsObjectList.get(j).getX()) <= 5 && Math.abs(projectileList.get(k).getY()-physicsObjectList.get(j).getY()) <= 15 && projectileList.get(k).getOwner() != physicsObjectList.get(j)) {
								physicsObjectList.get(Math.abs(j-1)).dealDamage(projectileList.get(k).getDamage(), physicsObjectList.get(j));
								projectileList.remove(k);
							}
						}
					}
				}
			}

			for(int j=0; j<weaponList.size(); j++)	//Draw objects in weaponList
				weaponList.get(j).draw(g);
		}
	}

	public static void main(String[] args) {	//Call the graphics constructor
		new Physics();
	}
}

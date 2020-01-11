import javax.imageio.ImageIO;
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
	
	public static HashMap<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();

	public static boolean paused = false;
	public static boolean quit = false;
	
	public static boolean P1IsShooting = false;
	public static boolean P2IsShooting = false;

	public static Image backgroundImage = Toolkit.getDefaultToolkit().createImage("better.jpg").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);

	public static JFrame frame;
	public static JPanel panel = new Canvas();	//Canvas is a method which creates a panel that you can "draw" objects onto

	public Physics() {
		frame = new JFrame("Super Smash");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);		

		panel.setLayout(null);

		physicsObjectList.add(new PhysicsObject(1, "yoshi.png", "sword.png", false, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 30, 40, 33.3, 10));
		physicsObjectList.add(new PhysicsObject(2, "yoshi.png", "sword.png", false, ThreadLocalRandom.current().nextInt(550, 750 + 1), 100, 30, 40, 33.3, 20));

		platformList.add(new Platform(400, 92 ,101, 15, false, true));
		platformList.add(new Platform(280, 170 ,102, 15, false, true));
		platformList.add(new Platform(519, 170 ,102, 15, false, true));
		platformList.add(new Platform(400, 245 ,101, 15, false, true));
		platformList.add(new Platform(158, 245 ,107, 15, false, true));
		platformList.add(new Platform(637, 245 ,105, 15, false, true));
		platformList.add(new Platform(90, 315 ,710, 25, false, true));
		
		platformList.add(new Platform(30, 330, 60, 25, true, true));
		platformList.add(new Platform(800, 330, 60, 25, true, false));
		
		try {
			imageMap.put("fireball", ImageIO.read(new File("fireball.png")));
			imageMap.put("sword", ImageIO.read(new File("sword.png")));
			imageMap.put("hand", ImageIO.read(new File("hand.png")));
			imageMap.put("handFlipped", toBufferedImage(flip(ImageIO.read(new File("hand.png")))));
			imageMap.put("steam", ImageIO.read(new File("steam.png")));
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
				new mainMenu();
			}
		});
		Thread animationThread = new Thread(new Runnable() {	//The main loop
			public void run() {
				while(!quit) {
					while (!paused) {
						if(P1IsShooting) physicsObjectList.get(0).swingWeapon();
						if(P2IsShooting) physicsObjectList.get(1).swingWeapon();
						
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

		if(e.getKeyCode() == KeyEvent.VK_COMMA)	P1IsShooting = true;
			

		//WASD for object2
		if(e.getKeyCode() == KeyEvent.VK_D) physicsObjectList.get(1).moveX(4);	//"VK_*LETTER*" --> "D" key
		if(e.getKeyCode() == KeyEvent.VK_A) physicsObjectList.get(1).moveX(-4);	//physicsObjectList.get(1): gets second obejct (the only two are the boxes)

		if(e.getKeyCode() == KeyEvent.VK_W) {
			if(physicsObjectList.get(1).getNumJumps()<=1) physicsObjectList.get(1).moveY(-10);
		}

		if(e.getKeyCode() == KeyEvent.VK_X) P2IsShooting = true;
	}

	public void keyReleased(KeyEvent e) {	//When the keys are released
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) physicsObjectList.get(0).moveX(0); //Object1
		if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) physicsObjectList.get(1).moveX(0); //Object2
		if(e.getKeyCode() == KeyEvent.VK_COMMA)	P1IsShooting = false;
		if(e.getKeyCode() == KeyEvent.VK_X) P2IsShooting = false;
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

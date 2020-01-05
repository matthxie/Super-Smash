import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Physics implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	public static final int height = 600;	//Window dimensions
	public static final int width = 900;

	//Put any and all objects you create into an ArrayList, the canvas method will draw their contents onto the panel
	public static ArrayList<PhysicsObject> physicsObjectList = new ArrayList<PhysicsObject>();	//All physics objects	
	public static ArrayList<Platform> platformList = new ArrayList<Platform>();	//All platform objects
	public static ArrayList<MeleeWeapon> weaponList = new ArrayList<MeleeWeapon>();	//All weapon objects
	public static ArrayList<ProjectileWeapon> projectileList = new ArrayList<ProjectileWeapon>(); //All projectiles 

	public static boolean paused = false;
	public static boolean quit = false;
	
	public static Image backgroundImage = Toolkit.getDefaultToolkit().createImage("better.jpg").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);
	public static Image fireball = Toolkit.getDefaultToolkit().createImage("fireball.png");
	public static Image sword = Toolkit.getDefaultToolkit().createImage("sword.png");

	
	public static JFrame frame;
	public static JPanel panel = new canvas();	//canvas is a method which creates a panel that you can "draw" objects onto

	public Physics() {
		frame = new JFrame("Super Smash");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);		

		panel.setLayout(null);

		physicsObjectList.add(new PhysicsObject("yoshi.png", "sword.png", true, ThreadLocalRandom.current().nextInt(100, 300 + 1), 100, 30, 40));
		physicsObjectList.add(new PhysicsObject("yoshi.png", "sword.png", false, ThreadLocalRandom.current().nextInt(550, 750 + 1), 100, 30, 40));

		platformList.add(new Platform(400, 92 ,101, 5));
		platformList.add(new Platform(280, 170 ,102, 5));
		platformList.add(new Platform(519, 170 ,102, 5));
		platformList.add(new Platform(400, 245 ,101, 5));
		platformList.add(new Platform(158, 245 ,107, 5));
		platformList.add(new Platform(637, 245 ,105, 5));
		platformList.add(new Platform(90, 315 ,710, 15));

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

	public void clearAll() {
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
	}

	public void keyTyped(KeyEvent e) {}		//KeyListener is an interface so must implement all empty methods, this one is just useless

	public void keyPressed(KeyEvent e) {	//When the keys are pressed (when they're released is the method after this one)
		//Arrow keys for object1
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) physicsObjectList.get(0).moveX(4);	// "VK_RIGHT" --> Right arrow 
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) physicsObjectList.get(0).moveX(-4);	//Get the first object from physicsObjectList, positive X moves right

		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(!physicsObjectList.get(0).fallingStatus()) physicsObjectList.get(0).moveY(-10);	//Negative Y moves up
		}

		if(e.getKeyCode() == KeyEvent.VK_COMMA)	//Attack animation
			physicsObjectList.get(0).swingWeapon();

		//WASD for object2
		if(e.getKeyCode() == KeyEvent.VK_D) physicsObjectList.get(1).moveX(4);	//"VK_*LETTER*" --> "D" key
		else if(e.getKeyCode() == KeyEvent.VK_A) physicsObjectList.get(1).moveX(-4);	//physicsObjectList.get(1): gets second obejct (the only two are the boxes)

		if(e.getKeyCode() == KeyEvent.VK_W) {
			if(!physicsObjectList.get(1).fallingStatus()) physicsObjectList.get(1).moveY(-10);
		}

		if(e.getKeyCode() == KeyEvent.VK_X) 	//Attack animation
			physicsObjectList.get(1).swingWeapon();
	}

	public void keyReleased(KeyEvent e) {	//When the keys are released
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) physicsObjectList.get(0).moveX(0); //Object1
		if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) physicsObjectList.get(1).moveX(0); //Object2
	}

	public static class canvas extends JPanel {	//Make a new JPanel which unlike regular JPanels you can draw objects onto 
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.drawImage(backgroundImage, 0, 0, null);

			for(int i=0; i<physicsObjectList.size(); i++) //Draw objects from physicsObjectList
				physicsObjectList.get(i).draw(g);

			for(int l=0; l<projectileList.size(); l++) {	//Draw projectiles
				if(projectileList.get(l).attack()) projectileList.remove(l);
				else {
					projectileList.get(l).draw(g);
					for(int j=0; j<physicsObjectList.size(); j++) 
						for(int k=0; k<projectileList.size(); k++) {
							if(Math.abs(projectileList.get(k).getX()-physicsObjectList.get(j).getX()) <= 5 && Math.abs(projectileList.get(k).getY()-physicsObjectList.get(j).getY()) <= 15 && projectileList.get(k).getOwner() != physicsObjectList.get(j)) {
								physicsObjectList.get(Math.abs(j-1)).dealDamage(projectileList.get(k).getDamage(), -physicsObjectList.get(j).getOrientation(), physicsObjectList.get(j));
								projectileList.remove(k);
							}
						}
				}
			}

			for(int j=0; j<weaponList.size(); j++)	//Draw contents in weaponList
				weaponList.get(j).draw(g);
		}
	}

	public static void main(String[] args) {	//Call the graphics constructor
		new Physics();
	}
}

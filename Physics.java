import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Physics implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	public static final int height = 400;	//Window dimensions
	public static final int width = 600;

	//Put any and all objects you create into an ArrayList, the whiteboard method will draw their contents onto the panel
	//Might have to make a new object class containing non physics stuff and use the AnyOtherObject ArrayList for platforms and decorations
	public static ArrayList<PhysicsObject> physicsObjectList = new ArrayList<PhysicsObject>();	//All the physics objects
	//public static Arraylist<AnyOtherObject> normalObjectList = new Arraylist<AnyOtherObject>(); //All the non physics objects

	JFrame frame;	
	JPanel panel = new whiteboard();	//whiteboard is a method which creates a panel that you can "draw" objects onto

	public Physics() {
		frame = new JFrame("Gravity???????");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);	
		
		panel.setLayout(new BorderLayout());	
		
		physicsObjectList.add(new PhysicsObject(ThreadLocalRandom.current().nextInt(50, 350 + 1), 200, Color.red));	//Add the two blocks to ArrayList
		physicsObjectList.add(new PhysicsObject(ThreadLocalRandom.current().nextInt(50, 350 + 1), 200, Color.blue));

		frame.add(panel);

		frame.setLocationRelativeTo(null);	//Make the frame visible
		frame.setVisible(true);

		Thread animationThread = new Thread(new Runnable() {	//The main loop
			public void run() {	
				while (true) {	
					frame.repaint();	//Refresh frame and panel
					panel.repaint();
					try {Thread.sleep(10);} catch (Exception ex) {}	//10 millisecond delay between each refresh
				}
			}
		});	
		animationThread.start();	//Start the main loop
	}

	public void keyTyped(KeyEvent e) {}		//KeyListener is an interface so must implement all empty methods, this one is just useless

	public void keyPressed(KeyEvent e) {	//When the keys are pressed (when they're released is the method after this one)
		//Arrow keys for object1
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) physicsObjectList.get(0).moveX(4);	// "VK_RIGHT" --> Right arrow 
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) physicsObjectList.get(0).moveX(-4);	//Get the first object from physicsObjectList, positive X moves right

		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(!physicsObjectList.get(0).fallingStatus()) physicsObjectList.get(0).moveY(-12);	//Negative Y moves up
		}

		//WASD for object2
		if(e.getKeyCode() == KeyEvent.VK_D) physicsObjectList.get(1).moveX(4);	//"VK_*LETTER*" --> "D" key
		else if(e.getKeyCode() == KeyEvent.VK_A) physicsObjectList.get(1).moveX(-4);	//physicsObjectList.get(1): gets second object (the only two are the boxes)

		if(e.getKeyCode() == KeyEvent.VK_W) {
			if(!physicsObjectList.get(1).fallingStatus()) physicsObjectList.get(1).moveY(-12);
		}
	}

	public void keyReleased(KeyEvent e) {	//When the keys are released
		//Object1
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) physicsObjectList.get(0).moveX(0);

		//Object2
		if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) physicsObjectList.get(1).moveX(0);
	}

	public static void main(String[] args) {	//Call the graphics constructor
		new Physics();
	}

	public class whiteboard extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			for(int i=0; i<physicsObjectList.size(); i++) //Draws all the objects from physicsObjectList
				physicsObjectList.get(i).draw(g);
		}
	}
}
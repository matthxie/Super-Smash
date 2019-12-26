import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class mainMenu implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	public static final int height = 600;	//Window dimensions
	public static final int width = 900;

	public static boolean start = true;

	public static int selection = 1;
		
	JFrame frame;	
	JPanel panel = new JPanel();	
	
	public mainMenu() {
		frame = new JFrame("Super Smash");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);
		
		panel.setLayout(new BorderLayout());	
		
		selectionMenu(1);
		
		frame.add(panel);
		
		frame.setLocationRelativeTo(null);	//Make the frame visible
		frame.setVisible(true);	
	}
	
	public void selectionMenu(int selection) {
		clearAll();
		putImage("smashMenu" + selection + ".png");
	}
	
	public void putImage(String img) {
		ImageIcon icon = new ImageIcon(img);
		Image image = icon.getImage().getScaledInstance(width-20, height-40, java.awt.Image.SCALE_SMOOTH);
		JLabel pic = new JLabel(new ImageIcon(image));
		panel.add(pic);
	}
	
	public void clearAll() {
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
	}

	public void keyTyped(KeyEvent e) {}	//KeyListener is an interface so must implement all empty methods, this one is just useless

	public void keyPressed(KeyEvent e) {	//When the keys are pressed (when they're released is the method after this one)

			
		if(e.getKeyCode() == KeyEvent.VK_DOWN && start && selection < 5) {
			selection++;
			selectionMenu(selection);
		}
		if(e.getKeyCode() == KeyEvent.VK_UP && start && selection > 1) {
			selection--;
			selectionMenu(selection);
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			clearAll();
			if(selection == 1) new Physics();
		}
	}

	public void keyReleased(KeyEvent e) {}

	public static void main(String[] args) {	//Call the graphics constructor
		new mainMenu();
	}
}
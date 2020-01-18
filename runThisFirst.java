import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class runThisFirst implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private static final int height = 600;	//Window dimensions
	private static final int width = 900;

	static JFrame frame;	
	static JPanel panel = new JPanel();	

	public runThisFirst() {
		frame = new JFrame("Menu");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);

		panel.setLayout(new BorderLayout());	
		ImageIcon icon = new ImageIcon("smashBrosEntry.png");
		Image image = icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		JLabel pic = new JLabel(new ImageIcon(image));
		panel.add(pic);
		
		frame.add(panel);

		frame.setLocationRelativeTo(null);	//Make the frame visible
		frame.setVisible(true);
		
		Physics.playSound("main theme");
	}

	public void keyTyped(KeyEvent e) {}	//KeyListener is an interface so must implement all empty methods, this one is just useless
 
	public void keyPressed(KeyEvent e) {	//When the keys are pressed (when they're released is the method after this one)
		new mainMenu();
		frame.dispose();
	}

	public void keyReleased(KeyEvent e) {}

	public static void main(String[] args) {	//Call the graphics constructor
		new runThisFirst();
	}
}

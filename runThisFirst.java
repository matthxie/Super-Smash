import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class runThisFirst implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	public static final int height = 600;	//Window dimensions
	public static final int width = 900;

	public static boolean start = false;

	public static int selection = 1;

	JFrame frame;	
	JPanel panel = new JPanel();	
	
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
	}
	
	
	public void putImage(String img) {
		ImageIcon icon = new ImageIcon(img);
		Image image = icon.getImage().getScaledInstance(620, 370, java.awt.Image.SCALE_SMOOTH);
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
		frame.setVisible(false);
		new mainMenu();
	}

	public void keyReleased(KeyEvent e) {}

	public static void main(String[] args) {	//Call the graphics constructor
		new runThisFirst();
	}
}
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


public class Settings implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int rectX,rectY,rectWidth,rectHeight;
	private int currentSelection = 0;
	private final int height = 600;	//Window dimensions
	private final int width = 900;

	public static int music = 0, currentChoiceEffects=2;

	private int[][] buttonBoundsX = {

			{288, 136},
			{450, 136},
			{287, 136},
			{452, 136},
			{348, 175},

	};
	private int[][] buttonBoundsY = {

			{215, 65},
			{215, 65},
			{379, 65},
			{379, 65},
			{489, 69},
	};

	private boolean closed = false;
	private Image backgroundImg  = Toolkit.getDefaultToolkit().createImage("SettingsMenuImage.png").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);

	private JFrame frame;	
	private JPanel panel = new canvas();	

	public Settings() {

		setDrawnSelection();
		frame = new JFrame("Settings");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);		

		panel.setLayout(new BorderLayout());	

		frame.add(panel);

		frame.setLocationRelativeTo(null);	//Make the frame visible
		frame.setVisible(true);	
		Thread updateMenu = new Thread(new Runnable() {	//The main loop
			public void run() {	

				while (!closed) {	
					frame.repaint();	//Refresh frame and panel
					panel.repaint();
					try {Thread.sleep(17);} catch (Exception ex) {}	//10 millisecond delay between each refresh
				}
				frame.dispose();
			}
		});	
		updateMenu.start();	//Start the main loop

	}
	private void setDrawnSelection() {
		rectX = buttonBoundsX[currentSelection][0];
		rectY = buttonBoundsY[currentSelection][0];
		rectWidth = buttonBoundsX[currentSelection][1];
		rectHeight = buttonBoundsY[currentSelection][1];

	}
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {		
		if(e.getKeyCode() == KeyEvent.VK_LEFT && currentSelection >0) {
			currentSelection--;
			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT && currentSelection < 4) {
			currentSelection++;
			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(currentSelection==4) {
				new MainMenu();
				frame.dispose();
			}	
			else if(currentSelection <= 1) {
				music = currentSelection;
			}
			else if(currentSelection > 1) {
				currentChoiceEffects = currentSelection;
			}
		}
	}

	public void keyReleased(KeyEvent e) {}

	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.setColor(new Color(200, 0,0));
			g.drawImage(backgroundImg, 0, 0, null);
			g.setColor(new Color(0,0,255));

			g.drawRect(buttonBoundsX[music][0], buttonBoundsY[music][0], buttonBoundsX[music][1], buttonBoundsY[music][1]);
			g.drawRect(buttonBoundsX[music][0]+1, buttonBoundsY[music][0]+1, buttonBoundsX[music][1]-2, buttonBoundsY[music][1]-2);
			g.drawRect(buttonBoundsX[music][0]-1, buttonBoundsY[music][0]-1, buttonBoundsX[music][1]+2, buttonBoundsY[music][1]+2);

			g.drawRect(buttonBoundsX[currentChoiceEffects][0], buttonBoundsY[currentChoiceEffects][0], buttonBoundsX[currentChoiceEffects][1], buttonBoundsY[currentChoiceEffects][1]);
			g.drawRect(buttonBoundsX[currentChoiceEffects][0]+1, buttonBoundsY[currentChoiceEffects][0]+1, buttonBoundsX[currentChoiceEffects][1]-2, buttonBoundsY[currentChoiceEffects][1]-2);
			g.drawRect(buttonBoundsX[currentChoiceEffects][0]-1, buttonBoundsY[currentChoiceEffects][0]-1, buttonBoundsX[currentChoiceEffects][1]+2, buttonBoundsY[currentChoiceEffects][1]+2);
			g.setColor(new Color(255,0,0));
			g.drawRect(rectX, rectY, rectWidth, rectHeight);
			g.drawRect(rectX+1, rectY+1, rectWidth-2, rectHeight-2);
			g.drawRect(rectX-1, rectY-1, rectWidth+2, rectHeight+2);
		}
	}
}
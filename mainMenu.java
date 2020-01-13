import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class mainMenu implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int rectX,rectY,rectWidth,rectHeight;
	private int currentSelection = 0;
	private final int height = 600;	//Window dimensions
	private final int width = 900;
	private int[][] wordBoundsX= new int[][] {
		{610-5,200+10}, {500-5,395+9}, {560-5,300+10}
	};//Order: SMASH, How to play, settings
	private int[][] wordBoundsY = new int[][] {
		{240-5, 40+10},{340-5,40+10},{440-5,40+10}
	};
	private boolean closed = false;
	private Image backgroundImg  = Toolkit.getDefaultToolkit().createImage("betterIntroScreen.jpg").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);
	private Image smashImg  = Toolkit.getDefaultToolkit().createImage("SMASH.png").getScaledInstance(200, 40,java.awt.Image.SCALE_SMOOTH);
	private Image howToPlayImg  = Toolkit.getDefaultToolkit().createImage("HOWTOPLAY.png").getScaledInstance(395, 40,java.awt.Image.SCALE_SMOOTH);
	private Image settingsImg  = Toolkit.getDefaultToolkit().createImage("SETTINGS.png").getScaledInstance(300, 40,java.awt.Image.SCALE_SMOOTH);

	private JFrame frame;	
	private JPanel panel = new canvas();	

	public mainMenu() {
		
		setDrawnSelection();

		frame = new JFrame("Super Smash");	//Frame stuff
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
		rectX = wordBoundsX[currentSelection][0];
		rectY = wordBoundsY[currentSelection][0];
		rectWidth = wordBoundsX[currentSelection][1];
		rectHeight = wordBoundsY[currentSelection][1];

	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN && currentSelection < 2) {
			currentSelection++;
			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_UP&&currentSelection > 0) {
			currentSelection--;
			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			closed = true;
			if(currentSelection==0)new ChooseCharacterMenu();
			else if(currentSelection==1) new HowToPlayMenu();
			else if(currentSelection==2) ;
			else System.out.println("Houston we have a problem with the selection");
		}
		
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {	//Call the graphics constructor
		new mainMenu();
	}



	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.setColor(new Color(200, 0,0));
			g.drawImage(backgroundImg,0,0, null);
			g.drawImage(smashImg, 610, 240, null);
			g.drawImage(howToPlayImg, 500, 340, null);
			g.drawImage(settingsImg, 560, 440, null);
			g.drawRect(rectX, rectY, rectWidth, rectHeight);
			g.drawRect(rectX+1, rectY+1, rectWidth-2, rectHeight-2);
			g.drawRect(rectX-1, rectY-1, rectWidth+2, rectHeight+2);
		}
	}
}
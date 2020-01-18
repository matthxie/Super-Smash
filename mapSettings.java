import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;


public class mapSettings implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int currentSelection = 0;
	private final int height = 600;	//Window dimensions
	private final int width = 900;
	public static int currentChoiceTop = 0, currentChoiceBottom=2;
	
	private int[][] buttonBoundsX = {
			
			{238, 175}, 
			{512, 175},
			{238, 175}, 
			{511, 175}, 
			{373, 175}
			

	};
	private int[][] buttonBoundsY = {
			
			{190, 69},
			{191, 69},
			{380, 69},
			{379, 69},
			{492, 69},
	};

	private boolean closed = false;
	private Image backgroundImg  = Toolkit.getDefaultToolkit().createImage("mapSettings.png").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);

	private JFrame frame;	
	private JPanel panel = new canvas();	

	public mapSettings() {

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
	
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT && currentSelection > 0) {
			currentSelection--;
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT&&currentSelection < 4) {
			currentSelection++;
//			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(currentSelection==4) {
				frame.dispose();
				if(currentChoiceTop == 0) {
					if(currentChoiceBottom == 2) {
						Arrays.sort(ChooseMapMenu.allMapArray, new SortByIncreasingDifficulty()); //DIFFICULTY TITLE INCREASING DECREASING
					}
					else{
						Arrays.sort(ChooseMapMenu.allMapArray, new SortByDecreasingDiff()); //DIFFICULTY TITLE INCREASING DECREASING

					}
				}
				else{
					if(currentChoiceBottom == 2) {
						Arrays.sort(ChooseMapMenu.allMapArray, new SortByIncreasingTitle()); //DIFFICULTY TITLE INCREASING DECREASING
					}
					else{
						Arrays.sort(ChooseMapMenu.allMapArray, new SortByDecreasingTitle()); //DIFFICULTY TITLE INCREASING DECREASING

					}
				}
				new ChooseMapMenu();
				closed = true;
			}
			else if(currentSelection <= 1) {
				currentChoiceTop = currentSelection;
			}
			else if(currentSelection> 1) {
				currentChoiceBottom = currentSelection;
			}
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {	//Call the graphics constructor
		new mapSettings();
	}



	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.setColor(new Color(34, 33,34));
			g.fillRect(0, 0, width, height);
			g.drawImage(backgroundImg,0-10,0, null);
			
			
			g.setColor(new Color(0,0,255));
			
			g.drawRect(buttonBoundsX[currentChoiceTop][0], buttonBoundsY[currentChoiceTop][0], buttonBoundsX[currentChoiceTop][1], buttonBoundsY[currentChoiceTop][1]);
			g.drawRect(buttonBoundsX[currentChoiceTop][0]+1, buttonBoundsY[currentChoiceTop][0]+1, buttonBoundsX[currentChoiceTop][1]-2, buttonBoundsY[currentChoiceTop][1]-2);
			g.drawRect(buttonBoundsX[currentChoiceTop][0]-1, buttonBoundsY[currentChoiceTop][0]-1, buttonBoundsX[currentChoiceTop][1]+2, buttonBoundsY[currentChoiceTop][1]+2);
			
			g.drawRect(buttonBoundsX[currentChoiceBottom][0], buttonBoundsY[currentChoiceBottom][0], buttonBoundsX[currentChoiceBottom][1], buttonBoundsY[currentChoiceBottom][1]);
			g.drawRect(buttonBoundsX[currentChoiceBottom][0]+1, buttonBoundsY[currentChoiceBottom][0]+1, buttonBoundsX[currentChoiceBottom][1]-2, buttonBoundsY[currentChoiceBottom][1]-2);
			g.drawRect(buttonBoundsX[currentChoiceBottom][0]-1, buttonBoundsY[currentChoiceBottom][0]-1, buttonBoundsX[currentChoiceBottom][1]+2, buttonBoundsY[currentChoiceBottom][1]+2);
			g.setColor(new Color(255,0,0));
			
			g.drawRect(buttonBoundsX[currentSelection][0], buttonBoundsY[currentSelection][0], buttonBoundsX[currentSelection][1], buttonBoundsY[currentSelection][1]);
			g.drawRect(buttonBoundsX[currentSelection][0]+1, buttonBoundsY[currentSelection][0]+1, buttonBoundsX[currentSelection][1]-2, buttonBoundsY[currentSelection][1]-2);
			g.drawRect(buttonBoundsX[currentSelection][0]-1, buttonBoundsY[currentSelection][0]-1, buttonBoundsX[currentSelection][1]+2, buttonBoundsY[currentSelection][1]+2);
			
		}
	}
}

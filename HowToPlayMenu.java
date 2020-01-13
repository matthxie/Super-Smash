import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class HowToPlayMenu implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int rectX,rectY,rectWidth,rectHeight;
	private int currentSelection = 0;
	private final int height = 600;	//Window dimensions
	private final int width = 900;

	private int[][] buttonBoundsX = {
			{48, 175},
			{668, 175}

	};
	private int[][] buttonBoundsY = {
			{488, 69},
			{488, 69}
	};

	private boolean closed = false;
	private Image backgroundImg  = Toolkit.getDefaultToolkit().createImage("MOVEMENTCONTROKS.png").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);

	private JFrame frame;	
	private JPanel panel = new canvas();	

	public HowToPlayMenu() {

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
		rectX = buttonBoundsX[currentSelection][0];
		rectY = buttonBoundsY[currentSelection][0];
		rectWidth = buttonBoundsX[currentSelection][1];
		rectHeight = buttonBoundsY[currentSelection][1];

	}
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT && currentSelection >0) {
			currentSelection--;
			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT&&currentSelection < 1) {
			currentSelection++;
			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(currentSelection==0) {
				closed = true;
				new mainMenu();
			}
			else if(currentSelection==1) {
				backgroundImg = Toolkit.getDefaultToolkit().createImage("MOVEMENTCONTROKS.png").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);

			}
			System.out.println("Houston we have a problem with the selection");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {	//Call the graphics constructor
		new HowToPlayMenu();
	}



	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.setColor(new Color(200, 0,0));
			g.drawImage(backgroundImg,0,0, null);

			g.drawRect(rectX, rectY, rectWidth, rectHeight);
			g.drawRect(rectX+1, rectY+1, rectWidth-2, rectHeight-2);
			g.drawRect(rectX-1, rectY-1, rectWidth+2, rectHeight+2);
		}
	}
}
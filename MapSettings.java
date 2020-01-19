import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;

public class MapSettings implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int rectX,rectY,rectWidth,rectHeight;
	private int currentSelection = 0;
	
	private boolean difficultySelected;
	
	private final int height = 600;	//Window dimensions
	private final int width = 900;
	
	
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

	public MapSettings() {

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
		if(e.getKeyCode() == KeyEvent.VK_LEFT && currentSelection > 0) {
			currentSelection--;
			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT&&currentSelection < 4) {
			currentSelection++;
			setDrawnSelection();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(currentSelection == 0) difficultySelected = true;
			if(currentSelection == 1) difficultySelected = false;
			
			if(currentSelection == 2 && difficultySelected) Collections.sort(ChooseMapMenu.allMapArray, new SortByDecreasingDiff());
			if(currentSelection == 2 && !difficultySelected) Collections.sort(ChooseMapMenu.allMapArray, new SortByDecreasingTitle());
			
			if(currentSelection == 3 && difficultySelected) Collections.sort(ChooseMapMenu.allMapArray, new SortByIncreasingDifficulty());
			if(currentSelection == 3 && !difficultySelected) Collections.sort(ChooseMapMenu.allMapArray, new SortByIncreasingTitle());
			
			if(currentSelection == 4) {
				frame.dispose();
				new ChooseMapMenu();
				closed = true;
			}
			
		}
	}

	public void keyReleased(KeyEvent e) {}

	public static void main(String[] args) {	//Call the graphics constructor
		new MapSettings();
	}

	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.setColor(new Color(34, 33,34));
			g.fillRect(0, 0, width, height);
			g.drawImage(backgroundImg,0-10,0, null);
			g.setColor(new Color(255,0,0));
			g.drawRect(rectX, rectY, rectWidth, rectHeight);
			g.drawRect(rectX+1, rectY+1, rectWidth-2, rectHeight-2);
			g.drawRect(rectX-1, rectY-1, rectWidth+2, rectHeight+2);
		}
	}
}
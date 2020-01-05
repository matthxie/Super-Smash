import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ChooseCharacterMenu implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int rectX,rectY,rectWidth,rectHeight;
	private int playerNumber = 1;
	private Font font = null;
	private int currentSelection = 0;
	private String playerOne,playerTwo;
	private String[] characterList = new String[] {"Mario", "Donkey Kong", "Link", "Samus", "Yoshi", "Kirby", "Fox", "Pikachu"};
	private final int height = 600;	//Window dimensions
	private final int width = 900;
	private Image chooseCharImage  = Toolkit.getDefaultToolkit().createImage("betterSmashChoose.png").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);

	private int[][] imageBoundsX= new int[][] {
		{97,140,0}, {238,141,0}, {379,141,0}, {521,141,0},{662,141,0}, {167,141,0}, {309,141,0}, {450,141,0}, {591, 143,0}
	};//Order: Mario, Donkey Kong, Link, Samus, Yoshi, Kirby, Fox, Pikachu, Random
	private int[][] imageBoundsY = new int[][] {
		{84, 121},{84,121},{84,121},{84,121},{84,121}, {206,122}, {206,122}, {206, 122}, {206,123}
	};//Order: Mario, Donkey Kong, Link, Samus, Yoshi, Kirby, Fox, Pikachu, Random

	private JFrame frame;	
	private JPanel panel = new canvas();	

	public ChooseCharacterMenu() {
		String fName = "superFont.ttf";
		File fontFile = new File(fName);		

		try {
			Font tempfont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			font = tempfont.deriveFont((float)(40));

		} catch (FontFormatException e) {
		} catch (IOException e) {
		}

		frame = new JFrame("Choose Your Character");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);		

		setDrawnSelection();
		panel.setLayout(new BorderLayout());	

		frame.add(panel);

		frame.setLocationRelativeTo(null);	//Make the frame visible
		frame.setVisible(true);	
		
		Thread drawSquares = new Thread(new Runnable() {	//The main loop
			public void run() {	

				while (playerNumber < 3) {	
					frame.repaint();	//Refresh frame and panel
					panel.repaint();
					try {Thread.sleep(17);} catch (Exception ex) {}	//10 millisecond delay between each refresh
				}
				new ChooseMapMenu();
			}
		});	
		drawSquares.start();	//Start the main loop

	}
	public void setDrawnSelection() {
		rectX = imageBoundsX[currentSelection][0];
		rectY = imageBoundsY[currentSelection][0];
		rectWidth = imageBoundsX[currentSelection][1];
		rectHeight = imageBoundsY[currentSelection][1];

	}


	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(playerNumber<3) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				if(currentSelection < 8) {
					if(imageBoundsX[currentSelection+1][2]==0) {
						currentSelection++;
					}
					else {
						currentSelection+= 2;
					}
				}
				else if(imageBoundsX[0][2]== 0) {
					currentSelection = 0;
				}
				else currentSelection = 1;

			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
				if(currentSelection > 1) {
					if(imageBoundsX[currentSelection-1][2]==0) {
						currentSelection--;
					}
					else {
						currentSelection-=2;
					}
				}
				else currentSelection = 8;

			else if(e.getKeyCode()== KeyEvent.VK_ENTER) {

				if(currentSelection == 8) randomPlayer();
				else if(playerNumber == 1) {
					playerOne = characterList[currentSelection];
					imageBoundsX[currentSelection][2] = 1;

				}
				else if(playerNumber == 2) {
					playerTwo = characterList[currentSelection];
					imageBoundsX[currentSelection][2] = 2;
				}
				frame.repaint();	//Refresh frame and panel
				panel.repaint();
				if(currentSelection < 7) {
					currentSelection++;
				}else currentSelection--;
				playerNumber++;
			}

			setDrawnSelection();
		}
	}
	public void randomPlayer() {
		double h = Math.random()*7;
		int tempSelect =(int) h;
		while(imageBoundsX[tempSelect][2] != 0) {
			System.out.println(tempSelect);
			h = Math.random()*7;
			tempSelect =(int) h;
		}
		System.out.println(tempSelect);

		if(playerNumber == 1) {
			playerOne = characterList[tempSelect];
			imageBoundsX[tempSelect][2] = 1;
		}
		else if(playerNumber == 2) {
			playerTwo = characterList[tempSelect];
			imageBoundsX[tempSelect][2] = 2;
		}

	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	public void putImage(String img) {
		ImageIcon icon = new ImageIcon(img);
		Image image = icon.getImage().getScaledInstance((int)(icon.getIconWidth()/1.2),(int)(icon.getIconHeight()/1.2), java.awt.Image.SCALE_SMOOTH);
		JLabel pic = new JLabel(new ImageIcon(image));
		panel.add(pic);
	}

	public void clearAll() {
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
	}

	public static void main(String[] args) {	//Call the graphics constructor
		new ChooseCharacterMenu();
	}



	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel

			g.setFont(font);
			g.drawString("Player "+ playerNumber +" ChOose Your Character", 80, 40);
			g.drawImage(chooseCharImage, 0, 0, null);

			for(int i = 0; i < imageBoundsX.length; i++) {
				if(imageBoundsX[i][2] == 1) {
					g.setColor(new Color(200,0,0));
					g.fillRect(imageBoundsX[i][0], imageBoundsY[i][0], imageBoundsX[i][1], imageBoundsY[i][1]);
				}
				else if(imageBoundsX[i][2] == 2) {
					g.setColor(new Color(0,0,200));
					g.fillRect(imageBoundsX[i][0], imageBoundsY[i][0], imageBoundsX[i][1], imageBoundsY[i][1]);
				}
			}
			if(playerNumber ==1) {
				g.setColor(new Color(200,0,0));
			}else {
				g.setColor(new Color(0,0,200));
			}
			g.drawRect(rectX, rectY, rectWidth, rectHeight);
			g.drawRect(rectX+1, rectY+1, rectWidth-2, rectHeight-2);
			g.drawRect(rectX-1, rectY-1, rectWidth+2, rectHeight+2);
			

		}
	}
}
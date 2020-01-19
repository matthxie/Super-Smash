import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ChooseCharacterMenu implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int rectX,rectY,rectWidth,rectHeight;
	private int playerNumber = 1;
	public static String playerOneChar = "mario";
	public static String playerTwoChar = "donkey"; 
	private Font font = null,fontSmaller=null, fontMedium=null;
	private int currentSelection = 0;
	private final int height = 600;	//Window dimensions
	private final int width = 900;
	private String[] characterList = {"Mario", "Donkey Kong",  "Link", "Samus", "Yoshi", "Kirby", "Fox", "Pikachu", "Random"};
	private String[] forMapCharList = {"mario", "donkey",  "link", "samus", "yoshi", "kirby", "fox", "pikachu", "random"};

	private Image chooseCharImage  = Toolkit.getDefaultToolkit().createImage("betterSmashChoose.png").getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);
	private int[][] imageBoundsX= new int[][] {
		{97,140,0}, {238,141,0}, {379,141,0}, {521,141,0},{662,141,0}, {167,141,0}, {309,141,0}, {450,141,0}, {591, 143,0}
	};//Order: Mario, Donkey Kong, Link, Samus, Yoshi, Kirby, Fox, Pikachu, Random
	private int[][] imageBoundsY = new int[][] {
		{84, 121},{84,121},{84,121},{84,121},{84,121}, {206,122}, {206,122}, {206, 122}, {206,123}
	};//Order: Mario, Donkey Kong, Link, Samus, Yoshi, Kirby, Fox, Pikachu, Random
	
	//Load in image screens for both players 
	private Image[] p1images = new Image[] {
			Toolkit.getDefaultToolkit().createImage("p1ScreenMario.png"),
			Toolkit.getDefaultToolkit().createImage("p1ScreenDonkey.png"),
			Toolkit.getDefaultToolkit().createImage("p1ScreenLink.png"),
			Toolkit.getDefaultToolkit().createImage("p1ScreenSamus.png"),
			Toolkit.getDefaultToolkit().createImage("p1ScreenYoshi.png"),
			Toolkit.getDefaultToolkit().createImage("p1ScreenKirby.png"),
			Toolkit.getDefaultToolkit().createImage("p1ScreenFox.png"),
			Toolkit.getDefaultToolkit().createImage("p1ScreenPikachu.png"),
			Toolkit.getDefaultToolkit().createImage("p1Screen.png"),


	};
	private Image[] p2images = new Image[] {
			Toolkit.getDefaultToolkit().createImage("p2ScreenMario.png"),
			Toolkit.getDefaultToolkit().createImage("p2ScreenDonkey.png"),
			Toolkit.getDefaultToolkit().createImage("p2ScreenLink.png"),
			Toolkit.getDefaultToolkit().createImage("p2ScreenSamus.png"),
			Toolkit.getDefaultToolkit().createImage("p2ScreenYoshi.png"),
			Toolkit.getDefaultToolkit().createImage("p2ScreenKirby.png"),
			Toolkit.getDefaultToolkit().createImage("p2ScreenFox.png"),
			Toolkit.getDefaultToolkit().createImage("p2ScreenPikachu.png"),
			Toolkit.getDefaultToolkit().createImage("p2Screen.png"),


	};
	private JFrame frame;	
	private JPanel panel = new canvas();	

	public ChooseCharacterMenu() {
		String fName = "superFont.ttf";
		File fontFile = new File(fName);		
		
		//Load in fonts
		try {
			Font tempfont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			font = tempfont.deriveFont((float)(40));
			fontSmaller = tempfont.deriveFont((float)(20));
			fontMedium = tempfont.deriveFont((float)(25));

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
				frame.dispose();
			}
		});	
		drawSquares.start();	//Start the main loop

	}
	//sets current object to highlight
	private void setDrawnSelection() {
		rectX = imageBoundsX[currentSelection][0];
		rectY = imageBoundsY[currentSelection][0]-20;
		rectWidth = imageBoundsX[currentSelection][1];
		rectHeight = imageBoundsY[currentSelection][1];

	}


	public void keyTyped(KeyEvent e) {}

	//Controls for going through character list
	public void keyPressed(KeyEvent e) {
		if(playerNumber<3) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {

				if(currentSelection < 8) {
					Physics.playSound("menuRight");

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
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				Physics.playSound("menuLeft");

				if((currentSelection-1 >= 0 && imageBoundsX[currentSelection-1][2]==0)) {
					currentSelection--;
				}
				else if (currentSelection > 1){
					currentSelection-=2;
				}
				else currentSelection = 8;
			}
			else if(e.getKeyCode()== KeyEvent.VK_ENTER) {
				Physics.playSound("menuSelect");

				if(currentSelection == 8) randomPlayer();
				else if(playerNumber == 1) {
					imageBoundsX[currentSelection][2] = 1;
					playerOneChar = forMapCharList[currentSelection];

				}
				else if(playerNumber == 2) {
					imageBoundsX[currentSelection][2] = 2;
					playerTwoChar = forMapCharList[currentSelection];

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
	//picks a random player
	public void randomPlayer() {
		double h = Math.random()*7;
		int tempSelect =(int) h;
		while(imageBoundsX[tempSelect][2] != 0) {
			System.out.println(tempSelect);
			h = Math.random()*7;
			tempSelect =(int) h;
		}
		

		if(playerNumber == 1) {
			playerOneChar = forMapCharList[currentSelection];
			System.out.println(tempSelect);
			imageBoundsX[tempSelect][2] = 1;
		}
		else if(playerNumber == 2) {
			playerTwoChar = forMapCharList[currentSelection];
			System.out.println(tempSelect);
			imageBoundsX[tempSelect][2] = 2;
		}

	}
	public void keyReleased(KeyEvent e) {}
	
	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.setColor(new Color(34,33,34));
			g.fillRect(0, 0, width, height);
			g.setColor(new Color(255,255,255));
			g.setFont(font);
			if(playerNumber == 1) {
				g.drawString("Player 1 ChOose Your Character", 80, 40);
			}
			else g.drawString("Player 2 ChOose Your Character", 80, 40);

			g.drawImage(chooseCharImage, 0, 0-20, null);
			g.drawImage(p1images[8], 0, 320,468,257, null);
			if(playerNumber == 1) {
				g.drawImage(p1images[currentSelection], 0, 320,468,257, null);
				g.setFont(fontMedium);
				FontMetrics mediumMetrics = g.getFontMetrics(fontMedium);
				int x=210 + (280 - mediumMetrics.stringWidth(characterList[currentSelection])) / 2;
				int y = 200 + ((400 - mediumMetrics.getHeight()) / 2) + mediumMetrics.getAscent();
				g.drawString(characterList[currentSelection], x,y);
				g.drawImage(p2images[8], 468, 319,468,257, null);
			}
			else {
				FontMetrics mediumMetrics = g.getFontMetrics(fontMedium);
				g.setFont(fontMedium);
				g.drawImage(p2images[currentSelection], 468, 319,468,257, null);
				int x=490 + (600 - mediumMetrics.stringWidth(characterList[currentSelection])) / 2;
				int y = 200 + ((400 - mediumMetrics.getHeight()) / 2) + mediumMetrics.getAscent();
				g.drawString(characterList[currentSelection], x,y);
			}
			//g.drawImage(pickP1Image, 0-9,0-82,null);
			for(int i = 0; i < imageBoundsX.length; i++) {
				g.setFont(fontSmaller);
				FontMetrics metrics = g.getFontMetrics(fontSmaller);
				if(imageBoundsX[i][2] == 1) {
					g.setColor(new Color(200,0,0));
					g.fillRect(imageBoundsX[i][0], imageBoundsY[i][0]-20, imageBoundsX[i][1], imageBoundsY[i][1]);
					g.setColor(new Color(0,0,0));
					g.drawRect(imageBoundsX[i][0], imageBoundsY[i][0]-20, imageBoundsX[i][1], imageBoundsY[i][1]);
					g.drawRect(imageBoundsX[i][0]+1, imageBoundsY[i][0]-20+1, imageBoundsX[i][1]-2, imageBoundsY[i][1]-2);
					g.drawRect(imageBoundsX[i][0]-1, imageBoundsY[i][0]-20-1, imageBoundsX[i][1]+2, imageBoundsY[i][1]+2);
					// Determine the X coordinate for the text
					int x=imageBoundsX[i][0] + (imageBoundsX[i][1] - metrics.stringWidth("Player 1")) / 2;
					// Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
					int y = imageBoundsY[i][0]-20 + ((imageBoundsY[i][1] - metrics.getHeight()) / 2) + metrics.getAscent();
					g.setColor(new Color(255,255,255));
					g.drawString("Player 1", x, y);
					g.drawImage(p1images[i], 0, 320,468,257, null);
				}
				else if(imageBoundsX[i][2] == 2) {
					g.setColor(new Color(0,0,200));
					g.fillRect(imageBoundsX[i][0], imageBoundsY[i][0]-20, imageBoundsX[i][1], imageBoundsY[i][1]);
					g.setColor(new Color(0,0,0));
					g.drawRect(imageBoundsX[i][0], imageBoundsY[i][0]-20, imageBoundsX[i][1], imageBoundsY[i][1]);
					g.drawRect(imageBoundsX[i][0]+1, imageBoundsY[i][0]-20+1, imageBoundsX[i][1]-2, imageBoundsY[i][1]-2);
					g.drawRect(imageBoundsX[i][0]-1, imageBoundsY[i][0]-1-20, imageBoundsX[i][1]+2, imageBoundsY[i][1]+2);
					// Determine the X coordinate for the text
					int x = imageBoundsX[i][0] + (imageBoundsX[i][1] - metrics.stringWidth("Player 2")) / 2;
					// Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
					int y = imageBoundsY[i][0]-20 + ((imageBoundsY[i][1] - metrics.getHeight()) / 2) + metrics.getAscent();
					g.setColor(new Color(25,255,255));
					g.drawString("Player 2", x, y);	
					g.drawImage(p2images[i], 468, 319,468,257, null);
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
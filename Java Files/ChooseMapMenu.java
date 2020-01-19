import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ChooseMapMenu implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int rectX=300,rectY=150,rectWidth=300,rectHeight=200;
	private int currentSelection = 0;
	private int nextSelection = 1;
	private int prevSelection = 5;
	private int nextNextSelection=2;
	private int prevPrevSelection=4;
	
	private boolean onSettings = false;
	
	private final static int height = 600;	//Window dimensions
	private final static int width = 900;
	
	private Font font = null;
	private final Image redCircle = Toolkit.getDefaultToolkit().createImage("redCircle.png").getScaledInstance(56, 56, java.awt.Image.SCALE_SMOOTH);

	private final Image settingsIcon = Toolkit.getDefaultToolkit().createImage("settingsIcon.png").getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
	private final Image starImage = Toolkit.getDefaultToolkit().createImage("superSmashDifficultyStars.png").getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
	
	private boolean ended=false;
	
	//CENTER OF SCREEN IS 450
	public JFrame frame;	
	private JPanel panel = new canvas();	

	//Store the location of platforms for each map
	public static ArrayList <Map> allMapArray = new ArrayList <Map>(
			Arrays.asList(
				new Map(Toolkit.getDefaultToolkit().createImage("SMASHMAP0.png").getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), new Platform[] {new Platform(140, 320 ,610, 25, false, false,false), new Platform(750, 330, 60, 5, true, false, false), new Platform(80, 330, 60, 5, true, true, false)}, 1, "FInAl DEstInAtIOn"),	
					new Map(Toolkit.getDefaultToolkit().createImage("SMASHMAP1.png").getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), new Platform[] {new Platform(388, 205 ,120, 5,false, false, false),new Platform(250, 290 ,120, 5, false, false,false),new Platform(529, 291 ,120, 5, false, false,false),new Platform(203, 370 ,490, 25, false, false,false), new Platform(693, 360, 60, 5, true, false, false), new Platform(143, 360, 60, 5, true, true, false)}, 1, "SUnrIsE"),
					new Map(Toolkit.getDefaultToolkit().createImage("SMASHMAP2.png").getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), new Platform[] {new Platform(400, 92 ,101, 5, false, false,false),new Platform(280, 170 ,102, 5, false, false,false),new Platform(519, 170 ,102, 5, false, false,false),new Platform(400, 245 ,101, 5, false, false,false),new Platform(158, 245 ,107, 5, false, false,false),new Platform(637, 245 ,105, 5, false, false,false),new Platform(100, 315 ,700, 25, false, false,false), new Platform(800, 305, 60, 5, true, false, false), new Platform(40, 305, 60, 5, true, true, false)}, 2, "BIg BAttlEfIEld"),
					new Map(Toolkit.getDefaultToolkit().createImage("SMASHMAP3.jpg").getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), new Platform[] {new Platform(215, 370 ,158, 5, false, false,false),new Platform(216, 460 ,465, 25, false, false,false),new Platform(676, 470, 60, 5, false, true, false), new Platform(156, 470, 60, 5, true, true, false),new Platform(371, 266 ,310, 5, false, false,false),new Platform(217, 164 ,465, 5,false, false, false), new Platform(682, 154, 60, 5, true, false, false), new Platform(157, 154, 60, 5, true, true, false),new Platform(217, 64 ,153, 5, false, false,false),new Platform(528, 64 ,156, 5, false, false,false)}, 4, "WrEcKiNG cReW"),	
					new Map(Toolkit.getDefaultToolkit().createImage("SMASHMAP4.png").getScaledInstance(width, height-20, java.awt.Image.SCALE_SMOOTH), new Platform[] {new Platform(85, 220 ,160, 5, false, false,false),new Platform(30, 450 ,120, 5,false, false, false),new Platform(632, 255 ,268, 5, false, false,false),new Platform(405, 375 ,495, 25,false, false, false),new Platform(780, 365, 60, 5, true, false, false), new Platform(345, 365, 60, 5, true, true, false), new Platform(150, 190, 50, 240,false, false, true)}, 5, "SUzAkU CAstlE"),
					new Map(Toolkit.getDefaultToolkit().createImage("SMASHMAP5.png").getScaledInstance(width, height-20, java.awt.Image.SCALE_SMOOTH), new Platform[] {new Platform(65, 245 ,196, 5, false, false,false),new Platform(640, 245 ,196, 5, false, false,false),new Platform(194, 350 ,170, 5,false, false, false),new Platform(530, 345 ,172, 5, false, false,false),new Platform(324, 445 ,250, 5, false, false,false),new Platform(574, 435, 60, 5, true, false, false), new Platform(264, 435, 60, 5, true, true, false)}, 3, "NOrfAIr")	

			));
	
	public ChooseMapMenu() {
		frame = new JFrame("Choose Your Map");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);

		String fName = "superFont.ttf";	//Load in font file
		File fontFile = new File(fName);

		try {
			Font tempfont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			font = tempfont.deriveFont((float)(40));

		} catch (FontFormatException e) {
		} catch (IOException e) {
		}

		panel.setLayout(new BorderLayout());	

		frame.add(panel);

		frame.setLocationRelativeTo(null);	//Make the frame visible
		frame.setVisible(true);	
		Thread drawSquares = new Thread(new Runnable() {	//The main loop
			public void run() {	

				while (!ended) {	
					frame.repaint();	//Refresh frame and panel
					panel.repaint();
					try {Thread.sleep(17);} catch (Exception ex) {}	//10 millisecond delay between each refresh

				}

				new Physics();
				
				if(Physics.soundMap.get("main theme") != null) Physics.soundMap.get("main theme").stop();
				Physics.soundMap.clear();
				Physics.playSound("gong");

				frame.dispose();

			}
		});	
		drawSquares.start();	//Start the main loop

	}

	public void keyTyped(KeyEvent e) {}

	//Key events for going through the list of menus
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(!onSettings) {
				Physics.playSound("menuRight");

				if(currentSelection < 5) {
					currentSelection++;
				}
				else currentSelection = 0;
				if(currentSelection < 5) {
					nextSelection=currentSelection+1;
				}else nextSelection=0;
				if(currentSelection > 0)prevSelection = currentSelection-1;
				else prevSelection = 5;
				if(nextSelection+1<=5) {
					nextNextSelection = nextSelection+1;
				} else nextNextSelection =0;
				if(prevSelection-1>=0) {
					prevPrevSelection = prevSelection-1;
				} else prevPrevSelection =5;

			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_UP) {
			onSettings = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			onSettings = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(!onSettings) {
				Physics.playSound("menuLeft");
				if(currentSelection > 0) 
					currentSelection--;
				else currentSelection = 5;
				if(currentSelection < 5) {
					nextSelection=currentSelection+1;
				}else nextSelection=0;
				if(currentSelection > 0)prevSelection = currentSelection-1;
				else prevSelection = 5;
				if(nextSelection+1<=5) {
					nextNextSelection = nextSelection+1;
				} else nextNextSelection =0;
				if(prevSelection-1>=0) {
					prevPrevSelection = prevSelection-1;
				} else prevPrevSelection =5;
			}

		}
		else if(e.getKeyCode()== KeyEvent.VK_ENTER) {
			if(!onSettings) {

				Physics.currentMap=allMapArray.get(currentSelection);

				ended=true;

			}
			else {
				new MapSettings();
				frame.dispose();
			}
		}
	}

	public void keyReleased(KeyEvent e) {}

	public void clearAll() {	//Clear all components from panel
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
	}

	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			FontMetrics metrics = g.getFontMetrics(font);
			g.setColor(new Color(34,33,34));
			g.fillRect(0, 0, width, height);
			g.setFont(font);
			g.setColor(new Color(255,255,255));
			g.drawString("ChoOse Your Map", 250, rectY-40);
			BufferedImage img = new BufferedImage((int)(rectWidth/1.5), (int)(rectHeight/1.5),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule = AlphaComposite.SRC_OVER;
			Composite comp = AlphaComposite.getInstance(rule , (float) 0.6 );
			g2.setComposite(comp);

			g2.drawImage(allMapArray.get(nextSelection).getBack(), 0, 0,(int)(rectWidth/1.5), (int)(rectHeight/1.5),null);
			g.drawImage(img, rectX+250, rectY+35, null);

			BufferedImage img1 = new BufferedImage((int)(rectWidth/1.5), (int)(rectHeight/1.5),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g3 = img1.createGraphics();
			g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule2 = AlphaComposite.SRC_OVER;
			Composite comp2 = AlphaComposite.getInstance(rule2 , (float) 0.6 );
			g3.setComposite(comp2);

			g3.drawImage(allMapArray.get(prevSelection).getBack(), 0, 0,(int)(rectWidth/1.5), (int)(rectHeight/1.5),null);
			g.drawImage(img1, rectX-150, rectY+35, null);


			BufferedImage img2 = new BufferedImage((int)(rectWidth/1.8)-65, (int)(rectHeight/1.8),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g4 = img2.createGraphics();
			g4.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule3 = AlphaComposite.SRC_OVER;
			Composite comp3 = AlphaComposite.getInstance(rule3 , (float) 0.3 );
			g4.setComposite(comp3);

			g4.drawImage(allMapArray.get(prevPrevSelection).getBack(), 0, 0,(int)(rectWidth/1.8), (int)(rectHeight/1.8),null);
			g.drawImage(img2, rectX-250, rectY+45, null);


			BufferedImage img3 = new BufferedImage((int)(rectWidth/1.8)-65, (int)(rectHeight/1.8),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g5 = img3.createGraphics();
			g5.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule4 = AlphaComposite.SRC_OVER;
			Composite comp4 = AlphaComposite.getInstance(rule4 , (float) 0.3 );
			g5.setComposite(comp4);

			g5.drawImage(allMapArray.get(nextNextSelection).getBack(), 0-65, 0,(int)(rectWidth/1.8), (int)(rectHeight/1.8),null);
			g.drawImage(img3, rectX+450, rectY+45, null);
			g.setColor(new Color(0,0,0));

			g.drawRect(rectX-150, rectY+35, (int)(rectWidth/1.5), (int)(rectHeight/1.5));
			g.drawRect(rectX-150+1, rectY+35+1, (int)(rectWidth/1.5)-2, (int)(rectHeight/1.5)-2);
			g.drawRect(rectX-150-1, rectY+35-1, (int)(rectWidth/1.5)+2, (int)(rectHeight/1.5)+2);
			g.drawRect(rectX+250, rectY+35, (int)(rectWidth/1.5), (int)(rectHeight/1.5));
			g.drawRect(rectX+250+1, rectY+35+1, (int)(rectWidth/1.5)-2, (int)(rectHeight/1.5)-2);
			g.drawRect(rectX+250-1, rectY+35-1, (int)(rectWidth/1.5)+2, (int)(rectHeight/1.5)+2);

			allMapArray.get(currentSelection).draw(g, rectX,rectY,rectWidth,rectHeight);
			if(!onSettings) {
				g.setColor(new Color(255,0,0));
			}
			g.drawRect(rectX, rectY, rectWidth, rectHeight);
			g.drawRect(rectX+1, rectY+1, rectWidth-2, rectHeight-2);
			g.drawRect(rectX-1, rectY-1, rectWidth+2, rectHeight+2);

			g.setColor(new Color(255,255,255));
			g.drawString(allMapArray.get(currentSelection).getTitle(), (int)((width - metrics.stringWidth(allMapArray.get(currentSelection).getTitle())) / 2), rectY+rectHeight+70);
			BufferedImage difficultyStars = new BufferedImage(50*allMapArray.get(currentSelection).getDifficulty(), 50,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g6 = difficultyStars.createGraphics();
			g6.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule5 = AlphaComposite.SRC_OVER;
			int x =0, y=0;
			Composite comp5 = AlphaComposite.getInstance(rule5 , (float) 1 );
			g6.setComposite(comp5);
			for(int i = 0; i < allMapArray.get(currentSelection).getDifficulty(); i++) {
				g6.drawImage(starImage, x, y,null);
				x+=50;
			}
			g.drawImage(difficultyStars, (int)((900-difficultyStars.getWidth())/2), rectY+rectHeight+100, null);
			g.drawImage(settingsIcon, 850, 0,null);
			if(onSettings) {
				g.drawImage(redCircle, 850-3, 0-3, null);

			}
		}
	}
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChooseMapMenu implements KeyListener {	//KeyListener is like ActionListener but for keyboard
	private int rectX=300,rectY=100,rectWidth=300,rectHeight=200;
	private int currentSelection = 0;
	private int nextSelection = 1;
	private int prevSelection = 5;
	private int nextNextSelection=2;
	private int prevPrevSelection=4;
	private final int height = 600;	//Window dimensions
	private final int width = 900;
	private Font font = null;
//CENTER OF SCREEN IS 450
	private JFrame frame;	
	private JPanel panel = new canvas();	

	public ChooseMapMenu() {
		frame = new JFrame("Choose Your Map");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.addKeyListener(this);
		
		String fName = "superFont.ttf";
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

				while (true) {	
					frame.repaint();	//Refresh frame and panel
					panel.repaint();
					try {Thread.sleep(17);} catch (Exception ex) {}	//10 millisecond delay between each refresh
				}

			}
		});	
		drawSquares.start();	//Start the main loop

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
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
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
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
		else if(e.getKeyCode()== KeyEvent.VK_ENTER) {

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
		new ChooseMapMenu();
	}



	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			g.fillRect(0, 0, width, height);

			BufferedImage img = new BufferedImage((int)(rectWidth/1.5), (int)(rectHeight/1.5),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule = AlphaComposite.SRC_OVER;
			Composite comp = AlphaComposite.getInstance(rule , (float) 0.6 );
			g2.setComposite(comp);
			//				 physics2.allMapArray[nextSelection].draw(g2, rectX+250,rectY+35,(int)(rectWidth/1.5),(int)(rectHeight/1.5));

			g2.drawImage(physics2.allMapArray[nextSelection].getBack(), 0, 0,(int)(rectWidth/1.5), (int)(rectHeight/1.5),null);
			g.drawImage(img, rectX+250, rectY+35, null);

			BufferedImage img1 = new BufferedImage((int)(rectWidth/1.5), (int)(rectHeight/1.5),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g3 = img1.createGraphics();
			g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule2 = AlphaComposite.SRC_OVER;
			Composite comp2 = AlphaComposite.getInstance(rule2 , (float) 0.6 );
			g3.setComposite(comp2);

			g3.drawImage(physics2.allMapArray[prevSelection].getBack(), 0, 0,(int)(rectWidth/1.5), (int)(rectHeight/1.5),null);
			g.drawImage(img1, rectX-150, rectY+35, null);

			
			BufferedImage img2 = new BufferedImage((int)(rectWidth/1.8)-65, (int)(rectHeight/1.8),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g4 = img2.createGraphics();
			g4.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule3 = AlphaComposite.SRC_OVER;
			Composite comp3 = AlphaComposite.getInstance(rule3 , (float) 0.2 );
			g4.setComposite(comp3);

			g4.drawImage(physics2.allMapArray[prevPrevSelection].getBack(), 0, 0,(int)(rectWidth/1.8), (int)(rectHeight/1.8),null);
			g.drawImage(img2, rectX-250, rectY+45, null);


			
			BufferedImage img3 = new BufferedImage((int)(rectWidth/1.8)-65, (int)(rectHeight/1.8),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g5 = img3.createGraphics();
			g5.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int rule4 = AlphaComposite.SRC_OVER;
			Composite comp4 = AlphaComposite.getInstance(rule4 , (float) 0.2 );
			g5.setComposite(comp4);

			g5.drawImage(physics2.allMapArray[nextNextSelection].getBack(), 0-65, 0,(int)(rectWidth/1.8), (int)(rectHeight/1.8),null);
			g.drawImage(img3, rectX+450, rectY+45, null);


//					    physics2.allMapArray[nextSelection].draw(g, rectX+250,rectY+35,(int)(rectWidth/1.5),(int)(rectHeight/1.5));
//						physics2.allMapArray[prevSelection].draw(g, rectX-150,rectY+35,(int)(rectWidth/1.5),(int)(rectHeight/1.5));
			g.setColor(new Color(0,0,0));
			g.drawRect(rectX-150, rectY+35, (int)(rectWidth/1.5), (int)(rectHeight/1.5));
			g.drawRect(rectX-150+1, rectY+35+1, (int)(rectWidth/1.5)-2, (int)(rectHeight/1.5)-2);
			g.drawRect(rectX-150-1, rectY+35-1, (int)(rectWidth/1.5)+2, (int)(rectHeight/1.5)+2);
			g.drawRect(rectX+250, rectY+35, (int)(rectWidth/1.5), (int)(rectHeight/1.5));
			g.drawRect(rectX+250+1, rectY+35+1, (int)(rectWidth/1.5)-2, (int)(rectHeight/1.5)-2);
			g.drawRect(rectX+250-1, rectY+35-1, (int)(rectWidth/1.5)+2, (int)(rectHeight/1.5)+2);
			
			physics2.allMapArray[currentSelection].draw(g, rectX,rectY,rectWidth,rectHeight);
			g.drawRect(rectX, rectY, rectWidth, rectHeight);
			g.drawRect(rectX+1, rectY+1, rectWidth-2, rectHeight-2);
			g.drawRect(rectX-1, rectY-1, rectWidth+2, rectHeight+2);

			g.setFont(font);
			g.setColor(new Color(255,255,255));
			FontMetrics metrics = g.getFontMetrics(font);
		    g.drawString(physics2.allMapArray[currentSelection].getTitle(), (int)((width - metrics.stringWidth(physics2.allMapArray[currentSelection].getTitle())) / 2), 350);

		}
	}
}
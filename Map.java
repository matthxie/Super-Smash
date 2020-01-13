import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Map{ //extends JPanel
	private Image backgroundImage;
	private Platform[] platformArray;
	private int difficulty;
	private String title;
	private final Image starImage = Toolkit.getDefaultToolkit().createImage("superSmashDifficultyStars.png").getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
	private BufferedImage difficultyStars;
	public String getTitle() {
		return title;
	}
	public Platform[] getPlatformArray() {
		return platformArray;
	}
	public BufferedImage getStarImage() {
		return difficultyStars;
	}
	public Image getBack() {
		return backgroundImage;
	}
	public int getDifficulty() {
		return difficulty;
	}
	public Map(Image img, Platform[] platArr, int diff, String title) {
		backgroundImage = img;
		platformArray = platArr;
		difficulty = diff;
		this.title = title;
		int x = 0, y = 0;
		difficultyStars = new BufferedImage(50*difficulty, 50,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = difficultyStars.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int rule = AlphaComposite.SRC_OVER;
		Composite comp = AlphaComposite.getInstance(rule , (float) 1 );
		g2.setComposite(comp);
		//				 physics2.allMapArray[nextSelection].draw(g2, rectX+250,rectY+35,(int)(rectWidth/1.5),(int)(rectHeight/1.5));
		for(int i = 0; i < difficulty; i++) {
			g2.drawImage(starImage, x, y,null);
			System.out.println(i);
			x+=50;
		}
		
	}
	public void drawDiff(Graphics g, int x, int y) {
		Graphics2D gg = (Graphics2D) g;
		gg.drawImage(difficultyStars, x, y, null);
	}
	public void draw(Graphics g, int x, int y, int width, int height) {
		Graphics2D gg = (Graphics2D) g;		
		g.drawImage(backgroundImage, x, y, width, height, null);
	}
	
	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;		
		gg.drawImage(backgroundImage, 0, 0, null);
	}
	
}
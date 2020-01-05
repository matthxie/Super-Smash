import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

public class Map extends JPanel{
	private Image backgroundImage;
	private Platform[] platformArray;
	private int difficulty;
	private String title;
	
	public String getTitle() {
		return title;
	}
	
	public Image getBack() {
		return backgroundImage;
	}
	
	public Map(Image img, Platform[] platArr, int diff, String title) {
		backgroundImage = img;
		platformArray = platArr;
		difficulty = diff;
		this.title = title;
	}
	public void draw(Graphics g, int x, int y, int width, int height) {
		Graphics2D gg = (Graphics2D) g;		
		gg.drawImage(backgroundImage, x, y, width, height, null);
	}
	
	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;		
		gg.drawImage(backgroundImage, 0, 0, null);
	}
	
}

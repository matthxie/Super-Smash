import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class Map{ //extends JPanel
	private Image backgroundImage;
	private Platform[] platformArray;
	private int difficulty;
	private String title;
	private int spawnX, spawnXLimit;
	
	public String getTitle() {
		return title;
	}
	public int getSpawnX() {
		return spawnX;
	}
	public int getSpawnXLimit() {
		return spawnXLimit;
	}
	public Platform[] getPlatformArray() {
		return platformArray;
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
		this.spawnX = platArr[0].getPlatX();
		this.spawnXLimit = platArr[0].getPlatWidth()+this.spawnX;
		
	}
	public void drawDiff(Graphics g, int x, int y) {
		Graphics2D gg = (Graphics2D) g;
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
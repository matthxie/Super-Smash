import javax.swing.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.*;

public class Platform extends JPanel {
	int topCorner, leftCorner;
	int longWise, fatWise;
	
	
	public Platform(int leftCorner, int topCorner, int longWise, int fatWise) {
		this.topCorner = topCorner;
		this.leftCorner = leftCorner;
		this.longWise = longWise;
		this.fatWise = fatWise;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(this.leftCorner, this.topCorner, this.longWise, 1);
	}
	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;		
		gg.fillRect(leftCorner, topCorner, longWise, fatWise);
	}
	
}

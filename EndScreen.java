import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EndScreen {	//KeyListener is like ActionListener but for keyboard
	private Font font = null,fontSmaller=null, fontMedium=null;
	
	private final int height = 600;	//Window dimensions
	private final int width = 900;

	private JFrame frame;	
	private JPanel panel = new canvas();	
	
	private String playerOne, playerTwo;
	private int numDeathsOne, numDeathsTwo;
	private double totalDamageOne, totalDamageTwo;

	public EndScreen() {
		String fName = "superFont.ttf";
		File fontFile = new File(fName);		

		playerOne = Physics.physicsObjectList.get(0).getName();
		playerTwo = Physics.physicsObjectList.get(1).getName();
		
		numDeathsOne = Physics.physicsObjectList.get(0).getNumDeaths();
		numDeathsTwo = Physics.physicsObjectList.get(1).getNumDeaths();
		
		totalDamageOne = Physics.physicsObjectList.get(0).getTotalDamage();
		totalDamageTwo = Physics.physicsObjectList.get(1).getTotalDamage();
		
		try {
			Font tempfont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			font = tempfont.deriveFont((float)(40));
			fontSmaller = tempfont.deriveFont((float)(20));
			fontMedium = tempfont.deriveFont((float)(25));

		} catch (FontFormatException e) {} catch (IOException e) {}
		
		frame = new JFrame("End Screen");	//Frame stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);

		panel.setLayout(new BorderLayout());	

		frame.add(panel);

		frame.setLocationRelativeTo(null);	//Make the frame visible
		frame.setVisible(true);	
	}

	public static void main(String[] args) {	//Call the graphics constructor
		new EndScreen();
	}

	public class canvas extends JPanel {	//Make a new JPanel that you can draw objects onto (Can't draw stuff anywhere you want onto normal JPanels)
		public void paintComponent(Graphics g) {
			super.paintComponent(g);	//Call paintComponent from the overlord JPanel
			
			g.drawImage(Physics.imageMap.get(playerOne), width*(2/3), 70, null);
			g.drawImage(Physics.imageMap.get(playerTwo), width*(1/3), 70, null);
			
			g.drawString("Number of deaths: " + numDeathsOne, width-600, 90);
			g.drawString("Number of deaths: " + numDeathsTwo, width-300, 90);
			
			g.drawString("Total damage received: " + totalDamageOne, width-600, 120);
			g.drawString("Total damage received: " + totalDamageTwo, width-300, 120);
			
			g.drawRect((width/2)-10, 0, 10, height);
		}
	}
}
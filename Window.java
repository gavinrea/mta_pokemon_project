import javax.swing.*;

public class Window {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
		JPanel contents = new JPanel();
		JButton saveButton = new JButton("Save");
		JTextArea textDisplay = new JTextArea("So my next point is...this is a really long sentence that will not wrap");
		textDisplay.setLineWrap(true);
		Tile t= new ForestTile();
		contents.add(saveButton);
		contents.add(textDisplay);
		
		frame.setContentPane(contents);
		
		frame.setTitle("Window");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0,0);
		frame.setSize(500,500);
		frame.setVisible(true);
	}

}

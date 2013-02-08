import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class BasicWindow extends JFrame
{
	public static final int TILE_SIZE = 50;
	public static final int VIEW_SIZE = 11;
	public static final int SCREEN_SIZE = TILE_SIZE * VIEW_SIZE;
	public static final int NUM_WORLD_ROWS = 1000;
	public static final int NUM_WORLD_COLS = 1000;

	private Tile[][] world = new Tile[NUM_WORLD_ROWS][NUM_WORLD_COLS];

	private int charRow = 5;
	private int charCol = 5;

	// data structure 
	class MyCanvas extends JComponent
	{
		public void paint(Graphics g)
		{
			super.paint(g);
			int x = 0;
			for(int row = charRow - 5; row <= charRow + 5; row++){
				int y = 0;
				for(int col = charCol - 5; col <= charCol + 5; col++){
					if(world[row][col] != null){
						world[row][col].draw(g, x , y);
					}
					y += 50;
				}
				x += 50;
			}
		}

	}
	// constructor
	public BasicWindow() throws FileNotFoundException
	{
		super("My fancy title");
		MyCanvas mc = new MyCanvas();
		mc.setPreferredSize(new Dimension(TILE_SIZE * VIEW_SIZE, TILE_SIZE * VIEW_SIZE));
		add(mc);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// JButton jb = new JButton("Click Me!");
		// add(jb);
		readWorld("world"); 
		repaint();
		setVisible(true);
	}
	public void readWorld(String fileName) throws FileNotFoundException{
		File f = new File(fileName);
		if(f.isFile()){
			Scanner s = new Scanner(f);
			int numLines = s.nextInt();
			for(int i = 0; i < numLines; i++){
				int row = s.nextInt();
				int col = s.nextInt();
				int width = s.nextInt();
				int height = s.nextInt();
				Tile t= new BlankTile();
				String terrainType = s.next();
				if(terrainType.equals("forest")){
					t = new ForestTile();
					//world[row][col] = new ForestTile();
					//System.out.println("creating ForestTile at " + row + ", " + col);
				}
				else if(terrainType.equals("water")){
					t = new WaterTile();
					//world[row][col] = new WaterTile();
					//System.out.println("creating WaterTile at " + row + ", " + col);
				}
				else if(terrainType.equals("dirt")){
					t = new DirtTile();
					//world[row][col] = new DirtTile();
					//System.out.println("creating DirtTile at " + row + ", " + col);
				}
				for(int x = row; x < row + width; x++){
					for(int y = col; y < col + height; y++){
						world[x][y] = t;
						System.out.println("creating "+terrainType+" at" + row + ","+ col);

					}
				}
			}
			s.close();
		}
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		BasicWindow myWindow = new BasicWindow();
	}
}

import java.awt.*;
import java.awt.event.*;
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
						if(col == charCol && row == charRow){
							Color skin = new Color(240, 210, 175);
							g.setColor(skin);
							g.fillRoundRect(x + 20, y + 20, 10, 20, 5, 5);
							g.fillOval(x + 10, y + 10, 30, 20);
						}
					}
					y += 50;
				}
				x += 50;
			}
		}
	}
	public class ListenForKey implements KeyListener{
		public void keyPressed(KeyEvent k){
			
		}
		public void keyReleased(KeyEvent k){
			
		}
		public void keyTyped(KeyEvent k){
			char c = k.getKeyChar();
			System.out.println("The key pressed was " + c);
			if(c == 'a'){
				charRow --;
			}
			else if(c == 'd'){
				charRow++;
			}
			else if(c == 'w'){
				charCol--;
			}
			else if(c == 's'){
				charCol++;
			}
			repaint();
		}
	}
	// constructor
	public BasicWindow() throws FileNotFoundException
	{
		super("My fancy title");
		MyCanvas mc = new MyCanvas();
		addKeyListener(new ListenForKey());
		mc.setPreferredSize(new Dimension(TILE_SIZE * VIEW_SIZE, TILE_SIZE * VIEW_SIZE));
		add(mc);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// JButton jb = new JButton("Click Me!");
		// add(jb);
		for(int i = 0; i < NUM_WORLD_ROWS;i++){
			for(int j = 0; j < NUM_WORLD_ROWS;j++){
				world[i][j] = new BlankTile();
			}
		}
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
				int height = s.nextInt();
				int width = s.nextInt();
				Tile t = new BlankTile();
				String terrainType = s.next();
				if(terrainType.equals("forest")){
					t = new ForestTile();
				}
				else if(terrainType.equals("water")){
					t = new WaterTile();
				}
				else if(terrainType.equals("dirt")){
					t = new DirtTile();
				}
				for(int x = row; x < row + width; x++){
					for(int y = col; y < col + height; y++){
						world[x][y] = t;
						System.out.println("creating "+terrainType+" at " + row + ", "+ col);

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

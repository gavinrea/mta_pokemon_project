import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class BasicWindow extends JFrame 
{
	//all initial values for frame and world size
	public static final int TILE_SIZE = 50;
	public static final int BORDER_WIDTH = 5;
	public static final int VIEW_SIZE = 11;
	public static final int SCREEN_SIZE = TILE_SIZE * VIEW_SIZE;
	public static final int EFFECTIVE_WORLD_ROWS = 1000;
	public static final int EFFECTIVE_WORLD_COLS = 1000;
	public static final int NUM_WORLD_ROWS = EFFECTIVE_WORLD_ROWS + BORDER_WIDTH * 2;
	public static final int NUM_WORLD_COLS = EFFECTIVE_WORLD_COLS + BORDER_WIDTH * 2;;

	//create world to specifications
	private Tile[][] world = new Tile[NUM_WORLD_ROWS][NUM_WORLD_COLS];
	//starting location of character
	private int charRow = 11;
	private int charCol = 5;

	// data structure 
	public class MyCanvas extends JComponent 
	{

		public void paint(Graphics g)
		{
			super.paint(g);
			int y = 0;
			for(int row = charRow - 5; row <= charRow + 5; row++){
				int x = 0;
				for(int col = charCol - 5; col <= charCol + 5; col++){
					//if there's a tile there, paint it
					if(world[row][col] != null){
						world[row][col].draw(g, x , y);
						//draw character over its location
						if(col == charCol && row == charRow){
							//character specifications
							Color skin = new Color(240, 210, 175);
							g.setColor(skin);
							g.fillRoundRect(x + 15, y + 20, 20, 20, 5, 5);
							g.fillOval(x + 10, y + 10, 30, 20);
						}//chara specifications
					}//if(world[][])
					x += 50;
				}//for(int col)
				y += 50;
			}// for int charRow
		}//paint(Graphics g)
	}//MyCanvas()
	//we need a class keylistener, so we can make it an object and pass it to Jcomponent
	public class ListenForKey implements KeyListener 
	{
		private int code;
		public void keyPressed(KeyEvent k)
		{
			code = k.getKeyCode();
			//print out the key

			//wasd controls
			int changeRow=0;
			int changeCol=0;

			if(code == k.VK_LEFT)
			{
				changeCol --;
			}
			else if(code == k.VK_RIGHT)
			{
				changeCol++;
			}
			else if(code == k.VK_UP)
			{
				changeRow--;
			}
			else if(code == k.VK_DOWN)
			{
				changeRow++;
			}
			if(world[charRow + changeRow][charCol + changeCol].isSolid() == false) 
			{
				charRow= charRow+changeRow;
				charCol= charCol+changeCol;
				System.out.println("Row " + charRow + ", Col " + charCol+ ", " + world[charRow][charCol]);
				System.out.println(world[charRow][charCol].isSolid());
				repaint();
			}
			//if it's a water tile: buggy code
			/*else if(world[charRow + changeRow][charCol + changeCol] == Tile.WaterTile){
				if(pokemon.has(waterType)){
					setSolid(world[charRow + changeRow][charCol + changeCol]);
				}
				else{
				System.out.println("None of your pokemon know surf");
			}*/
		}

		public void keyReleased(KeyEvent k){
		}
		//controller functions
		public void keyTyped(KeyEvent k)
		{
			/*
			 * char c = k.getKeyChar();
			//print out the key
			System.out.println("The key pressed was " + c);
			//asdw controls
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
			 */
		}
	}
	// constructor for window
	public BasicWindow() throws FileNotFoundException
	{
		super("My fancy title");
		MyCanvas mc = new MyCanvas();
		addKeyListener(new ListenForKey());
		mc.setPreferredSize(new Dimension(TILE_SIZE * VIEW_SIZE, TILE_SIZE * VIEW_SIZE));
		add(mc);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//fill inner square with blank tiles so we don't get any null pointer exceptions
		for(int i = BORDER_WIDTH; i < NUM_WORLD_ROWS - BORDER_WIDTH;i++)
		{
			for(int j = BORDER_WIDTH; j < NUM_WORLD_ROWS - BORDER_WIDTH;j++)
			{
				world[i][j] = new WhiteTile();
			}
		}
		//make a black border around the outside of the image to prevent players from walking off the world
		//left vertical
		for(int i = 0; i < BORDER_WIDTH;i++)
		{
			for(int j = 0; j < NUM_WORLD_ROWS;j++)
			{
				world[i][j] = new BlackTile();
			}
		}
		//right vertical
		for(int i = NUM_WORLD_ROWS - BORDER_WIDTH; i < BORDER_WIDTH;i++)
		{
			for(int j = 0; j < NUM_WORLD_ROWS;j++)
			{
				world[i][j] = new BlackTile();
			}
		}
		//top horizontal
		for(int i = BORDER_WIDTH; i < NUM_WORLD_ROWS - BORDER_WIDTH;i++)
		{
			for(int j = 0; j < BORDER_WIDTH;j++)
			{
				world[i][j] = new BlackTile();
			}
		}
		//bottom horizontal
		for(int i = BORDER_WIDTH; i < NUM_WORLD_ROWS - BORDER_WIDTH;i++)
		{
			for(int j = NUM_WORLD_ROWS - BORDER_WIDTH; j < NUM_WORLD_ROWS;j++)
			{
				world[i][j] = new BlackTile();
			}
		}
		//read in the world
		readWorld("world"); 
		//paint the world
		repaint();
		setVisible(true);
	}
	//Readworld Function
	public void readWorld(String fileName) throws FileNotFoundException
	{
		File f = new File(fileName);
		if(f.isFile())
		{
			Scanner s = new Scanner(f);
			//the first number in the file is the number of lines to read
			int numLines = s.nextInt();
			for(int i = 0; i < numLines; i++)
			{
				//upper left hand corner
				int row = s.nextInt();
				int col = s.nextInt();
				//how many tiles wide/high
				int height = s.nextInt();
				int width = s.nextInt();
				//create a tile which will run a gauntlet of if statements to be assigned
				Tile t = new WhiteTile();
				String terrainType = s.next();
				//is there a more elegant way? what if we name the tiles the same as on world? Do we need all these ifs?
				if(terrainType.equals("forest"))
				{
					t = new ForestTile();
				}
				else if(terrainType.equals("water"))
				{
					t = new WaterTile();
				}
				else if(terrainType.equals("dirt"))
				{
					t = new DirtTile();
				}
				else if(terrainType.equals("pic"))
				{
					t = new PicTile();
				}
				//set the world array point equal to the tile
				for(int c = col; c < col + width; c++)
				{
					for(int r = row; r < row + height; r++)
					{
						world[r + BORDER_WIDTH][c + BORDER_WIDTH] = t;
						//log to console
						System.out.println("creating " + terrainType + " at " + row + ", "+ col);

					}
				}
			}
			s.close();
		}
	}
	//main method
	public static void main(String[] args) throws FileNotFoundException
	{
		BasicWindow myWindow = new BasicWindow();
	}
}

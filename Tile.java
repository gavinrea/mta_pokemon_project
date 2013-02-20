import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
//superclass tile. Abstract so you have to make a type of one
public abstract class Tile {
	protected boolean solid = false;
	public boolean isSolid(){
		return solid;
	}
	public void setSolid(boolean solidity){
		solid = solidity;
	}
	public abstract void draw(Graphics g, int x, int y);
}
//the default tile, which we will set the world to initially before assigning more specific values
class WhiteTile extends Tile{
	public WhiteTile(){}
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.white);
		g.fillRect(x, y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}
class BlackTile extends Tile{
	public BlackTile(){
		solid = true;
	}
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.black);
		g.fillRect(x, y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}
//test on drawpicture
class RedTile extends Tile{
	private BufferedImage buf;
	public RedTile(){
		try{
			File f = new File("/Users/gavinrea/Desktop/birds.jpeg");
			System.out.println("File status = " + f.isFile());
			buf = ImageIO.read(f);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void draw(Graphics g, int x, int y){
		g.drawImage(buf, x, y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE, null);
	}
}
//black background + tree
class TreeTile extends Tile{
	public TreeTile(){
		solid = true;
	}
	public void draw(Graphics g, int x, int y){
		Color darkGreen = new Color(20, 90, 10);
		g.setColor(darkGreen);
		g.fillRect(x, y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
		g.setColor(Color.GREEN);
		int[] xVal = {x, x + 25, x + 49};
		int[] yVal = {y + 49, y, y + 49};
		g.fillPolygon(xVal, yVal, 3);
	}
}
class ForestTile extends Tile{
	public ForestTile(){
		solid = true;
	}
	public void draw(Graphics g, int x, int y){
		Color darkGreen = new Color(20, 90, 10);
		g.setColor(darkGreen);
		g.fillRect(x, y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
		g.setColor(new Color(90, 160, 35));
		int[] aVal = {x, x + 25, x + 49};
		int[] bVal = {y, y + 49, y};
		g.fillPolygon(aVal, bVal, 3);
		g.setColor(Color.GREEN);
		int[] xVal = {x, x + 25, x + 49};
		int[] yVal = {y + 49, y, y + 49};
		g.fillPolygon(xVal, yVal, 3);


	}
}
//blue tile
class WaterTile extends Tile{
	public WaterTile(){
		solid = true;
	}
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.BLUE);
		g.fillRect(x,y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}
//brown tile
class DirtTile extends Tile{
	public DirtTile(){}
	public void draw(Graphics g, int x, int y){
		Color brown = new Color(180, 110, 20);
		g.setColor(brown);
		g.fillRect(x,y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}

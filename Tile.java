import java.awt.*;
//superclass tile. Abstract so you have to make a type of one
public abstract class Tile {
	public boolean solid;
	public boolean isSolid(){
		return solid;
	}
	public abstract void draw(Graphics g, int x, int y);
}
//the default tile, which we will set the world to initially before assigning more specific values
class BlankTile extends Tile{
	private boolean solid = false;
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.white);
		g.fillRect(x, y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}
//black background + tree
class treeTile extends Tile{
	private boolean solid = true;
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
	public boolean solid = true;
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
	public boolean solid = true;
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.BLUE);
		g.fillRect(x,y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}
//brown tile
class DirtTile extends Tile{
	public boolean solid = false;
	public void draw(Graphics g, int x, int y){
		Color brown = new Color(180, 110, 20);
		g.setColor(brown);
		g.fillRect(x,y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}

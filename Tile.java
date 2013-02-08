import java.awt.*;


public abstract class Tile {
	public abstract void draw(Graphics g, int x, int y);

}
class BlankTile extends Tile{
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.white);
		g.fillRect(x, y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}
class ForestTile extends Tile{
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.BLACK);
		g.fillRect(x, y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
		g.setColor(Color.GREEN);
		int[] xVal = {x, x + 25, x + 49};
		int[] yVal = {y + 49, y, y + 49};
		g.fillPolygon(xVal, yVal, 3);


	}
}

class WaterTile extends Tile{
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.BLUE);
		g.fillRect(x,y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}

class DirtTile extends Tile{
	public void draw(Graphics g, int x, int y){
		Color brown = new Color(180, 110, 20);
		g.setColor(brown);
		g.fillRect(x,y, BasicWindow.TILE_SIZE, BasicWindow.TILE_SIZE);
	}
}

package worlds;

import java.awt.Graphics;

import org.joml.Vector2i;

import engine.Engine;
import objects.GameObject;
import pathfinding.ExampleFactory;
import pathfinding.ExampleNode;
import pathfinding.Map;
import physics.Handler;
import physics.Physics;
import registry.GameRegistry;
import tiles.Tile;

public class World
{
	private Map<ExampleNode> map;
	private Tile[][] tiles;
	private int width, height;

	public World(int width, int height)
	{
		Handler.setWorld(this);
		map = new Map<>(width, height, new ExampleFactory());
		tiles = new Tile[width][height];
		this.width = width;
		this.height = height;
		buildWorld();
	}

	private void buildWorld()
	{
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				tiles[i][j] = GameRegistry.getTilesRegister().get("default_tile");
				setSolid(tiles[i][j].isSolid(), i, j);
			}
		}
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 0, 0);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 1, 0);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 2, 0);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 3, 0);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 4, 0);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 5, 0);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 0);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 0, 1);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 0, 2);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 0, 3);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 0, 4);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 0, 5);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 0, 6);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 2, 2);

		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 1);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 4);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 5);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 6);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 8);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 9);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 10);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 11);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 12);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 13);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 14);

		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 0, 7);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 1, 7);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 2, 7);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 3, 7);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 4, 7);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 6, 7);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 7, 7);
		setTile(GameRegistry.getTilesRegister().get("rock_tile"), 8, 7);
	}

	public void update(double delta)
	{

	}

	public void render(Graphics graphics)
	{
		int startX = (int)(Math.max(0, Handler.getCamera().getOffset().x / Tile.TILEWIDTH));
		int startY = (int)(Math.max(0, Handler.getCamera().getOffset().y / Tile.TILEHEIGHT));
		int endX = (int)(Math.min(width, ((Handler.getCamera().getOffset().x + Engine.getWidth()) / Tile.TILEWIDTH) + 1));
		int endY = (int)(Math.min(height, ((Handler.getCamera().getOffset().y + Engine.getHeight()) / Tile.TILEHEIGHT) + 1));

		for(int y = startY; y < endY; y++)
		{
			for(int x = startX; x < endX; x++)
			{
				getTile(x, y).render(graphics, (int)((x * Tile.TILEWIDTH) - Handler.getCamera().getOffset().x), (int)((y * Tile.TILEWIDTH) - Handler.getCamera().getOffset().y));
			}
		}
	}

	public Tile getTile(int x, int y)
	{
		if((x < 0) || (y < 0) || (x >= width) || (y >= height))
		{
			return GameRegistry.getTilesRegister().get("default_tile");
		}
		if(tiles[x][y] == null)
		{
			return GameRegistry.getTilesRegister().get("default_tile");
		}
		return tiles[x][y];
	}

	public void setTile(Tile tile, int x, int y)
	{
		if((x < 0) || (y < 0) || (x >= width) || (y >= height))
		{
			return;
		}
		if(tile == null)
		{
			tiles[x][y] = GameRegistry.getTilesRegister().get("default_tile");
			setSolid(tiles[x][y].isSolid(), x, y);
			return;
		}
		tiles[x][y] = tile;
		setSolid(tile.isSolid(), x, y);
	}

	public void setSolid(boolean solid, int x, int y)
	{
		map.getNode(x, y).setWalkable(!solid);
	}

	public boolean isSolid(int x, int y)
	{
		return map.getNode(x, y).isWalkable();
	}

	public GameObject getPickedObject()
	{
		Vector2i pos = Handler.getCamera().getPickRay();
		for(GameObject o : Handler.getObjectManager().getAllObjects())
		{
			if((o.getX() >= (Handler.getCamera().getOffset().x + Engine.getWidth())) || (o.getX() < Handler.getCamera().getOffset().x) || (o.getY() >= (Handler.getCamera().getOffset().y + Engine.getHeight())) || (o.getY() < Handler.getCamera().getOffset().y))
			{
				continue;
			}
			if(Physics.collides(pos, o))
			{
				return o;
			}
		}
		return null;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public Tile[][] getTiles()
	{
		return tiles;
	}

	public Map<ExampleNode> getMap()
	{
		return map;
	}
}

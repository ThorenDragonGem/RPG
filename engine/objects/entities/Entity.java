package objects.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import gfx.Skin2D;
import objects.GameObject;
import pathfinding.ExampleNode;
import physics.Handler;
import tiles.Tile;
import times.CoolDown;

public class Entity extends GameObject
{
	protected List<ExampleNode> path;
	protected CoolDown moveCD;

	public Entity(String name, Skin2D skin, int width, int height)
	{
		super(name, skin, width, height);
		path = new ArrayList<>();
		moveCD = new CoolDown(7);
		moveCD.go();
	}

	@Override
	public void update(double delta)
	{
		super.update(delta);
		moveCD.update();

	}

	protected int index;

	protected void move()
	{
		if((path == null) || path.isEmpty())
		{
			return;
		}
		if(moveCD.isOver())
		{
			// sets walkable on move and update the solidity of the map.
			// for each Tile, if Tile is solid then not walkable <= no
			// else if there is solid GameObject then not walkable (objects
			// are
			// casted as tiles not walkable)
			// else walkable
			if(solid)
			{
				undoWorldSolidity(x, y);
			}
			x = path.get(index).getxPosition() * Tile.TILEWIDTH;
			y = path.get(index).getyPosition() * Tile.TILEHEIGHT;
			if(solid)
			{
				setSolid(solid);
			}
			moveCD.restart();
			index++;
			if(index > (path.size() - 1))
			{
				index = 0;
				path = new ArrayList<>();
			}
		}
		checkWorldLimits();
	}

	protected void move(int offsetX, int offsetY)
	{
		if(!moveCD.isOver())
		{
			return;
		}

		if(!collidesWithTiles(x / Tile.TILEWIDTH, (y / Tile.TILEHEIGHT) + offsetY))
		{
			path.clear();
			if(!collidesWithObjects(x / Tile.TILEWIDTH, (y / Tile.TILEHEIGHT) + offsetY))
			{
				y += offsetY * Tile.TILEHEIGHT;
			}
		}
		if(!collidesWithTiles((x / Tile.TILEWIDTH) + offsetX, y / Tile.TILEHEIGHT))
		{
			path.clear();
			if(!collidesWithObjects((x / Tile.TILEWIDTH) + offsetX, y / Tile.TILEHEIGHT))
			{
				x += offsetX * Tile.TILEWIDTH;
			}
		}
		checkWorldLimits();
		moveCD.restart();
	}

	/**
	 * @return true if there is no collisions with adjacent tiles
	 */
	protected boolean collidesWithTiles(int offsetX, int offsetY)
	{
		if(Handler.getWorld().getTile(offsetX, offsetY).isSolid())
		{
			return true;
		}
		return false;
	}

	protected boolean collidesWithObjects(int offsetX, int offsetY)
	{
		if(!Handler.getWorld().getMap().getNode(offsetX, offsetY).isWalkable())
		{
			return true;
		}
		return false;
	}

	protected void checkWorldLimits()
	{
		if(x < 0)
		{
			x = 0;
		}
		else if(x >= (Handler.getWorld().getWidth() * Tile.TILEWIDTH))
		{
			x = Handler.getWorld().getWidth() * Tile.TILEWIDTH;
		}
		if(y < 0)
		{
			y = 0;
		}
		else if(y >= (Handler.getWorld().getHeight() * Tile.TILEHEIGHT))
		{
			y = Handler.getWorld().getHeight() * Tile.TILEHEIGHT;
		}
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
	}

	@Override
	public GameObject createNew(int x, int y)
	{
		return new Entity(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid);
	}
}

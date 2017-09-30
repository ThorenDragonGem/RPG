package objects.entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import gfx.Colors;
import gfx.Skin2D;
import objects.GameObject;
import objects.entities.Entity;
import pathfinding.ExampleNode;
import physics.Handler;
import tiles.Tile;
import times.CoolDown;

public class Creature extends Entity
{
	protected List<ExampleNode> path;
	protected CoolDown moveCD;
	protected GameObject target;
	protected int index;
	protected double health, maxHealth;

	public Creature(String name, Skin2D skin, int width, int height)
	{
		super(name, skin, width, height);
		path = new LinkedList<>();
		moveCD = new CoolDown(7);
		moveCD.go();
		// TODO stats
		maxHealth = 100;
		health = maxHealth;
	}

	@Override
	public void update(double delta)
	{
		super.update(delta);
		moveCD.update();

		if(health <= 0)
		{
			death();
		}
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
		graphics.setColor(new Color(Colors.interpolate((float)(health / maxHealth), Colors.red1.getRGB(), Colors.orange1.getRGB(), Colors.green1.getRGB())));
		graphics.fillRect((int)(x - Handler.getCamera().getOffset().x), (int)((y - Handler.getCamera().getOffset().y)), (int)((health / maxHealth) * width * Tile.TILEWIDTH), 3);
	}

	protected void move()
	{
		// TODO update the path when moving => else walk on non walkable tiles
		// on time t whereas walkable on time 0
		// => update path and so move every time to the index = 0 position;
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
			// x = path.get(0).getxPosition() * Tile.TILEWIDTH;
			// y = path.get(0).getyPosition() * Tile.TILEHEIGHT;
			if(solid)
			{
				setSolid(solid);
			}
			moveCD.restart();
			// path = Handler.getWorld().getMap().findPath(x / Tile.TILEWIDTH, y
			// / Tile.TILEHEIGHT, newPos.x, newPos.y);
			index++;
			if(index > (path.size() - 1))
			{
				index = 0;
				path = new LinkedList<>();
			}
		}
		checkWorldLimits();
	}

	protected void death()
	{
		active = false;
		// TODO spawn ItemContainer with Creature's inventory if inventory not
		// null or not empty
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
		if((offsetX < 0) || (offsetX >= Handler.getWorld().getWidth()) || (offsetY < 0) || (offsetY >= Handler.getWorld().getHeight()))
		{
			return true;
		}
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
}

package objects.entities.creatures;

import java.awt.Graphics;

import gfx.Skin2D;
import objects.GameObject;
import objects.entities.Entity;
import physics.Handler;
import physics.Physics;
import tiles.Tile;

public class Aggressive extends Creature
{
	
	public Aggressive(String name, Skin2D skin, int width, int height)
	{
		super(name, skin, width, height);
		moveCD.setTimeAndRestart(12);
		target = null;
		priority = 9;
	}

	@Override
	public void update(double delta)
	{
		super.update(delta);
		if(target == null)
		{
			look();
		}
		else
		{
			if(targetAtAdjacentTiles(1, 1))
			{
				attack();
			}
			else if(targetAtAdjacentTiles(5, 5))
			{
				chase();
			}
			else
			{
				target = null;
			}
			if(!Handler.getObjectManager().getAllObjects().contains(target))
			{
				target = null;
			}
		}
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
	}

	protected void look()
	{
		for(Entity e : Physics.getEntitiesCircle(x / Tile.TILEWIDTH, y / Tile.TILEHEIGHT, 5))
		{
			if((e instanceof Creature) && (e != this))
			// if(e instanceof Player)
			{
				target = e;
			}
		}
	}

	protected void chase()
	{
		path = Handler.getWorld().getMap().findPath(x / Tile.TILEWIDTH, y / Tile.TILEHEIGHT, target.getX() / Tile.TILEWIDTH, target.getY() / Tile.TILEHEIGHT);
		index = 0;
		move();
	}

	protected void attack()
	{
		// System.out.println("Aggressive.attack()");
		((Creature)target).health--;
	}

	protected void defend()
	{
		System.out.println("Aggressive.defend()");
	}

	/**
	 * @return whether the target is in the Tile quad x - tileOffsetX ---------- x + tileOffsetX ; y - tileOffsetY ---------- y + tileOfsetY
	 */
	protected boolean targetAtAdjacentTiles(int tileOffsetX, int tileOffsetY)
	{
		for(int a = (x / Tile.TILEWIDTH) - tileOffsetX; a < ((x / Tile.TILEHEIGHT) + tileOffsetX + 1); a++)
		{
			for(int b = (y / Tile.TILEHEIGHT) - tileOffsetY; b < ((y / Tile.TILEHEIGHT) + tileOffsetY + 1); b++)
			{
				if((a == (target.getX() / Tile.TILEWIDTH)) && (b == (target.getY() / Tile.TILEHEIGHT)))
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public GameObject createNew(int x, int y, boolean virgin)
	{
		if(virgin)
		{
			return new Aggressive(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setPriority(priority);
		}
		else
		{
			return new Aggressive(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid).setBounds(bounds).setPriority(priority);
		}
	}
}

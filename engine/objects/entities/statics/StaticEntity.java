package objects.entities.statics;

import java.awt.Graphics;

import gfx.Skin2D;
import objects.GameObject;
import objects.entities.Entity;
import tiles.Tile;

public class StaticEntity extends Entity
{

	public StaticEntity(String name, Skin2D skin, int width, int height)
	{
		super(name, skin, width, height);
	}

	@Override
	public void update(double delta)
	{
		super.update(delta);
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
	}

	@Override
	public GameObject createNew(int x, int y, boolean virgin)
	{
		if(virgin)
		{
			return new StaticEntity(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid).setPriority(priority);
		}
		else
		{
			return new StaticEntity(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid).setBounds(bounds).setPriority(priority);
		}
	}
}
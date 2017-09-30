package objects.blocks;

import java.awt.Graphics;

import gfx.Skin2D;
import objects.GameObject;
import tiles.Tile;

public class Block extends GameObject
{

	public Block(String name, Skin2D skin, int width, int height)
	{
		super(name, skin, width, height);
		solid = true;
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
			return new Block(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid).setPriority(priority);
		}
		else
		{
			return new Block(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid).setBounds(bounds).setPriority(priority);
		}
	}
}

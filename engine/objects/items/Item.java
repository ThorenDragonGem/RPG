package objects.items;

import java.awt.Graphics;

import gfx.Skin2D;
import objects.GameObject;
import tiles.Tile;

public class Item extends GameObject
{
	protected int stackSize;

	public Item(String name, Skin2D skin, int width, int height)
	{
		super(name, skin, width, height);
		this.stackSize = 64;
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

	public int getStackSize()
	{
		return stackSize;
	}

	@Override
	public GameObject createNew(int x, int y)
	{
		return new Item(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid);
	}
}

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
		stackSize = 64;
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

	public Item setStackSize(int stackSize)
	{
		this.stackSize = stackSize;
		return this;
	}

	public int getStackSize()
	{
		return stackSize;
	}

	@Override
	public GameObject createNew(int x, int y, boolean virgin)
	{
		if(virgin)
		{
			return new Item(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setPriority(priority);
		}
		else
		{
			// TODO rework Item => if not droppable => delete bounds and
			// solidity else create drop mechanics
			return ((Item)new Item(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT)).setStackSize(stackSize).setSolid(solid).setBounds(bounds).setPriority(priority);
		}
	}
}

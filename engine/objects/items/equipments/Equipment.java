package objects.items.equipments;

import java.awt.Graphics;

import gfx.Skin2D;
import objects.items.Item;

public class Equipment extends Item
{

	public Equipment(String name, Skin2D skin, int width, int height)
	{
		super(name, skin, width, height);
		stackSize = 1;
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

}

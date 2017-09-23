package objects.items.equipments;

import java.awt.Graphics;

import gfx.Skin2D;

public class Weapon extends Equipment
{
	// TODO shields, swords, axes...

	public Weapon(String name, Skin2D skin, int width, int height)
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
}

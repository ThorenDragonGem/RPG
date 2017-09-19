package uis;

import java.awt.Graphics;

import gfx.Skin2D;

public abstract class UI
{
	protected Skin2D skin;
	protected boolean opened;
	/** used to remove it when deleted */
	protected boolean active;
	protected int x, y, width, height;
	protected int priority = 0;

	public abstract void update(double delta);

	public abstract void render(Graphics graphics);

	public int getPriority()
	{
		return priority;
	}

	public UI setPriority(int priority)
	{
		this.priority = priority;
		return this;
	}

	public UI setX(int x)
	{
		this.x = x;
		return this;
	}

	public int getX()
	{
		return x;
	}

	public UI setY(int y)
	{
		this.y = y;
		return this;
	}

	public int getY()
	{
		return y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public Skin2D getSkin()
	{
		return skin;
	}

	public UI setOpened(boolean opened)
	{
		this.opened = opened;
		return this;
	}

	public boolean isOpened()
	{
		return opened;
	}

	public boolean isActive()
	{
		return active;
	}

	public UI setActive(boolean active)
	{
		this.active = active;
		return this;
	}
}

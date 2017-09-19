package objects;

import java.awt.Graphics;
import java.awt.Rectangle;

import engine.Engine;
import gfx.Skin2D;
import physics.Handler;
import tiles.Tile;

public class GameObject
{
	protected Skin2D skin;
	protected String name;
	protected Rectangle bounds;
	protected int x, y, width, height;
	protected boolean active, solid;
	protected int priority = 0;

	/**
	 * @param width in tiles measure
	 * @param height in tiles measure
	 */
	public GameObject(String name, Skin2D skin, int width, int height)
	{
		this.name = name;
		this.skin = skin;
		this.width = width;
		this.height = height;
		/** default (0, 0, 1, 1); */
		bounds = new Rectangle(0, 0, width, height);

		active = true;
		solid = false;
	}

	public void update(double delta)
	{
		skin.update(delta);
	}

	public void render(Graphics graphics)
	{
		// graphics.drawImage(skin.getCurrentSkin(), (int)((x * Tile.TILEWIDTH)
		// - Handler.getCamera().getOffset().x), (int)((y * Tile.TILEHEIGHT) -
		// Handler.getCamera().getOffset().y), Tile.TILEWIDTH, Tile.TILEHEIGHT,
		// null);
		graphics.drawImage(skin.getCurrentSkin(), (int)(x - Handler.getCamera().getOffset().x), (int)(y - Handler.getCamera().getOffset().y), width * Tile.TILEWIDTH, height * Tile.TILEHEIGHT, null);
	}

	public GameObject setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		checkPosition();
		return this;
	}

	private void checkPosition()
	{
		if(x < 0)
		{
			x = 0;
		}
		if(x > ((Handler.getWorld().getWidth() * Tile.TILEWIDTH) - Engine.getWidth()))
		{
			x = (Handler.getWorld().getWidth() * Tile.TILEWIDTH) - Engine.getWidth();
		}
		if(y < 0)
		{
			y = 0;
		}
		if(y > ((Handler.getWorld().getHeight() * Tile.TILEHEIGHT) - Engine.getHeight()))
		{
			y = (Handler.getWorld().getHeight() * Tile.TILEHEIGHT) - Engine.getHeight();
		}
	}

	public int getX()
	{
		return x;
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

	public Rectangle getBounds()
	{
		return bounds;
	}

	public GameObject setBounds(Rectangle bounds)
	{
		this.bounds = bounds;
		return this;
	}

	public GameObject setBounds(int x, int y, int width, int height)
	{
		undoWorldSolidity(this.x, this.y);
		bounds.setBounds(x, y, width, height);
		setSolid(solid);
		return this;
	}

	public String getName()
	{
		return name;
	}

	public Skin2D getSkin()
	{
		return skin;
	}

	public int getPriority()
	{
		return priority;
	}

	public GameObject setPriority(int priority)
	{
		this.priority = priority;
		return this;
	}

	public boolean isActive()
	{
		return active;
	}

	public GameObject setActive(boolean active)
	{
		this.active = active;
		return this;
	}

	public boolean isSolid()
	{
		return solid;
	}

	public GameObject setSolid(boolean solid)
	{
		this.solid = solid;
		for(int i = (x / Tile.TILEWIDTH) + bounds.x; i < ((x / Tile.TILEWIDTH) + bounds.x + bounds.width); i++)
		{
			for(int j = (y / Tile.TILEHEIGHT) + bounds.y; j < ((y / Tile.TILEHEIGHT) + bounds.y + bounds.height); j++)
			{
				Handler.getWorld().setSolid(solid, i, j);
			}
		}
		return this;
	}

	protected GameObject undoWorldSolidity(int lastX, int lastY)
	{
		for(int i = (lastX / Tile.TILEWIDTH) + bounds.x; i < ((lastX / Tile.TILEWIDTH) + bounds.x + bounds.width); i++)
		{
			for(int j = (lastY / Tile.TILEHEIGHT) + bounds.y; j < ((lastY / Tile.TILEHEIGHT) + bounds.y + bounds.height); j++)
			{
				Handler.getWorld().setSolid(false, i, j);
			}
		}
		return this;
	}

	public GameObject createNew(int x, int y)
	{
		return new GameObject(name, skin, width, height).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid);
	}
}

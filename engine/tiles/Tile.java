package tiles;

import java.awt.Graphics;

import gfx.Skin2D;

public class Tile
{
	public static final int TILEWIDTH = 64, TILEHEIGHT = 64;

	private Skin2D skin;
	private boolean solid;

	public Tile(Skin2D skin)
	{
		this.skin = skin;
		solid = false;
	}

	public void update(double delta)
	{
		skin.update(delta);
	}

	public void render(Graphics graphics, int x, int y)
	{
		graphics.drawImage(skin.getCurrentSkin(), x, y, TILEWIDTH, TILEHEIGHT, null);
	}

	public boolean isSolid()
	{
		return solid;
	}

	public Tile setSolid(boolean solid)
	{
		this.solid = solid;
		return this;
	}
}

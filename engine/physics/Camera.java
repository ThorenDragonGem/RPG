package physics;

import org.joml.Vector2f;
import org.joml.Vector2i;

import engine.Engine;
import tiles.Tile;

public class Camera
{
	private Vector2f offset;
	private boolean enabled;

	public Camera(Vector2f position)
	{
		Handler.setCamera(this);
		offset = position;
		enabled = true;
	}

	public Camera()
	{
		this(new Vector2f());
	}

	public void move(float offsetX, float offsetY)
	{
		offset.add(offsetX, offsetY);
		checkBlankSpace();
	}

	public void centerOnPosition(float x, float y)
	{
		// offset.set(x - (Engine.getWidth() / 2) - (Tile.TILEWIDTH / 2), y -
		// (Engine.getHeight() / 2) - (Tile.TILEHEIGHT / 2));
		offset.lerp(new Vector2f((x - (Engine.getWidth() / 2)) + (Tile.TILEWIDTH / 2), (y - (Engine.getHeight() / 2)) + (Tile.TILEHEIGHT / 2)), 0.07f);
		checkBlankSpace();
	}

	private void checkBlankSpace()
	{
		if(offset.x < 0)
		{
			offset.x = 0;
		}
		else if(offset.x > ((Handler.getWorld().getWidth() * Tile.TILEWIDTH) - Engine.getWidth()))
		{
			offset.x = (Handler.getWorld().getWidth() * Tile.TILEWIDTH) - Engine.getWidth();
		}
		if(offset.y < 0)
		{
			offset.y = 0;
		}
		// TODO BUG displacement of Tile.TILE_HEIGHT + 3 (67) pixels along the y
		// axis at the extreme border of the world
		else if(offset.y > (((Handler.getWorld().getHeight() * Tile.TILEHEIGHT) + ((Tile.TILEHEIGHT / 2) - 3)) - Engine.getHeight()))
		{
			offset.y = ((Handler.getWorld().getHeight() * Tile.TILEHEIGHT) + ((Tile.TILEHEIGHT / 2) - 3)) - Engine.getHeight();
		}
	}

	public Vector2i getPickRay()
	{
		return new Vector2i((int)(Engine.inputs.getX() + offset.x), (int)(Engine.inputs.getY() + offset.y));
	}

	public Camera enable()
	{
		enabled = true;
		return this;
	}

	public Camera disable()
	{
		enabled = false;
		return this;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public Vector2f getOffset()
	{
		return offset;
	}
}

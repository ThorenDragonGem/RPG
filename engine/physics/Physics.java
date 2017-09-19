package physics;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import objects.GameObject;
import objects.entities.Entity;
import tiles.Tile;

public class Physics
{
	/**
	 * @return true if go overlaps the circle symbolized by position + radius
	 */
	public static boolean overlaps(int x, int y, int radiusX, int radiusY, Rectangle r)
	{
		int closestX = x;
		int closestY = y;

		if(x < r.x)
		{
			closestX = r.x;
		}
		else if(x > (r.x + r.width))
		{
			closestX = r.x + r.width;
		}

		if(y < r.y)
		{
			closestY = r.y;
		}
		else if(y > (r.y + r.height))
		{
			closestY = r.y + r.height;
		}

		closestX = closestX - x;
		closestX *= closestX;
		closestY = closestY - y;
		closestY *= closestY;

		return (closestX + closestY) < (radiusX * Tile.TILEWIDTH * radiusY * Tile.TILEHEIGHT);
	}

	public static void update(double delta)
	{

	}

	/**
	 * @return all Entity inside rectangle radiusX-radiusY
	 */
	public static List<GameObject> getObjects(int x, int y, int radiusX, int radiusY)
	{
		List<GameObject> res = new ArrayList<>();
		for(GameObject object : Handler.getObjectManager().getAllObjects())
		{
			if(overlaps(x, y, radiusX, radiusY, new Rectangle(object.getX(), object.getY(), Tile.TILEWIDTH, Tile.TILEHEIGHT)))
			{
				res.add(object);
			}
		}
		return res;
	}

	/**
	 * @return all Entity inside rectangle radiusX-radiusY
	 */
	public static List<Entity> getEntities(int x, int y, int radiusX, int radiusY)
	{
		List<Entity> res = new ArrayList<>();
		for(Entity e : Handler.getObjectManager().getEntityManager().getEntities())
		{
			if(overlaps(x, y, radiusX, radiusY, new Rectangle(e.getX(), e.getY(), Tile.TILEWIDTH, Tile.TILEHEIGHT)))
			{
				res.add(e);
			}
		}
		return res;
	}

	/**
	 * @param x in tile measure
	 * @param y in tile measure
	 * @param radius in tile measure
	 * @return all GameObject inside circle of radius radius (x , y) included
	 */
	public static List<GameObject> getObjectsCircle(int x, int y, int radius)
	{
		List<GameObject> res = new ArrayList<>();
		for(GameObject object : Handler.getObjectManager().getAllObjects())
		{
			if(length(x + (1 / 2), y + (1 / 2), (object.getX() / Tile.TILEWIDTH) + (1 / 2), (object.getY() / Tile.TILEHEIGHT) + (1 / 2)) < (radius + 1))
			{
				res.add(object);
			}
		}
		return res;
	}

	/**
	 * @param x in tile measure
	 * @param y in tile measure
	 * @param radius in tile measure
	 * @return all Entity inside circle of radius radius (x , y) included
	 */
	public static List<Entity> getEntitiesCircle(int x, int y, int radius)
	{
		List<Entity> res = new ArrayList<>();
		for(Entity e : Handler.getObjectManager().getEntityManager().getEntities())
		{
			if(length(x + (1 / 2), y + (1 / 2), (e.getX() / Tile.TILEWIDTH) + (1 / 2), (e.getY() / Tile.TILEHEIGHT) + (1 / 2)) < (radius + 1))
			{
				res.add(e);
			}
		}
		return res;
	}

	public static int length(int x1, int y1, int x2, int y2)
	{
		int x = x2 - x1;
		int y = y2 - y1;
		return (int)Math.sqrt((x * x) + (y * y));
	}

	public static boolean collides(Vector2i pos, GameObject o)
	{
		return new Rectangle(o.getX(), o.getY(), o.getWidth() * Tile.TILEWIDTH, o.getHeight() * Tile.TILEHEIGHT).intersects(new Rectangle(pos.x, pos.y, 1, 1));
	}
}

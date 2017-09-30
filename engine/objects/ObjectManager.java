package objects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import engine.Engine;
import objects.blocks.Block;
import objects.blocks.BlockManager;
import objects.entities.Entity;
import objects.entities.EntityManager;
import objects.entities.creatures.Player;
import objects.items.Item;
import objects.items.ItemManager;
import physics.Handler;
import tiles.Tile;

public class ObjectManager
{
	private List<GameObject> objects;
	private EntityManager entities;
	private BlockManager blocks;
	private ItemManager items;

	public ObjectManager(Player player)
	{
		Handler.setObjectManager(this);
		objects = new LinkedList<>();
		entities = new EntityManager(player);
		blocks = new BlockManager();
		items = new ItemManager();
	}

	/**
	 * Sends each GameObject in its proper manager;
	 * if this object hasn't its manager,
	 * the GameObject stay in objects List
	 */
	public void update(double delta)
	{
		Iterator<GameObject> it = objects.iterator();
		// sort between instances and update first anonymous objects
		while(it.hasNext())
		{
			GameObject go = it.next();
			if(go instanceof Block)
			{
				blocks.add((Block)go);
				it.remove();
				continue;
			}
			if(go instanceof Item)
			{
				items.add((Item)go);
				it.remove();
				continue;
			}
			if(go instanceof Entity)
			{
				entities.add((Entity)go);
				it.remove();
				continue;
			}
			go.update(delta);
			if(!go.isActive())
			{
				it.remove();
				go.undoWorldSolidity(go.x, go.y);
			}
		}
		// next blocks
		blocks.update(delta);
		// next items
		items.update(delta);
		// next entities
		entities.update(delta);
	}

	public void updateRender(double delta)
	{
		for(GameObject object : objects)
		{
			object.updateRender(delta);
		}
		blocks.updateRender(delta);
		items.updateRender(delta);
		entities.updateRender(delta);
	}

	public void render(Graphics graphics)
	{
		for(GameObject object : objects)
		{
			if(((object.getX() + (object.getWidth() * Tile.TILEWIDTH)) > Handler.getCamera().getOffset().x) && ((object.getX() - (object.getWidth() * Tile.TILEWIDTH)) <= (Engine.getWidth() + Handler.getCamera().getOffset().x)))
			{
				if(((object.getY() + (object.getHeight() * Tile.TILEHEIGHT)) > Handler.getCamera().getOffset().y) && ((object.getY() - (object.getHeight() * Tile.TILEHEIGHT)) <= (Engine.getHeight() + Handler.getCamera().getOffset().y)))
				{
					object.render(graphics);
				}
			}
		}
		blocks.render(graphics);
		items.render(graphics);
		entities.render(graphics);
		for(GameObject object : objects)
		{
			if(((object.getX() + (object.getWidth() * Tile.TILEWIDTH)) > Handler.getCamera().getOffset().x) && ((object.getX() - (object.getWidth() * Tile.TILEWIDTH)) <= (Engine.getWidth() + Handler.getCamera().getOffset().x)))
			{
				if(((object.getY() + (object.getHeight() * Tile.TILEHEIGHT)) > Handler.getCamera().getOffset().y) && ((object.getY() - (object.getHeight() * Tile.TILEHEIGHT)) <= (Engine.getHeight() + Handler.getCamera().getOffset().y)))
				{
					object.renderOver(graphics);
				}
			}
		}
		blocks.renderOver(graphics);
		items.renderOver(graphics);
		entities.renderOver(graphics);
	}

	public void add(GameObject object)
	{
		if(objects.contains(object))
		{
			return;
		}
		objects.add(object);
		// don't check on start => check on addingItem during process
		// checkObjectPositionAndSpawn(object);
	}

	public void remove(GameObject object)
	{
		if(!objects.contains(object))
		{
			return;
		}
		object.active = false;
	}

	/**
	 * Doesn't seam to work!
	 */
	@Deprecated
	private void checkObjectPositionAndSpawn(GameObject object)
	{
		if(Handler.getWorld().isSolid((int)((object.getX() - Handler.getCamera().getOffset().x) / Tile.TILEWIDTH), (int)((object.getY() - Handler.getCamera().getOffset().y) / Tile.TILEHEIGHT)))
		{
			boolean done = false;
			int r = 0;
			int a = 0, b = 0;
			boolean[][] tilesAround = new boolean[11][11];
			tilesAround[0][0] = true;
			while(!done)
			{
				if(r > 5)
				{
					break;
				}
				for(int i = 0; i < ((2 * r) + 1); i++)
				{
					for(int j = 0; j < ((2 * r) + 1); j++)
					{
						if((tilesAround[i][j] == true) || (((object.x / Tile.TILEWIDTH) - i - r) < 0) || (((object.x / Tile.TILEWIDTH) - i - r) > Handler.getWorld().getWidth()) || ((object.y - j - r) < 0) || ((object.y - j - r) > Handler.getWorld().getHeight()))
						{
							continue;
						}
						if(!Handler.getWorld().isSolid(i - r, j - r))
						{
							System.out.println(((i - r) + "  " + (j - r)));
							done = true;
							a = i;
							b = j;
						}
						tilesAround[i][j] = true;
					}
				}
				r++;
			}
			if(r > 5)
			{
				return;
			}
			object.x = a * Tile.TILEWIDTH;
			object.y = b * Tile.TILEHEIGHT;
			objects.add(object);
		}
		// the position of the object is on a non solid Tile
		else
		{
			objects.add(object);
		}
	}

	public List<GameObject> getObjects()
	{
		return objects;
	}

	public List<GameObject> getAllObjects()
	{
		List<GameObject> res = new ArrayList<>();
		res.addAll(objects);
		res.addAll(blocks.getBlocks());
		res.addAll(items.getItems());
		res.addAll(entities.getEntities());
		return res;
	}

	public BlockManager getBlockManager()
	{
		return blocks;
	}

	public ItemManager getItemManager()
	{
		return items;
	}

	public EntityManager getEntityManager()
	{
		return entities;
	}
}

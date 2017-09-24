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
import objects.entities.Player;
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
			}
		}
		// next blocks
		blocks.update(delta);
		// next items
		items.update(delta);
		// next entities
		entities.update(delta);

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
	}

	public void add(GameObject object)
	{
		if(objects.contains(object))
		{
			return;
		}
		objects.add(object);
	}

	public void remove(GameObject object)
	{
		if(!objects.contains(object))
		{
			return;
		}
		object.active = false;
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

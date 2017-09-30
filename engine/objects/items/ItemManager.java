package objects.items;

import java.awt.Graphics;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import engine.Engine;
import physics.Handler;
import tiles.Tile;

public class ItemManager
{
	private List<Item> items;
	private Comparator<Item> renderSorter = new Comparator<Item>()
	{
		@Override
		public int compare(Item a, Item b)
		{
			if((a.getY() + a.getHeight()) == (b.getY() + b.getHeight()))
			{
				if(a.getPriority() == b.getPriority())
				{
					String s1 = a.getName();
					String s2 = b.getName();
					return s1.compareTo(s2);
				}
				else
				{
					return Integer.compare(a.getPriority(), b.getPriority());
				}
			}
			else if((a.getY() + a.getHeight()) < (b.getY() + b.getHeight()))
			{
				return -1;
			}
			return 1;
		}
	};

	public ItemManager()
	{
		items = new LinkedList<>();
	}

	public void update(double delta)
	{
		Iterator<Item> it = items.iterator();
		while(it.hasNext())
		{
			Item item = it.next();
			item.update(delta);
			if(!item.isActive())
			{
				it.remove();
				item.undoWorldSolidity(item.getX(), item.getY());
			}
		}
		items.sort(renderSorter);
	}

	public void updateRender(double delta)
	{
		for(Item item : items)
		{
			item.updateRender(delta);
		}
	}

	public void render(Graphics graphics)
	{
		for(Item item : items)
		{
			if(((item.getX() + (item.getWidth() * Tile.TILEWIDTH)) > Handler.getCamera().getOffset().x) && ((item.getX() - (item.getWidth() * Tile.TILEWIDTH)) <= (Engine.getWidth() + Handler.getCamera().getOffset().x)))
			{
				if(((item.getY() + (item.getHeight() * Tile.TILEHEIGHT)) > Handler.getCamera().getOffset().y) && ((item.getY() - (item.getHeight() * Tile.TILEHEIGHT)) <= (Engine.getHeight() + Handler.getCamera().getOffset().y)))
				{
					item.render(graphics);
				}
			}
		}
	}

	public void renderOver(Graphics graphics)
	{
		for(Item item : items)
		{
			if(((item.getX() + (item.getWidth() * Tile.TILEWIDTH)) > Handler.getCamera().getOffset().x) && ((item.getX() - (item.getWidth() * Tile.TILEWIDTH)) <= (Engine.getWidth() + Handler.getCamera().getOffset().x)))
			{
				if(((item.getY() + (item.getHeight() * Tile.TILEHEIGHT)) > Handler.getCamera().getOffset().y) && ((item.getY() - (item.getHeight() * Tile.TILEHEIGHT)) <= (Engine.getHeight() + Handler.getCamera().getOffset().y)))
				{
					item.renderOver(graphics);
				}
			}
		}
	}

	public void add(Item item)
	{
		if(items.contains(item))
		{
			return;
		}
		items.add(item);
	}

	public void remove(Item item)
	{
		if(!items.contains(item))
		{
			return;
		}
		item.setActive(false);
	}

	public List<Item> getItems()
	{
		return items;
	}
}

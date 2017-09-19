package objects.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import engine.Engine;
import physics.Handler;
import tiles.Tile;

public class EntityManager
{
	private Player player;
	private List<Entity> entities;
	private Comparator<Entity> renderSorter = new Comparator<Entity>()
	{
		@Override
		public int compare(Entity a, Entity b)
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

	public EntityManager(Player player)
	{
		this.player = (Player)player.createNew(1, 1);
		entities = new ArrayList<>();
		add(this.player);
	}

	public void update(double delta)
	{
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext())
		{
			Entity e = it.next();
			// isPaused() ?
			// if(!Engine.isPaused())
			// {
			// e.update(delta);
			// }
			// else if(e instanceof Player)
			// {
			e.update(delta);
			// }
			if(!e.isActive())
			{
				it.remove();
			}
		}
		entities.sort(renderSorter);
		Handler.getCamera().centerOnPosition(player.getX(), player.getY());
	}

	public void render(Graphics graphics)
	{
		// different 'for' loop => collision detection
		for(Entity e : entities)
		{
			if(((e.getX() + (e.getWidth() * Tile.TILEWIDTH)) > Handler.getCamera().getOffset().x) && ((e.getX() - (e.getWidth() * Tile.TILEWIDTH)) <= (Engine.getWidth() + Handler.getCamera().getOffset().x)))
			{
				if(((e.getY() + (e.getHeight() * Tile.TILEHEIGHT)) > Handler.getCamera().getOffset().y) && ((e.getY() - (e.getHeight() * Tile.TILEHEIGHT)) <= (Engine.getHeight() + Handler.getCamera().getOffset().y)))
				{
					e.render(graphics);
				}
			}
		}
	}

	public void add(Entity e)
	{
		if(entities.contains(e))
		{
			return;
		}
		entities.add(e);
	}

	public void remove(Entity e)
	{
		if(!entities.contains(e))
		{
			return;
		}
		e.setActive(false);
	}

	public Player getPlayer()
	{
		return player;
	}

	public List<Entity> getEntities()
	{
		return entities;
	}
}
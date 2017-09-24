package physics;

import java.awt.Graphics;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import engine.Engine;
import objects.ObjectManager;
import uis.UI;
import utils.Mathf;
import worlds.World;

public class Handler
{
	private static List<UI> uis;
	private static Comparator<UI> uisSorter = new Comparator<UI>()
	{
		@Override
		public int compare(UI a, UI b)
		{
			if(a.getPriority() == b.getPriority())
			{
				return (int)Mathf.random(-9, 9);
			}
			else if(a.getPriority() < b.getPriority())
			{
				return -10;
			}
			return 10;
		};
	};
	private static ObjectManager manager;
	private static Camera camera;
	private static Handler handler;
	private static World world;

	public Handler()
	{
		Handler.handler = this;
		uis = new LinkedList<>();
	}

	public static void update(double delta)
	{
		if(world != null)
		{
			world.update(delta);
		}
		if(manager != null)
		{
			if(!Engine.isPaused())
			{
				manager.update(delta);
			}
		}
		Iterator<UI> it = uis.iterator();
		while(it.hasNext())
		{
			UI next = it.next();
			next.update(delta);
			if(!next.isActive())
			{
				it.remove();
			}
		}
	}

	public static void render(Graphics graphics)
	{
		if(world != null)
		{
			world.render(graphics);
		}
		if(manager != null)
		{
			manager.render(graphics);
		}
		uis.sort(uisSorter);
		for(UI ui : uis)
		{
			ui.render(graphics);
		}
	}

	public static Camera getCamera()
	{
		return camera;
	}

	public static Handler setCamera(Camera camera)
	{
		Handler.camera = camera;
		return handler;
	}

	public static World getWorld()
	{
		return world;
	}

	public static Handler setWorld(World world)
	{
		Handler.world = world;
		return handler;
	}

	public static ObjectManager getObjectManager()
	{
		return manager;
	}

	public static Handler setObjectManager(ObjectManager objectManager)
	{
		Handler.manager = objectManager;
		return handler;
	}

	public static List<UI> getUis()
	{
		return uis;
	}
}

package objects.blocks;

import java.awt.Graphics;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import engine.Engine;
import physics.Handler;
import tiles.Tile;

public class BlockManager
{
	private List<Block> blocks;
	private Comparator<Block> renderSorter = new Comparator<Block>()
	{
		@Override
		public int compare(Block a, Block b)
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

	public BlockManager()
	{
		blocks = new LinkedList<>();
	}

	public void update(double delta)
	{
		Iterator<Block> it = blocks.iterator();
		while(it.hasNext())
		{
			Block block = it.next();
			block.update(delta);
			if(!block.isActive())
			{
				it.remove();
			}
		}
		blocks.sort(renderSorter);
	}

	public void render(Graphics graphics)
	{
		for(Block block : blocks)
		{
			if(((block.getX() + (block.getWidth() * Tile.TILEWIDTH)) > Handler.getCamera().getOffset().x) && ((block.getX() - (block.getWidth() * Tile.TILEWIDTH)) <= (Engine.getWidth() + Handler.getCamera().getOffset().x)))
			{
				if(((block.getY() + (block.getHeight() * Tile.TILEHEIGHT)) > Handler.getCamera().getOffset().y) && ((block.getY() - (block.getHeight() * Tile.TILEHEIGHT)) <= (Engine.getHeight() + Handler.getCamera().getOffset().y)))
				{
					block.render(graphics);
				}
			}
		}
	}

	public void add(Block block)
	{
		if(blocks.contains(block))
		{
			return;
		}
		blocks.add(block);
	}

	public void remove(Block block)
	{
		if(!blocks.contains(block))
		{
			return;
		}
		block.setActive(false);
	}

	public List<Block> getBlocks()
	{
		return blocks;
	}
}

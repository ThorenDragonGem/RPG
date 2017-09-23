package objects.items.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import objects.entities.Entity;
import objects.items.Item;

public class Inventory
{
	protected List<InventoryCell> cells;
	protected int slots;
	protected Entity holder;

	public Inventory(Entity holder, int slots)
	{
		this.holder = holder;
		cells = new ArrayList<>();
		this.slots = slots;
	}

	public List<InventoryCell> getCells()
	{
		return cells;
	}

	public Inventory setCells(List<InventoryCell> cells)
	{
		this.cells = cells;
		return this;
	}

	public boolean addItem(Item item)
	{
		for(int i = 0; i < slots; i++)
		{
			if((cells.size() > i) && cells.get(i).getType().getName().equals(item.getName()))
			{
				if(!cells.get(i).isFull())
				{
					cells.get(i).add(item);
					return true;
				}
			}
		}
		if(cells.size() < slots)
		{
			createNewCell(item);
			return true;
		}
		return false;
	}

	private void createNewCell(Item item)
	{
		InventoryCell cell = new InventoryCell();
		cell.add(item);
		cells.add(cell);
	}

	public Item remove(InventoryCell cell)
	{
		Item item = cell.remove();
		if(cell.getSize() == 0)
		{
			cells.remove(getCellIndex(cell.getType()));
		}
		return item;
	}

	public void update(double delta)
	{
		Iterator<InventoryCell> iterator = cells.iterator();
		while(iterator.hasNext())
		{
			InventoryCell cell = iterator.next();
			if(cell.getSize() <= 0)
			{
				iterator.remove();
			}
		}
	}

	public int getCellIndex(Item type)
	{
		for(int i = 0; i < cells.size(); i++)
		{
			if(cells.get(i).getType() == type)
			{
				return i;
			}
		}
		return -1;
	}

	public Entity getHolder()
	{
		return holder;
	}

	public int getSlots()
	{
		return slots;
	}

	@Override
	public String toString()
	{
		String string = "[";
		for(int i = 0; i < cells.size(); i++)
		{
			if((cells.get(i) == null) || (cells.get(i).getType() == null))
			{
				string += "(null)";
			}
			else
			{
				string += "(" + i + ":" + cells.get(i).getType().getName() + "|" + cells.get(i).getSize() + "/" + cells.get(i).getType().getStackSize() + ")";
			}
		}
		return string += "]";
	}
}

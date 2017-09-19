package objects.items.inventory;

import objects.items.Item;

public class InventoryCell
{
	private Item[] stack;
	private Item type;
	private int size;

	public InventoryCell()
	{
		size = 0;
	}

	public void add(Item item)
	{
		if(stack == null)
		{
			type = item;
			stack = new Item[item.getStackSize()];
		}
		stack[getNextFreeSlot()] = item;
		size++;
	}

	public Item remove()
	{
		if(stack == null)
		{
			return null;
		}
		stack[size - 1] = null;
		size--;
		return type;
	}

	public boolean isFull()
	{
		return size == type.getStackSize();
	}

	public int getIndex(Item item)
	{
		for(int i = 0; i < stack.length; i++)
		{
			if(stack[i] == item)
			{
				return i;
			}
		}
		return -1;
	}

	public int getNextFreeSlot()
	{
		for(int i = 0; i < stack.length; i++)
		{
			if(stack[i] == null)
			{
				return i;
			}
		}
		return -1;
	}

	public Item getType()
	{
		return type;
	}

	public int getSize()
	{
		return size;
	}
}

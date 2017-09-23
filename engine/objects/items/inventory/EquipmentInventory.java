package objects.items.inventory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import objects.entities.Entity;
import objects.items.equipments.Equipment;

public class EquipmentInventory
{
	// TODO add HashMap<Equipment, Integer> equipments cells => add equipment
	// inventory => closed inventory so attribute Equipment to its specific cell
	// (or one of the two: talismans and weapons)
	// => add new UI: EquipmentInventoryRenderer
	// protected List<Equipment> equipmentInventory;
	// protected HashMap<Integer, Equipment> equipmentInventory;
	protected List<EquipmentCell> equipmentInventory;
	protected Entity holder;
	protected int slots;

	public EquipmentInventory(Entity holder, Class<? extends Equipment>... cellsEquipmentTypes)
	{
		equipmentInventory = new ArrayList<>();
		slots = cellsEquipmentTypes.length;
		for(int index = 0; index < cellsEquipmentTypes.length; index++)
		{
			equipmentInventory.add(index, new EquipmentCell(cellsEquipmentTypes[index]));
		}
	}

	public List<EquipmentCell> getEquipmentInventory()
	{
		return equipmentInventory;
	}

	/**
	 * Sets the Equipment on its own slot.
	 * But, if there is more than one slot for this Equipment,
	 * specify the slot to put the Equipment in
	 * @param e The Equipment to put in Inventory
	 * @param slot The specific slot for the Equipment (usually equals to 0)
	 * @return The replaced Equipment (return null if there wasn't one)
	 */
	public Equipment setEquipment(Equipment e, int slot)
	{
		List<Integer> indexes = getEquipmentIndexes(e);
		// test if the new Equipment has its own slot in the inventory
		if(indexes.isEmpty())
		{
			return null;
		}
		// return the cell at index: indexes[slot]
		// if more than one cell of Equipment 'e'
		// return the the number 'slot' cell of the Equipment Array
		// if only one return the index 0 => the only one available
		return equipmentInventory.get(indexes.get(slot)).set(e);
	}

	public void remove(int index)
	{
		if((index >= 0) && (index < equipmentInventory.size()))
		{
			equipmentInventory.get(index).remove();
		}
	}

	public void clear()
	{
		for(int i = 0; i < equipmentInventory.size(); i++)
		{
			remove(i);
		}
	}

	public List<Integer> getEquipmentIndexes(Equipment e)
	{
		LinkedList<Integer> indexes = new LinkedList<>();
		for(int i = 0; i < equipmentInventory.size(); i++)
		{
			if(equipmentInventory.get(i).getType().equals(getSuperClasses(e)))
			{
				indexes.add(i);
			}
		}
		return indexes;
	}

	/**
	 * @return the upper Class of the Equipment (Helm, Parchment, Weapon...)
	 */
	public Class<? super Equipment> getSuperClasses(Equipment e)
	{
		Class<?> c = e.getClass();
		if(e.getClass().getSuperclass() == Equipment.class)
		{
			return (Class<? super Equipment>)c;
		}
		c = e.getClass().getSuperclass();
		Class<?> store = e.getClass().getSuperclass();
		while(store != Equipment.class)
		{
			store = store.getSuperclass();
			if(store != Equipment.class)
			{
				c = c.getSuperclass();
			}
		}
		return (Class<? super Equipment>)c;
	}

	@Override
	public String toString()
	{
		String s = "";
		for(int i = 0; i < equipmentInventory.size(); i++)
		{
			if(equipmentInventory.get(i) == null)
			{
				s += "[NULL] ; ";
			}
			else
			{
				s += equipmentInventory.get(i).toString() + " ; ";
			}
		}
		return s.substring(0, s.length() - 3);
	}
}

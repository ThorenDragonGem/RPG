package objects.items.inventory;

import objects.items.equipments.Equipment;

public class EquipmentCell
{
	private Class<? extends Equipment> type;
	private Equipment equipment;

	public EquipmentCell(Class<? extends Equipment> equipment)
	{
		type = equipment;
		this.equipment = null;
	}

	public Equipment set(Equipment equipment)
	{
		if(equipment == null)
		{
			Equipment last = this.equipment;
			this.equipment = null;
			return last;
		}
		if(type.equals(getSuperClasses(equipment)))
		{
			Equipment last = this.equipment;
			this.equipment = equipment;
			return last;
		}
		else
		{
			return null;
		}
	}

	public Equipment remove()
	{
		Equipment e = equipment;
		equipment = null;
		return e;
	}

	public boolean isFull()
	{
		return equipment != null;
	}

	public Class<? extends Equipment> getType()
	{
		return type;
	}

	public Equipment getEquipment()
	{
		return equipment;
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
		return "[E:" + (equipment == null ? "null" : equipment.getName()) + " ; " + (type == null ? "TYPE = NULL!" : type.getName()) + "]";
	}
}

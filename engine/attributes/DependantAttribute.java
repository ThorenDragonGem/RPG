package attributes;

import java.util.HashMap;
import java.util.Map;

public class DependantAttribute extends Attribute
{
	protected Map<String, Attribute> otherAttributes;

	public DependantAttribute(double startingValue, double multiplier)
	{
		super(startingValue, multiplier);
		otherAttributes = new HashMap<>();
	}

	public void addAttribute(String name, Attribute attribute)
	{
		otherAttributes.put(name, attribute);
	}

	public void removeAttribute(String name)
	{
		if(otherAttributes.containsKey(name))
		{
			otherAttributes.remove(name);
		}
	}

	public void removeAttribute(Attribute attribute)
	{
		for(String name : otherAttributes.keySet())
		{
			if(otherAttributes.get(name) == attribute)
			{
				otherAttributes.remove(name);
			}
		}
	}

	public void removeAll()
	{
		for(Attribute attribute : otherAttributes.values())
		{
			removeAttribute(attribute);
		}
	}

	@Override
	public void updateFinalBonus()
	{
		if(otherAttributes.values().size() == 0)
		{
			super.updateFinalBonus();
		}
		else
		{
			for(Attribute attribute : otherAttributes.values())
			{
				attribute.updateFinalBonus();
			}
		}
	}

	@Override
	public double calculateValue()
	{
		// specific attribute code goes here
		finalValue = getBaseValue();
		applyRawBonuses();
		applyFinalBonuses();
		for(Attribute attribute : otherAttributes.values())
		{
			finalValue += attribute.calculateValue();
		}
		finalValue *= (1 + multiplier);
		return finalValue;
	}

	public Map<String, Attribute> getAttributes()
	{
		return otherAttributes;
	}

	@Override
	public String toString()
	{
		String string = "";
		for(Attribute a : otherAttributes.values())
		{
			string += a;
		}
		return string;
	}
}

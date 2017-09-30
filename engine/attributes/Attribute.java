package attributes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Attribute extends BaseAttribute
{
	protected List<RawBonus> rawBonuses;
	protected List<FinalBonus> finalBonuses;
	protected double finalValue;
	protected double multiplier;

	public Attribute(double value, double addedMultiplier)
	{
		super(value, addedMultiplier);
		rawBonuses = new CopyOnWriteArrayList<>();
		finalBonuses = new CopyOnWriteArrayList<>();
		multiplier = addedMultiplier;
		finalValue = getBaseValue() * (1 + multiplier);
	}

	public void addRawBonus(RawBonus bonus)
	{
		rawBonuses.add(bonus);
	}

	public Attribute addFinalBonus(FinalBonus bonus)
	{
		finalBonuses.add(bonus);
		return this;
	}

	public void removeRawBonus(RawBonus bonus)
	{
		if(rawBonuses.contains(bonus))
		{
			rawBonuses.remove(bonus);
		}
	}

	public void removeFinalBonus(FinalBonus bonus)
	{
		if(finalBonuses.contains(bonus))
		{
			finalBonuses.remove(bonus);
		}
	}

	protected void applyRawBonuses()
	{
		double rawBonusValue = 0;
		double rawBonusMultiplier = 0;
		for(RawBonus bonus : rawBonuses)
		{
			rawBonusValue += bonus.getBaseValue();
			rawBonusMultiplier += bonus.getBaseMultiplier();
		}

		finalValue += rawBonusValue;
		finalValue *= (1 + rawBonusMultiplier);
	}

	protected void applyFinalBonuses()
	{
		double finalBonusValue = 0;
		double finalBonusMultiplier = 0;
		for(FinalBonus bonus : finalBonuses)
		{
			finalBonusValue += bonus.getBaseValue();
			finalBonusMultiplier += bonus.getBaseMultiplier();
		}

		finalValue += finalBonusValue;
		finalValue *= (1 + finalBonusMultiplier);
	}

	public double calculateValue()
	{
		// added base multiplier
		// add bonuses and multiply the sum by multiplier (>/=/< 1)
		finalValue = getBaseValue();
		applyRawBonuses();
		applyFinalBonuses();
		finalValue *= (1 + multiplier);
		return finalValue;
	}

	public void updateFinalBonus()
	{
		for(FinalBonus b : finalBonuses)
		{
			if(b.getTimer() != null)
			{
				b.update();
			}
		}
	}

	public List<RawBonus> getRawBonuses()
	{
		return rawBonuses;
	}

	public List<FinalBonus> getFinalBonuses()
	{
		return finalBonuses;
	}

	public double getFinalValue()
	{
		return calculateValue();
	}

	@Override
	public String toString()
	{
		String s = "{A:";
		for(RawBonus r : rawBonuses)
		{
			s += r + " ; ";
		}
		for(FinalBonus f : finalBonuses)
		{
			s += f + " ; ";
		}
		if(s.endsWith(" ; "))
		{
			s = s.substring(0, s.length() - 3);
		}
		s += "}";
		return s;
	}
}

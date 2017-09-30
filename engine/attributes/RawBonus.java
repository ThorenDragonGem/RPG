package attributes;

public class RawBonus extends BaseAttribute
{
	public RawBonus(double value, double multiplier)
	{
		super(value, multiplier);
	}

	@Override
	public String toString()
	{
		return "[b:" + getBaseValue() + "x" + getBaseMultiplier() + "]";
	}
}

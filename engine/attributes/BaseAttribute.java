package attributes;

public class BaseAttribute
{
	private double baseValue;
	private double baseMultiplier;

	public BaseAttribute(double value, double multiplier)
	{
		baseValue = value;
		baseMultiplier = multiplier;
	}

	public BaseAttribute(double value)
	{
		this(value, 0);
	}

	public double getBaseValue()
	{
		return baseValue;
	}

	public double getBaseMultiplier()
	{
		return baseMultiplier;
	}
}

package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormat;

public class Text
{
	/**
	 * Draws the String string centered at center or at left on (xPos,yPos)
	 */
	public static void drawString(Graphics graphics, String text, int xPos, int yPos, boolean center, Color c, Font font)
	{
		graphics.setColor(c);
		graphics.setFont(font);
		int x = xPos;
		int y = yPos;
		if(center)
		{
			FontMetrics fm = graphics.getFontMetrics();
			x = xPos - fm.stringWidth(text) / 2;
			y = (yPos - fm.getHeight() / 2) + fm.getAscent();
		}
		graphics.drawString(text, x, y);
	}

	/**
	 * Draws the String string centered at right on (xPos,yPos)
	 */
	public static void drawStringNegate(Graphics graphics, String text, int xPos, int yPos, Color c, Font font)
	{
		graphics.setColor(c);
		graphics.setFont(font);
		int x = xPos;
		int y = yPos;
		FontMetrics fm = graphics.getFontMetrics();
		x = xPos - fm.stringWidth(text);
		graphics.drawString(text, x, y);
	}

	public static String getThousandsStringd(double value)
	{
		if(value == 0)
			return "0,00";
		int thousands = 0;
		double res = value;
		while(res >= 1000)
		{
			res /= 1000;
			thousands++;
		}
		switch(thousands)
		{
			case 0:
				return getTwoDigits(value);
			case 1:
				return getTwoDigits(value / 1000d) + "k";
			case 2:
				return getTwoDigits(value / 1000000d) + "M";
			case 3:
				return getTwoDigits(value / 1000000000d) + "G";
			case 4:
				return getTwoDigits(value / 1000000000000d) + "T";
		}
		return getTwoDigits(value / 1000000000000d) + "T";
	}

	public static String getThousandsStringi(double value)
	{
		if(value == 0)
			return "0";
		int thousands = 0;
		double res = value;
		while(res >= 1000)
		{
			res /= 1000;
			thousands++;
		}
		switch(thousands)
		{
			case 0:
				return (int)value + "";
			case 1:
				return (int)(value / 1000d) + "k";
			case 2:
				return (int)(value / 1000000d) + "M";
			case 3:
				return (int)(value / 1000000000d) + "G";
			case 4:
				return (int)(value / 1000000000000d) + "T";
		}
		return (int)(value / 1000000000000d) + "T";
	}

	public static String getTwoDigits(double value)
	{
		return new DecimalFormat("##.00").format(value);
	}

}

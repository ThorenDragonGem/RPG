package uis;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import engine.Engine;
import gfx.Colors;
import objects.items.Item;

public class ItemUI extends UI
{
	private Item item;
	private List<String> properties;

	public ItemUI(Item item)
	{
		this.item = item;
		active = true;
		opened = false;
		properties = new LinkedList<>();
		priority = 50;
	}

	@Override
	public void update(double delta)
	{
		if(!opened || (item == null))
		{
			return;
		}
		properties.clear();
		splitString(item.getName(), 25, true);
		splitString("This is the Lore of the itemizationoftheDarkLordzubfusufpiuzb and it is not readable for now. Please come later!", 25, false);
	}

	@Override
	public void render(Graphics graphics)
	{
		if(!opened || (item == null))
		{
			return;
		}
		FontMetrics metrics = graphics.getFontMetrics();
		x = Engine.inputs.getX() + 10;
		y = Engine.inputs.getY() + 10;
		width = metrics.stringWidth("_twenty_eigth_letters_length_");
		if((x + width) > Engine.getWidth())
		{
			x -= 2 * width;
		}
		height = (int)(metrics.getAscent() * (properties.size() + 0.5f));
		graphics.setColor(Colors.gray30);
		graphics.fillRoundRect(x, y, width, height, 30, 30);
		graphics.setColor(Color.white);
		for(int i = 0; i < properties.size(); i++)
		{
			graphics.drawString(properties.get(i), x + metrics.stringWidth("12"), y + ((i + 1) * (metrics.getAscent())));
		}
	}

	public void splitString(String string, int length, boolean nextLine)
	{
		String s = WordUtils.wrap(string, 25, "\n", true);
		String[] lines = s.split("\n");
		for(String str : lines)
		{
			properties.add(str);
		}
	}

	public ItemUI setItem(Item item)
	{
		this.item = item;
		return this;
	}

	public Item getItem()
	{
		return item;
	}
}

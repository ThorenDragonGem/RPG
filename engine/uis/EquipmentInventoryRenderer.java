package uis;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import assets.Assets;
import engine.Engine;
import objects.items.inventory.EquipmentCell;
import objects.items.inventory.EquipmentInventory;

public class EquipmentInventoryRenderer extends UI
{
	private EquipmentInventory inventory;
	private List<Vector2i> cellsPositions;

	private int imageWidth = 64, imageHeight = 64;

	public EquipmentInventoryRenderer(EquipmentInventory inventory)
	{
		this.inventory = inventory;
		cellsPositions = new ArrayList<>();
		// helm
		cellsPositions.add(new Vector2i(184, 16));
		// chestplate
		cellsPositions.add(new Vector2i(184, 116));
		// belt
		cellsPositions.add(new Vector2i(184, 209));
		// greaves
		cellsPositions.add(new Vector2i(184, 290));
		// boot1
		cellsPositions.add(new Vector2i(441, 373));
		// boot2
		cellsPositions.add(new Vector2i(228, 373));
		// talisman1
		cellsPositions.add(new Vector2i(52, 27));
		// talisman2
		cellsPositions.add(new Vector2i(316, 27));
		// shoulderpad1
		cellsPositions.add(new Vector2i(92, 101));
		// shoulderpad2
		cellsPositions.add(new Vector2i(276, 101));
		// armband1
		cellsPositions.add(new Vector2i(92, 182));
		// armband2
		cellsPositions.add(new Vector2i(276, 182));
		// gauntlets1
		cellsPositions.add(new Vector2i(100, 263));
		// gauntlets2
		cellsPositions.add(new Vector2i(268, 263));
		// weapon1
		cellsPositions.add(new Vector2i(19, 344));
		// weapon2
		cellsPositions.add(new Vector2i(349, 344));
		// mantle
		cellsPositions.add(new Vector2i(354, 168));
		// parchment
		cellsPositions.add(new Vector2i(14, 167));
		active = true;
		active = true;
		skin = Assets.getTexture("equipmentInv");
		width = 432;
		height = 464;
		x = (Engine.getWidth() / 2) - (width / 2);
		y = (Engine.getHeight() / 2) - (height / 2);
		priority = 80;
	}

	@Override
	public void update(double delta)
	{
		if(!opened)
		{
			return;
		}
	}

	@Override
	public void render(Graphics graphics)
	{
		if(!opened)
		{
			return;
		}
		graphics.drawImage(skin.getCurrentSkin(), x, y, width, height, null);
		int i = 0;
		for(EquipmentCell cell : inventory.getEquipmentInventory())
		{
			if((cell != null) && (cell.getEquipment() != null) && (cell.getEquipment().getSkin() != null))
			{
				graphics.drawImage(cell.getEquipment().getSkin().getCurrentSkin(), x + cellsPositions.get(i).x, y + cellsPositions.get(i).y, imageWidth, imageHeight, null);
			}
			i++;
		}
	}

}

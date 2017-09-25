package uis;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Vector2i;
import org.joml.Vector4i;

import assets.Assets;
import core.Core;
import engine.Engine;
import inputs.Keyboard;
import inputs.Mouse;
import objects.items.equipments.*;
import objects.items.inventory.EquipmentCell;
import objects.items.inventory.EquipmentInventory;
import physics.Handler;

public class EquipmentInventoryRenderer extends UI
{
	private EquipmentInventory inventory;
	private List<Vector2i> cellsPositions;
	private HashMap<Vector4i, Class<? extends Equipment>> selectionPos;
	private Vector4i selectedCell;
	private int imageWidth = 64, imageHeight = 64;

	public EquipmentInventoryRenderer(EquipmentInventory inventory)
	{
		this.inventory = inventory;
		cellsPositions = new ArrayList<>();
		// helm 1
		cellsPositions.add(new Vector2i(184, 16));
		// chestplate 2
		cellsPositions.add(new Vector2i(184, 116));
		// belt 3
		cellsPositions.add(new Vector2i(184, 209));
		// greaves 4
		cellsPositions.add(new Vector2i(184, 290));
		// boot1 5
		cellsPositions.add(new Vector2i(141, 373));
		// boot2 6
		cellsPositions.add(new Vector2i(228, 373));
		// talisman1 7
		cellsPositions.add(new Vector2i(52, 27));
		// talisman2 8
		cellsPositions.add(new Vector2i(316, 27));
		// shoulderpad1 9
		cellsPositions.add(new Vector2i(92, 101));
		// shoulderpad2 10
		cellsPositions.add(new Vector2i(276, 101));
		// armband1 11
		cellsPositions.add(new Vector2i(92, 182));
		// armband2 12
		cellsPositions.add(new Vector2i(276, 182));
		// gauntlets1 13
		cellsPositions.add(new Vector2i(100, 263));
		// gauntlets2 14
		cellsPositions.add(new Vector2i(268, 263));
		// weapon1 15
		cellsPositions.add(new Vector2i(19, 344));
		// weapon2 16
		cellsPositions.add(new Vector2i(349, 344));
		// parchment 17
		cellsPositions.add(new Vector2i(21, 167));
		// mantle 18
		cellsPositions.add(new Vector2i(354, 168));

		selectedCell = null;

		selectionPos = new HashMap<>();
		selectionPos.put(null, Helm.class);
		selectionPos.put(null, Chestplate.class);
		selectionPos.put(null, Belt.class);
		selectionPos.put(null, Greaves.class);
		selectionPos.put(new Vector4i(138, 370, 70, 70), Boot.class);
		selectionPos.put(new Vector4i(225, 370, 70, 70), Boot.class);
		selectionPos.put(new Vector4i(51, 26, 66, 66), Talisman.class);
		selectionPos.put(new Vector4i(315, 26, 66, 66), Talisman.class);
		selectionPos.put(new Vector4i(91, 100, 66, 66), ShoulderPad.class);
		selectionPos.put(new Vector4i(275, 100, 66, 66), ShoulderPad.class);
		selectionPos.put(new Vector4i(91, 174, 66, 80), Armband.class);
		selectionPos.put(new Vector4i(275, 174, 66, 80), Armband.class);
		selectionPos.put(new Vector4i(99, 262, 66, 66), Gauntlet.class);
		selectionPos.put(new Vector4i(267, 262, 66, 66), Gauntlet.class);
		selectionPos.put(new Vector4i(11, 312, 80, 128), Weapon.class);
		selectionPos.put(new Vector4i(341, 312, 80, 128), Weapon.class);
		selectionPos.put(null, Parchment.class);
		selectionPos.put(null, Mantle.class);

		active = true;
		active = true;
		skin = Assets.getTexture("equipmentInv");
		width = 432;
		height = 464;
		x = (Engine.getWidth() / 2) - width - 30;
		y = (Engine.getHeight() / 2) - (height / 2);
		priority = 80;
	}

	@Override
	public void update(double delta)
	{
		// same key as Player's Inventory
		if(Engine.inputs.isKeyPressed(Keyboard.I) && Handler.getObjectManager().getEntityManager().getPlayer().getInventoryRenderer().canOpenInventory())
		{
			opened = !opened;
			Core.paused = opened;
		}
		if(!opened)
		{
			return;
		}
		// selection
		if(Engine.inputs.isButtonPressed(Mouse.LEFT))
		{
			for(Vector4i v : selectionPos.keySet())
			{
				if(v == null)
				{
					continue;
				}
				if((Engine.inputs.getX() > (x + v.x)) && (Engine.inputs.getX() <= (x + v.x + v.z)))
				{
					if((Engine.inputs.getY() > (y + v.y)) && (Engine.inputs.getY() <= (y + v.y + v.w)))
					{
						selectedCell = v;
						break;
					}
					else
					{
						selectedCell = null;
					}
				}
				else
				{
					selectedCell = null;
				}
			}
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
		// draw selection image in foreground of Equipment skin;
		if(selectedCell != null)
		{
			graphics.drawImage(Assets.getTexture("sel" + selectedCell.z + "x" + selectedCell.w).getCurrentSkin(), x + selectedCell.x, y + selectedCell.y, selectedCell.z, selectedCell.w, null);
		}
	}

	public HashMap<Vector4i, Class<? extends Equipment>> getSelectionPos()
	{
		return selectionPos;
	}

	public List<Vector2i> getCellsPositions()
	{
		return cellsPositions;
	}

	public EquipmentInventoryRenderer setSelectedCell(Vector4i selectedCell)
	{
		this.selectedCell = selectedCell;
		return this;
	}

	public Vector4i getSelectedCell()
	{
		return selectedCell;
	}

	public int getIndex(Vector2i v)
	{
		for(int i = 0; i < cellsPositions.size(); i++)
		{
			if(cellsPositions.get(i).equals(v))
			{
				return i;
			}
		}
		return -1;
	}
}
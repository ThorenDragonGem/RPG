package uis;

import java.awt.Color;
import java.awt.Graphics;

import assets.Assets;
import engine.Engine;
import inputs.Keyboard;
import inputs.Mouse;
import objects.entities.containers.ItemContainer;
import objects.items.Item;
import physics.Handler;
import tiles.Tile;
import utils.Text;

public class ItemContainerRenderer extends UI
{
	private ItemContainer container;
	private int invListCenterX, invListCenterY, invListSpacing, invImageX, invImageY, invImageWidth, invImageHeight, invCountX, invCountY;
	private int transferLeftX, transferLeftY, transferLeftWidth, transferLeftHeight;
	private int selectedCell = 0;

	public ItemContainerRenderer(ItemContainer container)
	{
		this.container = container;
		active = true;
		opened = false;
		skin = Assets.textures.get("container");

		width = skin.getCurrentSkin().getWidth();
		height = skin.getCurrentSkin().getHeight();
		x = (int)(Engine.getWidth() - (width * 1.25)) + 30;
		y = (Engine.getHeight() / 2) - (height / 2);
		invListCenterX = x + (width - 171);
		invListCenterY = y + (height / 2) + 5;
		invListSpacing = 30;
		invImageWidth = 64;
		invImageHeight = 64;
		invImageX = (x + width) - 388 - invImageWidth;
		invImageY = y + (invImageHeight / 2);
		invCountX = (x + width) - 421;
		invCountY = y + 124;
		transferLeftWidth = 23;
		transferLeftHeight = 61;
		transferLeftX = x + 81;
		transferLeftY = y + 201;
	}

	@Override
	public void update(double delta)
	{
		skin.update(delta);
		if(container.playerAround() && ((((int)(Engine.inputs.getX() + Handler.getCamera().getOffset().x) / Tile.TILEWIDTH) == (container.getX() / Tile.TILEWIDTH)) && (((int)(Engine.inputs.getY() + Handler.getCamera().getOffset().y) / Tile.TILEHEIGHT) == (container.getY() / Tile.TILEHEIGHT))) && Engine.inputs.isButtonPressed(Mouse.ONE))
		{
			opened = true;
		}
		if(Engine.inputs.isKeyPressed(Keyboard.E) || !container.playerAround())
		{
			opened = false;
		}

		if(!opened)
		{
			return;
		}

		if(!container.getInventory().getCells().isEmpty())
		{
			container.getInventory().getCells().get(selectedCell).getType().getSkin().update(delta);
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

		int len = container.getInventory().getCells().size();

		if(len == 0)
		{
			return;
		}
		for(int i = -5; i < 6; i++)
		{
			if(((selectedCell + i) < 0) || ((selectedCell + i) >= len))
			{
				continue;
			}
			if(i == 0)
			{
				Text.drawString(graphics, "> " + container.getInventory().getCells().get(selectedCell + i).getType().getName() + " <", invListCenterX, invListCenterY + (i * invListSpacing), true, Color.yellow, Assets.fonts.get("font").deriveFont(28f));
			}
			else
			{
				Text.drawString(graphics, container.getInventory().getCells().get(selectedCell + i).getType().getName(), invListCenterX, invListCenterY + (i * invListSpacing), true, Color.white, Assets.fonts.get("font").deriveFont(28f));
			}
		}
		Item item = container.getInventory().getCells().get(selectedCell).getType();
		graphics.drawImage(item.getSkin().getCurrentSkin(), invImageX, invImageY, invImageWidth, invImageHeight, null);
		Text.drawString(graphics, "" + container.getInventory().getCells().get(selectedCell).getSize(), invCountX, invCountY, true, Color.white, Assets.fonts.get("font").deriveFont(18f));
		graphics.drawImage(Assets.textures.get("inventoryLeft").getCurrentSkin(), transferLeftX, transferLeftY, transferLeftWidth, transferLeftHeight, null);
	}

	public int getTransferLeftX()
	{
		return transferLeftX;
	}

	public int getTransferLeftY()
	{
		return transferLeftY;
	}

	public int getTransferLeftWidth()
	{
		return transferLeftWidth;
	}

	public int getTransferLeftHeight()
	{
		return transferLeftHeight;
	}

	public int getSelectedCell()
	{
		return selectedCell;
	}

	public ItemContainerRenderer setSelectedCell(int selectedCell)
	{
		this.selectedCell = selectedCell;
		return this;
	}

	public ItemContainerRenderer addSelectedCell(int amount)
	{
		selectedCell += amount;
		return this;
	}
}

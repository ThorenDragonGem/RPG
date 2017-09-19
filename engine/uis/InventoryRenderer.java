package uis;

import java.awt.Color;
import java.awt.Graphics;

import assets.Assets;
import engine.Engine;
import gfx.Colors;
import inputs.Keyboard;
import inputs.Mouse;
import objects.GameObject;
import objects.entities.containers.ItemContainer;
import objects.items.Item;
import objects.items.equipments.Equipment;
import objects.items.inventory.Inventory;
import physics.Handler;
import utils.Text;

public class InventoryRenderer extends UI
{
	private Inventory inventory;
	private ItemContainer openedContainer;
	private boolean canOpenInventory;
	private int invListCenterX, invListCenterY, invListSpacing, invImageX, invImageY, invImageWidth, invImageHeight, invCountX, invCountY;
	private int transferRightX, transferRightY, transferRightWidth, transferRigthHeight;

	private int selectedCell = 0;

	public InventoryRenderer(Inventory inventory)
	{
		this.inventory = inventory;
		openedContainer = null;

		skin = Assets.textures.get("inventory");
		active = true;
		opened = false;
		canOpenInventory = true;

		width = skin.getCurrentSkin().getWidth();
		height = skin.getCurrentSkin().getHeight();
		x = (Engine.getWidth() / 2) - (width / 2);
		y = (Engine.getHeight() / 2) - (height / 2);
		rebuild();
	}

	@Override
	public void update(double delta)
	{
		{
			GameObject container = null;
			if(((container = Handler.getWorld().getPickedObject()) instanceof ItemContainer))
			{
				if(((ItemContainer)container).playerAround() && Engine.inputs.isButtonPressed(Mouse.ONE))
				{
					openedContainer = (ItemContainer)container;
					canOpenInventory = false;
				}
			}
		}

		if(openedContainer != null)
		{
			if(!openedContainer.isActive())
			{
				openedContainer = null;
				canOpenInventory = true;
				((InventoryRenderer)setX((Engine.getWidth() / 2) - (width / 2))).rebuild();
				opened = false;
			}
			if(!openedContainer.playerAround())
			{
				openedContainer = null;
				canOpenInventory = true;
				((InventoryRenderer)setX((Engine.getWidth() / 2) - (width / 2))).rebuild();
				opened = false;
			}
		}

		if(Engine.inputs.isKeyPressed(Keyboard.E))
		{
			openedContainer = null;
			canOpenInventory = true;
			((InventoryRenderer)setX((Engine.getWidth() / 2) - (width / 2))).rebuild();
			opened = false;
		}

		if(openedContainer != null)
		{
			((InventoryRenderer)setX((Engine.getWidth() / 2) - 30 - width)).rebuild();
			opened = true;
			if((Engine.inputs.getX() >= (openedContainer.getRenderer().getX() - 30)))
			{
				Handler.getObjectManager().getEntityManager().getPlayer().setRenderingInventory(openedContainer.getInventory());
			}
			else
			{
				Handler.getObjectManager().getEntityManager().getPlayer().setRenderingInventory(inventory);
			}
		}

		if(Engine.inputs.isKeyPressed(Keyboard.I) && canOpenInventory)
		{
			opened = !opened;
		}

		// TODO don't want to pause when opening inventory!
		// if(opened)
		// {
		// Engine.pause();
		// }
		// else
		// {
		// Engine.resume();
		// }
		if(!opened)
		{
			return;
		}

		skin.update(delta);
		if(!inventory.getCells().isEmpty())
		{
			inventory.getCells().get(selectedCell).getType().getSkin().update(delta);
		}

		if(openedContainer != null)
		{
			if(Handler.getObjectManager().getEntityManager().getPlayer().getRenderingInventory() == inventory)
			{
				if(selectedCell < inventory.getCells().size())
				{
					// right transfer
					if(openedContainer != null)
					{
						if((Engine.inputs.getX() >= transferRightX) && (Engine.inputs.getX() <= (transferRightX + transferRightWidth)))
						{
							if((Engine.inputs.getY() >= transferRightY) && (Engine.inputs.getY() <= (transferRightY + transferRigthHeight)))
							{
								if(Engine.inputs.isButtonPressed(Mouse.ONE))
								{
									int r = 1;
									if(Engine.inputs.isKeyDown(Keyboard.SHIFT))
									{
										r = inventory.getCells().get(selectedCell).getSize();
									}
									for(int i = 0; i < r; i++)
									{
										if(openedContainer.getInventory().addItem(inventory.getCells().get(selectedCell).getType()))
										{
											inventory.remove(inventory.getCells().get(selectedCell));
										}
									}
								}
							}
						}
					}
					// TODO WIP - Drops in world and modify collisions boxes
					// drops
					// if(Engine.inputs.isKeyDown(Keyboard.ENTER))
					// {
					// Handler.getObjectManager().add(inventory.remove(inventory.getCells().get(selectedCell)).createNew((inventory.getHolder().getX()
					// / Tile.TILEWIDTH) +
					// (int)Mathf.random(-inventory.getHolder().getWidth() * 2,
					// inventory.getHolder().getWidth() * 2),
					// (inventory.getHolder().getY() / Tile.TILEHEIGHT) +
					// (int)Mathf.random(-inventory.getHolder().getHeight() * 2,
					// inventory.getHolder().getHeight() * 2)));
					// if((inventory.getCells().size() > selectedCell) &&
					// (inventory.getCells().get(selectedCell).getSize() == 0))
					// {
					// inventory.getCells().remove(selectedCell);
					// }
					// }
				}
				if((Engine.inputs.getScrollY() < 0) || Engine.inputs.isKeyPressed(Keyboard.UP))
				{
					selectedCell--;
				}
				if((Engine.inputs.getScrollY() > 0) || Engine.inputs.isKeyPressed(Keyboard.DOWN))
				{
					selectedCell++;
				}
			}
			else if(Handler.getObjectManager().getEntityManager().getPlayer().getRenderingInventory() == openedContainer.getInventory())
			{
				if(selectedCell < openedContainer.getInventory().getCells().size())
				{
					// left transfer
					if((Engine.inputs.getX() >= openedContainer.getRenderer().getTransferLeftX()) && (Engine.inputs.getX() <= (openedContainer.getRenderer().getTransferLeftX() + openedContainer.getRenderer().getTransferLeftWidth())))
					{
						if((Engine.inputs.getY() >= openedContainer.getRenderer().getTransferLeftY()) && (Engine.inputs.getY() <= (openedContainer.getRenderer().getTransferLeftY() + openedContainer.getRenderer().getTransferLeftHeight())))
						{
							if(Engine.inputs.isButtonPressed(Mouse.ONE))
							{
								int r = 1;
								if(Engine.inputs.isKeyDown(Keyboard.SHIFT))
								{
									r = openedContainer.getInventory().getCells().get(selectedCell).getSize();
								}
								for(int i = 0; i < r; i++)
								{
									if(Handler.getObjectManager().getEntityManager().getPlayer().getInventory().addItem(openedContainer.getInventory().getCells().get(selectedCell).getType()))
									{
										openedContainer.getInventory().remove(openedContainer.getInventory().getCells().get(selectedCell));
									}
								}
							}
						}
					}
					// container drops
					// if(Engine.inputs.isKeyDown(Keyboard.ENTER))
					// {
					// Handler.getObjectManager().add(openedContainer.getInventory().remove(openedContainer.getInventory().getCells().get(selectedCell)).createNew((openedContainer.getX()
					// / Tile.TILEWIDTH) + 2, (openedContainer.getY() /
					// Tile.TILEHEIGHT) + 2));
					// if((openedContainer.getInventory().getCells().size() >
					// selectedCell) &&
					// (openedContainer.getInventory().getCells().get(selectedCell).getSize()
					// == 0))
					// {
					// openedContainer.getInventory().getCells().remove(selectedCell);
					// }
					// }
				}
				if((Engine.inputs.getScrollY() < 0) || Engine.inputs.isKeyPressed(Keyboard.UP))
				{
					openedContainer.getRenderer().addSelectedCell(-1);
				}
				if((Engine.inputs.getScrollY() > 0) || Engine.inputs.isKeyPressed(Keyboard.DOWN))
				{
					openedContainer.getRenderer().addSelectedCell(1);
				}

				if(openedContainer.getRenderer().getSelectedCell() < 0)
				{
					openedContainer.getRenderer().setSelectedCell(openedContainer.getInventory().getCells().size() - 1);
				}
				else if(openedContainer.getRenderer().getSelectedCell() >= openedContainer.getInventory().getCells().size())
				{
					openedContainer.getRenderer().setSelectedCell(0);
				}

			}
		}
		else
		{
			if(selectedCell < inventory.getCells().size())
			{
				// drops
				// if(Engine.inputs.isKeyDown(Keyboard.ENTER))
				// {
				// Handler.getObjectManager().add(inventory.remove(inventory.getCells().get(selectedCell)).createNew((inventory.getHolder().getX()
				// / Tile.TILEWIDTH) +
				// (int)Mathf.random(-inventory.getHolder().getWidth() * 2,
				// inventory.getHolder().getWidth() * 2),
				// (inventory.getHolder().getY() / Tile.TILEHEIGHT) +
				// (int)Mathf.random(-inventory.getHolder().getHeight() * 2,
				// inventory.getHolder().getHeight() * 2)));
				// if((inventory.getCells().size() > selectedCell) &&
				// (inventory.getCells().get(selectedCell).getSize() == 0))
				// {
				// inventory.getCells().remove(selectedCell);
				// }
				// }
			}
			if((Engine.inputs.getScrollY() < 0) || Engine.inputs.isKeyPressed(Keyboard.UP))
			{
				selectedCell--;
			}
			if((Engine.inputs.getScrollY() > 0) || Engine.inputs.isKeyPressed(Keyboard.DOWN))
			{
				selectedCell++;
			}
		}

		if(selectedCell < 0)

		{
			selectedCell = inventory.getCells().size() - 1;
		}
		else if(selectedCell >= inventory.getCells().size())
		{
			selectedCell = 0;
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

		int len = inventory.getCells().size();

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
				Text.drawString(graphics, "> " + (inventory.getCells().get(selectedCell + i).getType() instanceof Equipment ? " [E] " : "") + inventory.getCells().get(selectedCell + i).getType().getName() + " <", invListCenterX, invListCenterY + (i * invListSpacing), true, Color.yellow, Assets.fonts.get("font").deriveFont(28f));
			}
			else
			{
				Text.drawString(graphics, (inventory.getCells().get(selectedCell + i).getType() instanceof Equipment ? " [E] " : "") + inventory.getCells().get(selectedCell + i).getType().getName(), invListCenterX, invListCenterY + (i * invListSpacing), true, Color.white, Assets.fonts.get("font").deriveFont(28f));
			}
		}
		Item item = inventory.getCells().get(selectedCell).getType();
		graphics.drawImage(item.getSkin().getCurrentSkin(), invImageX, invImageY, invImageWidth, invImageHeight, null);
		if(inventory.getCells().get(selectedCell).getType() instanceof Equipment)
		{
			// equipment tag
			Text.drawString(graphics, "[E]", invImageX - 7, invImageY + 14, false, Color.white, Assets.fonts.get("font").deriveFont(18f));
			// rarity tag
			Text.drawString(graphics, "*", (invImageX + invImageWidth) - 18, invImageY + invImageHeight, false, Colors.gold1, Assets.fonts.get("font").deriveFont(18f));
		}
		Text.drawString(graphics, "" + inventory.getCells().get(selectedCell).getSize(), invCountX, invCountY, true, Color.white, Assets.fonts.get("font").deriveFont(18f));
		if(openedContainer != null)
		{
			graphics.drawImage(Assets.textures.get("inventoryRight").getCurrentSkin(), transferRightX, transferRightY, transferRightWidth, transferRigthHeight, null);
		}
	}

	public UI rebuild()
	{
		invListCenterX = x + 171;
		invListCenterY = y + (height / 2) + 5;
		invListSpacing = 30;
		invImageX = x + 392;
		invImageWidth = 64;
		invImageHeight = 64;
		invImageY = y + (invImageHeight / 2);
		invCountX = x + 421;
		invCountY = y + 124;
		transferRightWidth = 23;
		transferRigthHeight = 61;
		transferRightX = (x + width) - transferRightWidth - 81;
		transferRightY = y + 201;
		return this;
	}

	public InventoryRenderer setOpenedContainer(ItemContainer openedContainer)
	{
		this.openedContainer = openedContainer;
		return this;
	}

	public ItemContainer getOpenedContainer()
	{
		return openedContainer;
	}

	public boolean canOpenInventory()
	{
		return canOpenInventory;
	}

	public InventoryRenderer setCanOpenInventory(boolean canOpenInventory)
	{
		this.canOpenInventory = canOpenInventory;
		return this;
	}
}

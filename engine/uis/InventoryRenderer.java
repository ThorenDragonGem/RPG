package uis;

import java.awt.Color;
import java.awt.Graphics;

import org.joml.Vector2i;
import org.joml.Vector4i;

import assets.Assets;
import core.Core;
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
import utils.TextUtils;

public class InventoryRenderer extends UI
{
	private Inventory inventory;
	private ItemContainer openedContainer;
	private boolean canOpenInventory;
	private int invListCenterX, invListCenterY, invListSpacing, invImageX, invImageY, invImageWidth, invImageHeight, invCountX, invCountY;
	private int transferRightX, transferRightY, transferRightWidth, transferRigthHeight;

	private int selectedCell = 0;
	private ItemUI itemUI;

	public InventoryRenderer(Inventory inventory)
	{
		this.inventory = inventory;
		openedContainer = null;

		skin = Assets.getTexture("inventory");
		active = true;
		opened = false;
		canOpenInventory = true;
		itemUI = new ItemUI(null);
		Handler.getUis().add(itemUI);

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
				if(((ItemContainer)container).playerAround() && Engine.inputs.isButtonPressed(Mouse.LEFT) && !Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().isOpened())
				{
					System.out.println(true);
					openedContainer = (ItemContainer)container;
					canOpenInventory = false;
				}
			}
		}

		// paused => can't move and not updated
		// if(openedContainer != null)
		// {
		// if(!openedContainer.isActive())
		// {
		// close();
		// }
		// if(!openedContainer.playerAround())
		// {
		// close();
		// }
		// }

		if(Engine.inputs.isKeyPressed(Keyboard.E) && !Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().isOpened())
		{
			close();
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
			if(opened)
			{
				x = (Engine.getWidth() / 2) + 30;
				rebuild();
			}
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
			close();
			return;
		}
		Core.paused = true;

		if(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().isOpened())
		{
			updateEquipmentInventory();
			return;
		}

		skin.update(delta);
		if(!inventory.getCells().isEmpty())
		{
			inventory.getCells().get(selectedCell).getType().getSkin().update(delta);
		}

		int r = 1;
		if(openedContainer != null)
		{
			// transfer can be done independently of Mouse position

			if(selectedCell < inventory.getCells().size())
			{
				// right transfer
				if(openedContainer != null)
				{
					if((Engine.inputs.getX() >= transferRightX) && (Engine.inputs.getX() <= (transferRightX + transferRightWidth)))
					{
						if((Engine.inputs.getY() >= transferRightY) && (Engine.inputs.getY() <= (transferRightY + transferRigthHeight)))
						{
							if(Engine.inputs.isButtonPressed(Mouse.LEFT))
							{
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
					if(Engine.inputs.isKeyPressed(Keyboard.RIGHT))
					{
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
			if(openedContainer.getRenderer().getSelectedCell() < openedContainer.getInventory().getCells().size())
			{
				// left transfer
				if((Engine.inputs.getX() >= openedContainer.getRenderer().getTransferLeftX()) && (Engine.inputs.getX() <= (openedContainer.getRenderer().getTransferLeftX() + openedContainer.getRenderer().getTransferLeftWidth())))
				{
					if((Engine.inputs.getY() >= openedContainer.getRenderer().getTransferLeftY()) && (Engine.inputs.getY() <= (openedContainer.getRenderer().getTransferLeftY() + openedContainer.getRenderer().getTransferLeftHeight())))
					{
						if(Engine.inputs.isButtonPressed(Mouse.LEFT))
						{
							System.out.println(true);
							if(Engine.inputs.isKeyDown(Keyboard.SHIFT))
							{
								r = openedContainer.getInventory().getCells().get(openedContainer.getRenderer().getSelectedCell()).getSize();
							}
							for(int i = 0; i < r; i++)
							{
								if(Handler.getObjectManager().getEntityManager().getPlayer().getInventory().addItem(openedContainer.getInventory().getCells().get(openedContainer.getRenderer().getSelectedCell()).getType()))
								{
									openedContainer.getInventory().remove(openedContainer.getInventory().getCells().get(openedContainer.getRenderer().getSelectedCell()));
								}
							}
						}
					}
				}
				if(Engine.inputs.isKeyPressed(Keyboard.LEFT))
				{
					if(Engine.inputs.isKeyDown(Keyboard.SHIFT))
					{
						r = openedContainer.getInventory().getCells().get(openedContainer.getRenderer().getSelectedCell()).getSize();
					}
					for(int i = 0; i < r; i++)
					{
						if(Handler.getObjectManager().getEntityManager().getPlayer().getInventory().addItem(openedContainer.getInventory().getCells().get(openedContainer.getRenderer().getSelectedCell()).getType()))
						{
							openedContainer.getInventory().remove(openedContainer.getInventory().getCells().get(openedContainer.getRenderer().getSelectedCell()));
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
			if(Handler.getObjectManager().getEntityManager().getPlayer().getRenderingInventory() == inventory)
			{
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

	private void updateEquipmentInventory()
	{
		// TODO when cells (to select) are empty, fill the first 'left' (index
		// 0) by RightClick ; if the next one is empty, fill with a new
		// RightClick
		// TODO if you want to transfer 'select' Equipment but you have not
		// selected a cell, fill automatically the first left (index 0)

		// transfer to inventory
		for(Vector2i v : Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getCellsPositions())
		{
			if(Engine.inputs.isButtonPressed(Mouse.RIGHT))
			{
				if((Engine.inputs.getX() > (Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getX() + v.x)) && (Engine.inputs.getX() <= (Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getX() + v.x + 64)))
				{
					if((Engine.inputs.getY() > (Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getY() + v.y)) && (Engine.inputs.getY() <= (Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getY() + v.y + 64)))
					{
						int index = Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getIndex(v);
						if(index == -1)
						{
							break;
						}
						if(inventory.addItem(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentInventory().get(index).getEquipment()))
						{
							Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentInventory().get(index).set(null);
						}
					}
				}
			}
		}

		Item item = inventory.getCells().get(selectedCell).getType();
		if(item instanceof Equipment)
		{
			Vector4i selCell = Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getSelectedCell();
			// TODO cool idea with ButtonDown but need to add a CoolDown on it
			if((Engine.inputs.getX() > ((Engine.getWidth() / 2) + 30)) && Engine.inputs.isButtonDown(Mouse.RIGHT))
			{
				if(selCell == null)
				{
					if(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentIndexes((Equipment)item).size() == 1)
					{
						Equipment last = Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentInventory().get(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentIndexes((Equipment)item).get(0)).getEquipment();
						// if inventory full can swap equipments
						// do not if(addItem())!
						inventory.addItem(last);
						inventory.remove(inventory.getCells().get(selectedCell));
						Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().setEquipment((Equipment)item, 0);
					}
				}
				else
				{
					if(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentIndexes((Equipment)item).size() == 1)
					{
						return;
					}
					if(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getSuperClasses((Equipment)item).equals(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getSelectionPos().get(selCell)))
					{
						Equipment last = selCell.x > (Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getWidth() / 2) ? Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentInventory().get(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentIndexes((Equipment)item).get(1)).getEquipment() : Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentInventory().get(Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().getEquipmentIndexes((Equipment)item).get(0)).getEquipment();
						inventory.addItem(last);
						inventory.remove(inventory.getCells().get(selectedCell));
						Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventory().setEquipment((Equipment)item, selCell.x > (Handler.getObjectManager().getEntityManager().getPlayer().getEquipmentInventoryRenderer().getWidth() / 2) ? 1 : 0);
					}
				}
			}
		}

		if((Engine.inputs.getScrollY() < 0) || Engine.inputs.isKeyPressed(Keyboard.UP))
		{
			selectedCell--;
		}
		if((Engine.inputs.getScrollY() > 0) || Engine.inputs.isKeyPressed(Keyboard.DOWN))
		{
			selectedCell++;
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
				TextUtils.drawString(graphics, "> " + (inventory.getCells().get(selectedCell + i).getType() instanceof Equipment ? "[E] " : "") + TextUtils.getTruncatedString(inventory.getCells().get(selectedCell + i).getType().getName(), 8) + " <", invListCenterX, invListCenterY + (i * invListSpacing), true, Color.yellow, Assets.getFont("courbd").deriveFont(28f));
			}
			else
			{
				TextUtils.drawString(graphics, (inventory.getCells().get(selectedCell + i).getType() instanceof Equipment ? "[E] " : "") + TextUtils.getTruncatedString(inventory.getCells().get(selectedCell + i).getType().getName(), 12), invListCenterX, invListCenterY + (i * invListSpacing), true, Color.white, Assets.getFont("courbd").deriveFont(28f));
			}
		}
		Item item = inventory.getCells().get(selectedCell).getType();
		graphics.drawImage(item.getSkin().getCurrentSkin(), invImageX, invImageY, invImageWidth, invImageHeight, null);
		if(inventory.getCells().get(selectedCell).getType() instanceof Equipment)
		{
			// equipment tag
			TextUtils.drawString(graphics, "[E]", invImageX - 7, invImageY + 14, false, Color.white, Assets.getFont("courbd").deriveFont(18f));
			// rarity tag
			TextUtils.drawString(graphics, "*", (invImageX + invImageWidth) - 18, invImageY + invImageHeight, false, Colors.gold1, Assets.getFont("courbd").deriveFont(18f));
		}
		TextUtils.drawString(graphics, "" + inventory.getCells().get(selectedCell).getSize(), invCountX, invCountY, true, Color.white, Assets.getFont("courbd").deriveFont(18f));
		if(openedContainer != null)
		{
			graphics.drawImage(Assets.getTexture("inventoryRight").getCurrentSkin(), transferRightX, transferRightY, transferRightWidth, transferRigthHeight, null);
		}
		if((Engine.inputs.getX() > invImageX) && (Engine.inputs.getX() <= (invImageX + invImageWidth)))
		{
			if((Engine.inputs.getY() > invImageY) && (Engine.inputs.getY() <= (invImageY + invImageHeight)))
			{
				itemUI.setItem(inventory.getCells().get(selectedCell).getType());
				itemUI.setOpened(true);
			}
		}
		else
		{
			itemUI.setOpened(false);
		}
	}

	public void close()
	{
		openedContainer = null;
		canOpenInventory = true;
		((InventoryRenderer)setX((Engine.getWidth() / 2) - (width / 2))).rebuild();
		opened = false;
		itemUI.setOpened(false);
		Core.paused = false;
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

	public ItemUI getItemUI()
	{
		return itemUI;
	}

	public int getSelectedCell()
	{
		return selectedCell;
	}
}

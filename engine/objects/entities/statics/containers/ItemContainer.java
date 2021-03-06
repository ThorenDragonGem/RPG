package objects.entities.statics.containers;

import java.awt.Color;
import java.awt.Graphics;

import assets.Assets;
import gfx.Colors;
import gfx.Skin2D;
import objects.GameObject;
import objects.entities.statics.StaticEntity;
import objects.items.Item;
import objects.items.inventory.Inventory;
import physics.Handler;
import tiles.Tile;
import uis.ItemContainerRenderer;

public class ItemContainer extends StaticEntity
{
	protected Inventory inventory;
	protected ItemContainerRenderer renderer;
	protected Inventory renderingInventory;
	protected int maxTime;
	protected int time;

	public ItemContainer(String name, Skin2D skin, int width, int height, int slots)
	{
		super(name, skin, width, height);
		inventory = new Inventory(this, slots);
		renderer = new ItemContainerRenderer(this);
		inventory.addItem(new Item("rock", Assets.getTexture("rock"), 1, 1));
		inventory.addItem(new Item("stick", Assets.getTexture("stick"), 1, 1));
		Handler.getUis().add(renderer);
		maxTime = -1;// 60 * 100;
		time = maxTime;
	}

	public boolean playerAround()
	{
		if(((Handler.getObjectManager().getEntityManager().getPlayer().getX() / Tile.TILEWIDTH) > ((x / Tile.TILEWIDTH) - 3)) && ((Handler.getObjectManager().getEntityManager().getPlayer().getX() / Tile.TILEWIDTH) < ((x / Tile.TILEWIDTH) + 3)))
		{
			if(((Handler.getObjectManager().getEntityManager().getPlayer().getY() / Tile.TILEHEIGHT) > ((y / Tile.TILEHEIGHT) - 3)) && ((Handler.getObjectManager().getEntityManager().getPlayer().getY() / Tile.TILEHEIGHT) < ((y / Tile.TILEHEIGHT) + 3)))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(double delta)
	{
		super.update(delta);
		inventory.update(delta);
		if(maxTime != -1)
		{
			time--;
			if(time <= 0)
			{
				active = false;
				Handler.getUis().remove(renderer);
			}
		}
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
		graphics.setColor(new Color(Colors.interpolate((float)time / (float)maxTime, Colors.red1.getRGB(), Colors.yellow1.getRGB(), Colors.green1.getRGB())));
		graphics.fillRect((int)(x - Handler.getCamera().getOffset().x), (int)((y + ((height * Tile.TILEHEIGHT) - 3)) - Handler.getCamera().getOffset().y), (int)(((double)time / (double)maxTime) * width * Tile.TILEWIDTH), 3);
	}

	public ItemContainer setInventory(Inventory inventory)
	{
		this.inventory = inventory;
		return this;
	}

	public Inventory getInventory()
	{
		return inventory;
	}

	public ItemContainerRenderer getRenderer()
	{
		return renderer;
	}

	public int getTime()
	{
		return time;
	}

	@Override
	public GameObject createNew(int x, int y, boolean virgin)
	{
		if(Handler.getUis().contains(renderer))
		{
			if(Handler.getUis().contains(renderer.getItemUI()))
			{
				Handler.getUis().remove(renderer.getItemUI());
			}
			Handler.getUis().remove(renderer);
		}
		if(virgin)
		{
			return new ItemContainer(name, skin, width, height, inventory.getSlots()).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT).setSolid(solid).setPriority(priority);
		}
		else
		{
			return ((ItemContainer)new ItemContainer(name, skin, width, height, inventory.getSlots()).setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT)).setInventory(inventory).setSolid(solid).setPriority(priority);
		}
	}
}

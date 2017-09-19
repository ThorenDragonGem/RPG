package objects.entities;

import java.awt.Graphics;

import assets.Assets;
import engine.Engine;
import gfx.Animation;
import inputs.Keyboard;
import inputs.Mouse;
import objects.GameObject;
import objects.items.Item;
import objects.items.inventory.Inventory;
import physics.Handler;
import tiles.Tile;
import uis.InventoryRenderer;

public class Player extends Entity
{
	private Inventory inventory;
	private Inventory renderingInventory;
	private InventoryRenderer renderer;

	public Player()
	{
		super("player", new Animation(150, Assets.getArray("idle", 0, 2)), 1, 1);
		inventory = new Inventory(this, 10);
		renderer = new InventoryRenderer(inventory);
		priority = 10;
		Handler.getUis().add(renderer);
		renderingInventory = null;
		for(int i = 0; i < 100; i++)
		{
			inventory.addItem(new Item("rock", Assets.textures.get("rock"), 1, 1));
		}
	}

	@Override
	public void update(double delta)
	{
		super.update(delta);
		boolean mouseMove = false;
		boolean keyMove = false;
		renderingInventory = null;
		inventory.update(delta);
		int mouseTileX = 0, mouseTileY = 0;

		if(Engine.inputs.isButtonDown(Mouse.THREE) && !keyMove)
		{
			mouseMove = true;
			mouseTileX = (int)((Engine.inputs.getX() + Handler.getCamera().getOffset().x) / Tile.TILEWIDTH);
			mouseTileY = (int)((Engine.inputs.getY() + Handler.getCamera().getOffset().y) / Tile.TILEHEIGHT);
			if(mouseTileX < 0)
			{
				mouseTileX = 0;
			}
			else if(mouseTileX > Handler.getWorld().getWidth())
			{
				mouseTileX = Handler.getWorld().getWidth() - 1;
			}
			if(mouseTileY < 0)
			{
				mouseTileY = 0;
			}
			else if(mouseTileY > Handler.getWorld().getHeight())
			{
				mouseTileY = Handler.getWorld().getHeight() - 1;
			}
			path = Handler.getWorld().getMap().findPath(x / Tile.TILEWIDTH, y / Tile.TILEHEIGHT, mouseTileX, mouseTileY);
			index = 0;
			move();
		}

		if(!mouseMove)
		{
			keyMove = true;
			int dx = 0;
			int dy = 0;
			if(Engine.inputs.isKeyDown(Keyboard.Z))
			{
				dy += -1;
			}
			if(Engine.inputs.isKeyDown(Keyboard.S))
			{
				dy += 1;
			}
			if(Engine.inputs.isKeyDown(Keyboard.Q))
			{
				dx += -1;
			}
			if(Engine.inputs.isKeyDown(Keyboard.D))
			{
				dx += 1;
			}
			move(dx, dy);
		}

	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
	}

	@Override
	public GameObject createNew(int x, int y)
	{
		if(Handler.getUis().contains(renderer))
		{
			Handler.getUis().remove(renderer);
		}
		return new Player().setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT);
	}

	public InventoryRenderer getInventoryRenderer()
	{
		return renderer;
	}

	public Player setRenderingInventory(Inventory renderingInventory)
	{
		this.renderingInventory = renderingInventory;
		return this;
	}

	public Inventory getRenderingInventory()
	{
		return renderingInventory;
	}

	public Inventory getInventory()
	{
		return inventory;
	}
}
package objects.entities.creatures;

import java.awt.Graphics;

import org.imgscalr.Scalr.Rotation;
import org.joml.Vector2i;

import assets.Assets;
import engine.Engine;
import gfx.Animation;
import inputs.Keyboard;
import inputs.Mouse;
import objects.GameObject;
import objects.entities.Entity;
import objects.items.Item;
import objects.items.equipments.*;
import objects.items.inventory.EquipmentInventory;
import objects.items.inventory.Inventory;
import physics.Handler;
import physics.Physics;
import tiles.Tile;
import times.CoolDown;
import uis.EquipmentInventoryRenderer;
import uis.InventoryRenderer;

public class Player extends Creature
{
	private Inventory inventory;
	private Inventory renderingInventory;
	private InventoryRenderer renderer;
	private EquipmentInventory equipmentInv;
	private EquipmentInventoryRenderer equipmentInventoryRenderer;
	private CoolDown attackCD;
	private Animation currentAttackAnim, attackAnimLeft, attackAnimRight, attackAnimUp, attackAnimDown;
	private boolean attacking;
	private Vector2i attackTile;

	public Player()
	{
		super("player", new Animation(150, Assets.getArray("idle", 0, 2), true), 1, 1);
		maxHealth = 10000;
		health = maxHealth;
		priority = 10;

		inventory = new Inventory(this, 10);
		for(int i = 0; i < 100; i++)
		{
			inventory.addItem(new Item("rock", Assets.getTexture("rock"), 1, 1));
		}
		inventory.addItem(new Boot("Diamond", Assets.getTexture("boot3"), 1, 1));
		inventory.addItem(new Boot("Gold", Assets.getTexture("boot4"), 1, 1));
		// inventory.addItem(new Weapon("Knife", Assets.getTexture("knife"), 1,
		// 1));
		// inventory.addItem(new Helm("Helm", Assets.getTexture("stick"), 1,
		// 1));
		// inventory.addItem(new Talisman("Talisman", Assets.getTexture("rock"),
		// 1, 1));
		renderer = new InventoryRenderer(inventory);

		equipmentInv = new EquipmentInventory(this, Helm.class, Chestplate.class, Belt.class, Greaves.class, Boot.class, Boot.class, Talisman.class, Talisman.class, ShoulderPad.class, ShoulderPad.class, Armband.class, Armband.class, Gauntlet.class, Gauntlet.class, Weapon.class, Weapon.class, Parchment.class, Mantle.class);
		equipmentInv.setEquipment(new Helm("Helm of Light", Assets.getTexture("helm"), 1, 1), 0);
		equipmentInv.setEquipment(new Chestplate("Dragon Chestplate of Fire", Assets.getTexture("chest"), 1, 1), 0);
		equipmentInv.setEquipment(new Belt("Belt", Assets.getTexture("belt"), 1, 1), 0);
		equipmentInv.setEquipment(new Greaves("Greaves", Assets.getTexture("greaves"), 1, 1), 0);
		equipmentInv.setEquipment(new Boot("Leather", Assets.getTexture("boot1"), 1, 1), 0);
		equipmentInv.setEquipment(new Boot("Iron", Assets.getTexture("boot2"), 1, 1), 1);
		equipmentInv.setEquipment(new Talisman("Clock of the Ligth", Assets.getTexture("talisman1"), 1, 1), 0);
		equipmentInv.setEquipment(new Talisman("Clock of the Shadow", Assets.getTexture("talisman2"), 1, 1), 1);
		equipmentInv.setEquipment(new ShoulderPad("ShoulderPad1", Assets.getTexture("shoulderpad"), 1, 1), 0);
		equipmentInv.setEquipment(new ShoulderPad("ShoulderPad2", Assets.getTexture("shoulderpad"), 1, 1), 1);
		equipmentInv.setEquipment(new Armband("Armband1", Assets.getTexture("armband"), 1, 1), 0);
		equipmentInv.setEquipment(new Armband("Armband1", Assets.getTexture("armband"), 1, 1), 1);
		equipmentInv.setEquipment(new Gauntlet("Gauntlet1", Assets.getTexture("gauntlet"), 1, 1), 0);
		equipmentInv.setEquipment(new Gauntlet("Gauntlet2", Assets.getTexture("gauntlet"), 1, 1), 1);
		equipmentInv.setEquipment(new Parchment("SCRIPT!", Assets.getTexture("script"), 0, 0), 0);
		equipmentInv.setEquipment(new Mantle("Mantle", Assets.getTexture("mantle"), 1, 1), 0);
		equipmentInv.setEquipment(new Weapon("legendary sword", Assets.getTexture("knife"), 1, 1), 0);
		equipmentInv.setEquipment(new Weapon("Shield of the Black Star", Assets.getTexture("shield"), 1, 1), 1);
		equipmentInventoryRenderer = new EquipmentInventoryRenderer(equipmentInv);

		Handler.getUis().add(renderer);
		Handler.getUis().add(equipmentInventoryRenderer);
		renderingInventory = null;

		attackCD = new CoolDown(60);
		attackCD.go();
		currentAttackAnim = null;
		attackAnimLeft = new Animation(250, Assets.getArrayRotated("slash", 0, 3, Rotation.CW_90), false);
		attackAnimRight = new Animation(250, Assets.getArrayRotated("slash", 0, 3, Rotation.CW_270), false);
		attackAnimUp = new Animation(250, Assets.getArray("slash", 0, 3), false);
		attackAnimDown = new Animation(250, Assets.getArrayRotated("slash", 0, 3, Rotation.CW_180), false);
	}

	int mouseTileX = 0, mouseTileY = 0;

	@Override
	public void update(double delta)
	{
		super.update(delta);
		boolean mouseMove = false;
		renderingInventory = null;
		inventory.update(delta);

		if(Engine.inputs.isButtonDown(Mouse.RIGHT))
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
		}
		move();

		if(!mouseMove)
		{
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
			if((dx != 0) || (dy != 0))
			{
				path.clear();
			}
			move(dx, dy);
		}

		checkAttack(delta);
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
	}

	@Override
	public void renderOver(Graphics graphics)
	{
		super.renderOver(graphics);
		if(attacking)
		{
			if(currentAttackAnim != null)
			{
				graphics.drawImage(currentAttackAnim.getCurrentSkin(), (int)(target.getX() - Handler.getCamera().getOffset().x), (int)(target.getY() - Handler.getCamera().getOffset().y), null);
			}
		}
	}

	private void checkAttack(double delta)
	{
		attackCD.update();
		if(Engine.inputs.isButtonDown(Mouse.LEFT))
		{
			if(attackCD.isOver())
			{
				attacking = true;
				attackNear();
			}
		}
		if(currentAttackAnim != null)
		{
			if(attacking)
			{
				currentAttackAnim.update(delta);
			}
			if(currentAttackAnim.isFinished())
			{
				attacking = false;
				currentAttackAnim.restart();
				currentAttackAnim = null;
			}
		}
	}

	private void attackNear()
	{
		// attack the next tiles: -1,-1 => 1,1 (but not 0,0)
		attackTile = Handler.getCamera().getPickRay();
		if((attackTile.x == (x / Tile.TILEWIDTH)) && (attackTile.y == (y / Tile.TILEHEIGHT)))
		{
			return;
		}
		for(Entity e : Handler.getObjectManager().getEntityManager().getEntities())
		{
			if(!(e instanceof Creature) || (e == this) || ((e.getX() / Tile.TILEWIDTH) < ((x / Tile.TILEWIDTH) - 1)) || ((e.getX() / Tile.TILEWIDTH) > ((x / Tile.TILEWIDTH) + 1)) || ((e.getY() / Tile.TILEHEIGHT) < ((y / Tile.TILEHEIGHT) - 1)) || ((e.getY() / Tile.TILEWIDTH) > ((y / Tile.TILEHEIGHT) + 1)))
			{
				continue;
			}
			if(((e.getX() / Tile.TILEWIDTH) == (attackTile.x / Tile.TILEWIDTH)) && ((e.getY() / Tile.TILEHEIGHT) == (attackTile.y / Tile.TILEHEIGHT)))
			{
				// TODO target's UI
				target = e;
				// animation switch
				if((target.getX() / Tile.TILEWIDTH) == ((x / Tile.TILEWIDTH) + 1))
				{
					currentAttackAnim = attackAnimLeft;
				}
				else if((target.getX() / Tile.TILEWIDTH) == ((x / Tile.TILEWIDTH) - 1))
				{
					currentAttackAnim = attackAnimRight;
				}
				else
				{
					if((target.getY() / Tile.TILEHEIGHT) == ((y / Tile.TILEHEIGHT) + 1))
					{
						currentAttackAnim = attackAnimDown;
					}
					else
					{
						currentAttackAnim = attackAnimUp;
					}
				}
				attack((Entity)target);
			}
		}
	}

	private void attackFar()
	{
		// attack method for spells and shot: no distance checks!!!
		// TODO improve: currently attacking any Creature even if the distance
		// is high
		Vector2i attackTile = Handler.getCamera().getPickRay();
		for(Entity e : Handler.getObjectManager().getEntityManager().getEntities())
		{
			if(!(e instanceof Creature) || (e == this))
			{
				continue;
			}
			if(((e.getX() / Tile.TILEWIDTH) == (attackTile.x / Tile.TILEWIDTH)) && ((e.getY() / Tile.TILEHEIGHT) == (attackTile.y / Tile.TILEHEIGHT)))
			{
				target = e;
				attack(e);
			}
		}
	}

	private void attack(Entity e)
	{
		((Creature)e).health -= 10;
		attackCD.restart();
	}

	@Deprecated
	private boolean mouseAroundPlayer()
	{
		// TODO
		double maxRadius = 5.5 * Tile.TILEWIDTH;
		// get tile pos
		// if <= dist
		if(Physics.length(x - Handler.getCamera().getOffset().x, y - Handler.getCamera().getOffset().y, Engine.inputs.getX() + Handler.getCamera().getOffset().x, Engine.inputs.getY() + Handler.getCamera().getOffset().y) <= maxRadius)
		{
			return true;
		}
		return false;
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

	public EquipmentInventory getEquipmentInventory()
	{
		return equipmentInv;
	}

	public Player setEquipmentInventoryRenderer(EquipmentInventoryRenderer equipmentInventoryRenderer)
	{
		this.equipmentInventoryRenderer = equipmentInventoryRenderer;
		return this;
	}

	public EquipmentInventoryRenderer getEquipmentInventoryRenderer()
	{
		return equipmentInventoryRenderer;
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
		if(Handler.getUis().contains(equipmentInventoryRenderer))
		{
			Handler.getUis().remove(equipmentInventoryRenderer);
		}
		return new Player().setPosition(x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT);
	}
}
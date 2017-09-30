package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import assets.Assets;
import attributes.Attribute;
import attributes.DependantAttribute;
import attributes.FinalBonus;
import engine.Engine;
import engine.Instance;
import gfx.TextFont;
import inputs.Keyboard;
import objects.ObjectManager;
import objects.blocks.Block;
import objects.entities.Entity;
import objects.entities.creatures.Aggressive;
import objects.entities.creatures.Player;
import objects.entities.statics.StaticEntity;
import objects.entities.statics.containers.ItemContainer;
import objects.items.Item;
import physics.Camera;
import physics.Handler;
import registry.GameRegistry;
import tiles.Tile;
import uis.DamageUI;
import utils.TextUtils;
import utils.Utils;
import worlds.World;

public class Core implements Instance
{
	public static boolean paused;
	BufferedImage image;
	TextFont font;
	DependantAttribute attribute;

	@Override
	public void init() throws Exception
	{
		new Handler();
		GameRegistry.registerTile("default_tile", new Tile(Assets.getTexture("grass")));
		GameRegistry.registerTile("rock_tile", new Tile(Assets.getTexture("rockTile")).setSolid(true));
		new Camera();
		new World(100, 100);
		new ObjectManager(new Player());

		GameRegistry.registerBlock(new Block("rock", Assets.getTexture("rock"), 1, 1));
		GameRegistry.registerEntity((Entity)new StaticEntity("pine", Assets.getTexture("pine"), 1, 2).setSolid(true).setBounds(new Rectangle(0, 1, 1, 1)));
		GameRegistry.registerEntity((Entity)new Aggressive("aggressive", Assets.getTexture("mouse"), 1, 1).setSolid(true));

		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("block_rock").createNew(3, 6, false));
		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("entity_pine").createNew(10, 5, false));

		// TODO caution when spawning entities
		// => world's tile solidity deactivated

		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("entity_aggressive").createNew(10, 9, false));
		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("entity_aggressive").createNew(10, 8, false));
		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("entity_aggressive").createNew(10, 7, false));
		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("entity_aggressive").createNew(10, 4, false));
		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("entity_aggressive").createNew(10, 3, false));
		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("entity_aggressive").createNew(10, 2, false));
		Handler.getObjectManager().add(GameRegistry.getObjectsRegister().get("entity_aggressive").createNew(10, 1, false));

		GameRegistry.registerEntity(new ItemContainer("itemContainer", Assets.getTexture("itemContainer"), 1, 1, 10));
		// cf characteristics of the createNew() method
		Handler.getObjectManager().add(((ItemContainer)GameRegistry.getObjectsRegister().get("entity_itemContainer").createNew(5, 5, true)).getInventory().modify(new Item("legendKnife", Assets.getTexture("knife"), 1, 1)));
		Handler.getObjectManager().add((GameRegistry.getObjectsRegister().get("entity_itemContainer").createNew(5, 6, true)));

		Handler.getUis().add(new DamageUI());
		image = Utils.buildImage(64, 64, BufferedImage.TYPE_INT_ARGB, Assets.getTexture("grass").getCurrentSkin(), Assets.getTexture("stick").getCurrentSkin());
		// BufferedImage fontImage =
		// DrawUtils.replace(Assets.getTexture("imageFont").getCurrentSkin(),
		// Color.black, new Color(0, 0, 0, 255));
		attribute = new DependantAttribute(100, 1);
		Attribute parent = new Attribute(100, 0);
		attribute.addAttribute("parent", parent);
		FinalBonus f = new FinalBonus(1000, 0, 60);
		parent.addFinalBonus(f);
		f.start(parent);
		System.out.println(attribute.getFinalValue());
	}

	@Override
	public void update(double delta)
	{
		if(Engine.inputs.isKeyDown(Keyboard.ESCAPE))
		{
			System.exit(0);
		}
		// if(Engine.inputs.isKeyPressed(Keyboard.P))
		// {
		// paused = !paused;
		// }
		Handler.update(delta);

		// for(UI ui : Handler.getUis())
		// {
		// System.out.println(ui);
		// }
		System.out.println(attribute.calculateValue());
		attribute.updateFinalBonus();
		System.out.println(attribute);
	}

	@Override
	public void render(Graphics graphics)
	{
		Handler.render(graphics);
		// graphics.drawImage(Assets.getTexture("imageFont").getCurrentSkin(),
		// 300, 300, null);
		// graphics.drawLine(0, Engine.getHeight() / 2, Engine.getWidth(),
		// Engine.getHeight() / 2);
		// graphics.drawLine(Engine.getWidth() / 2, 0, Engine.getWidth() / 2,
		// Engine.getHeight());

		if(paused)
		{
			TextUtils.drawString(graphics, "Paused", Engine.getWidth() / 2, 30, true, Color.red, Assets.getFont("courbd").deriveFont(38f));
		}
	}

}

package core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import assets.Assets;
import engine.Engine;
import engine.Instance;
import gfx.TextFont;
import inputs.Keyboard;
import objects.ObjectManager;
import objects.blocks.Block;
import objects.entities.Entity;
import objects.entities.Player;
import objects.entities.containers.ItemContainer;
import objects.items.equipments.Equipment;
import physics.Camera;
import physics.Handler;
import registry.GameRegistry;
import tiles.Tile;
import uis.DamageUI;
import utils.Utils;
import worlds.World;

public class Core implements Instance
{
	BufferedImage image;
	TextFont font;

	@Override
	public void init() throws Exception
	{
		new Handler();
		GameRegistry.registerTile("default_tile", new Tile(Assets.getTexture("grass")));
		GameRegistry.registerTile("rock_tile", new Tile(Assets.getTexture("rockTile")).setSolid(true));
		new Camera();
		new World(100, 100);
		new ObjectManager(new Player());
		Handler.getObjectManager().add(new Block("block", Assets.getTexture("rock"), 1, 1).createNew(3, 6));
		Handler.getObjectManager().add(new Entity("pine", Assets.getTexture("pine"), 1, 2).setSolid(true).createNew(10, 5).setBounds(0, 1, 1, 1));
		// Handler.getObjectManager().add(new Aggressive("aggressive",
		// Assets.getTexture("mouse"), 1, 1).createNew(10, 9).setSolid(true));
		Handler.getObjectManager().add(new ItemContainer("itemContainer", Assets.getTexture("itemContainer"), 1, 1, 10).createNew(5, 5));
		ItemContainer container = (ItemContainer)new ItemContainer("itemContainer", Assets.getTexture("itemContainer"), 1, 1, 10).createNew(8, 5);
		container.getInventory().addItem(new Equipment("legend knife", Assets.getTexture("knife"), 1, 1));
		Handler.getObjectManager().add(container);
		Handler.getUis().add(new DamageUI());
		image = Utils.buildImage(64, 64, BufferedImage.TYPE_INT_ARGB, Assets.getTexture("grass").getCurrentSkin(), Assets.getTexture("stick").getCurrentSkin());
		// BufferedImage fontImage =
		// DrawUtils.replace(Assets.getTexture("imageFont").getCurrentSkin(),
		// Color.black, new Color(0, 0, 0, 255));
	}

	@Override
	public void update(double delta)
	{
		if(Engine.inputs.isKeyDown(Keyboard.ESCAPE))
		{
			System.exit(0);
		}
		Handler.update(delta);
		// for(UI ui : Handler.getUis())
		// {
		// System.out.println(ui);
		// }
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
	}

}

package core;

import java.awt.Graphics;

import assets.Assets;
import engine.Engine;
import engine.Instance;
import inputs.Keyboard;
import objects.ObjectManager;
import objects.blocks.Block;
import objects.entities.Aggressive;
import objects.entities.Entity;
import objects.entities.Player;
import objects.entities.containers.ItemContainer;
import physics.Camera;
import physics.Handler;
import registry.GameRegistry;
import tiles.Tile;
import uis.DamageUI;
import worlds.World;

public class Core implements Instance
{
	@Override
	public void init() throws Exception
	{
		new Handler();
		GameRegistry.registerTile("default_tile", new Tile(Assets.textures.get("grass")));
		GameRegistry.registerTile("rock_tile", new Tile(Assets.textures.get("rockTile")).setSolid(true));
		new Camera();
		new World(100, 100);
		new ObjectManager(new Player());
		Handler.getObjectManager().add(new Block("block", Assets.textures.get("rock"), 1, 1).createNew(3, 6));
		Handler.getObjectManager().add(new Entity("pine", Assets.textures.get("pine"), 1, 2).setSolid(true).createNew(10, 5).setBounds(0, 1, 1, 1));
		Handler.getObjectManager().add(new Aggressive("aggressive", Assets.textures.get("mouse"), 1, 1).createNew(10, 9).setSolid(true));
		Handler.getObjectManager().add(new ItemContainer("itemContainer", Assets.textures.get("itemContainer"), 1, 1, 10).createNew(5, 5));
		ItemContainer container = (ItemContainer)new ItemContainer("itemContainer", Assets.textures.get("itemContainer"), 1, 1, 10).createNew(8, 5);
		// container.getInventory().addItem(new Item("stick",
		// Assets.textures.get("stick"), 1, 1));
		Handler.getObjectManager().add(container);
		Handler.getUis().add(new DamageUI());
	}

	@Override
	public void update(double delta)
	{
		if(Engine.inputs.isKeyDown(Keyboard.ESCAPE))
		{
			System.exit(0);
		}
		Handler.update(delta);
	}

	@Override
	public void render(Graphics graphics)
	{
		Handler.render(graphics);

		// graphics.drawLine(0, Engine.getHeight() / 2, Engine.getWidth(),
		// Engine.getHeight() / 2);
		// graphics.drawLine(Engine.getWidth() / 2, 0, Engine.getWidth() / 2,
		// Engine.getHeight());
	}

}

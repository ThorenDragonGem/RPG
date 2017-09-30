package registry;

import java.util.HashMap;

import objects.GameObject;
import objects.blocks.Block;
import objects.entities.Entity;
import objects.items.Item;
import tiles.Tile;

public class GameRegistry
{
	private static HashMap<String, Tile> tilesRegister = new HashMap<>();
	private static HashMap<String, GameObject> objectsRegister = new HashMap<>();

	public static void registerTile(String tileIdName, Tile tile)
	{
		if(tilesRegister.containsKey(tileIdName))
		{
			return;
		}
		tilesRegister.put(tileIdName, tile);
	}

	public static HashMap<String, Tile> getTilesRegister()
	{
		return tilesRegister;
	}

	public static void registerGameObject(GameObject object)
	{
		if(objectsRegister.containsKey(object))
		{
			return;
		}
		objectsRegister.put(object == null ? "object_null" : object.getName(), object);
	}

	public static void registerItem(Item item)
	{
		if(objectsRegister.containsKey(item))
		{
			return;
		}
		objectsRegister.put(item == null ? "item_null" : "item_" + item.getName(), item);
	}

	public static void registerBlock(Block block)
	{
		if(objectsRegister.containsKey(block))
		{
			return;
		}
		objectsRegister.put(block == null ? "block_null" : "block_" + block.getName(), block);
	}

	public static void registerEntity(Entity entity)
	{
		if(objectsRegister.containsKey(entity))
		{
			return;
		}
		objectsRegister.put(entity == null ? "entity_null" : "entity_" + entity.getName(), entity);
	}

	public static HashMap<String, GameObject> getObjectsRegister()
	{
		return objectsRegister;
	}
}

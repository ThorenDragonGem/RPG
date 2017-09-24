package registry;

import java.util.HashMap;

import tiles.Tile;

public class GameRegistry
{
	// TODO GameRegistry.registerObject(object);

	private static HashMap<String, Tile> tilesRegister = new HashMap<>();

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
}

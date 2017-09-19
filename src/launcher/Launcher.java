package launcher;

import core.Core;
import engine.Engine;

public class Launcher
{
	public static void main(String[] args)
	{
		new Engine(new Core(), "RPG", 1280, 720, "./assets");
	}
}

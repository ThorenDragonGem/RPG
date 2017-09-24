package uis;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

import assets.Assets;
import engine.Engine;
import gfx.Animation;

public class DamageUI extends UI
{
	public DamageUI()
	{
		x = 0;
		y = 0;
		width = Engine.getWidth();
		height = Engine.getHeight();
		active = true;
		priority = 100;
		BufferedImage[] damages = Assets.getArray("damages", 0, 2);
		for(int i = 0; i < damages.length; i++)
		{
			damages[i] = Scalr.resize(damages[i], width, height);
		}
		skin = new Animation(100, damages);
		skin.setCurrentSkin(null);
	}

	@Override
	public void update(double delta)
	{

	}

	@Override
	public void render(Graphics graphics)
	{
		graphics.drawImage(skin.getCurrentSkin(), x, y, width, height, null);
	}
}

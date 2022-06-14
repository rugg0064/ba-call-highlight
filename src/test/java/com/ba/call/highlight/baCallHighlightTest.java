package com.ba.call.highlight;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class baCallHighlightTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(baCallHighlightPlugin.class);
		RuneLite.main(args);
	}
}
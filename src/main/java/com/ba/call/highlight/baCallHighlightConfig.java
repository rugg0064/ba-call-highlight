package com.ba.call.highlight;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("main")
public interface baCallHighlightConfig extends Config
{

	@ConfigItem(
			keyName = "highlightCallColor",
			name = "Hightlight call color",
			description = "Configures the color to highlight the horn call options",
			position = 0
	)
	default Color highlightCallColor() { return Color.green; }
}

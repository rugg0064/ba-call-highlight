package com.ba.call.highlight;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@PluginDescriptor(
	name = "BA Call Highlight",
	description = "Highlights the correct call option in barbarian assault",
	tags = {"overlay", "b.a.", "barbarian assault", "minigame", "attacker", "defender", "collector", "healer", "plugin hub"}
)
public class baCallHighlightPlugin extends Plugin
{
	@Inject private Client client;
	@Inject private baCallHighlightConfig config;

	private final Map<String, String> callToMenuEntry = new HashMap<>();

	private Role role;

	private enum Role
	{
		COLLECTOR(486, 9),
		HEALER   (488, 9),
		ATTACKER (485, 10),
		DEFENDER (487, 9);

		public final int groupID;
		public final int childID;

		Role(int groupID, int childID)
		{
			this.groupID = groupID;
			this.childID = childID;
		}
	}

	@Override
	protected void startUp() throws Exception
	{
		callToMenuEntry.put("Red egg", "Tell-red");
		callToMenuEntry.put("Green egg", "Tell-green");
		callToMenuEntry.put("Blue egg", "Tell-blue");
		callToMenuEntry.put("Controlled/Bullet/Wind", "Tell-controlled");
		callToMenuEntry.put("Accurate/Field/Water", "Tell-accurate");
		callToMenuEntry.put("Aggressive/Blunt/Earth", "Tell-aggressive");
		callToMenuEntry.put("Defensive/Barbed/Fire", "Tell-defensive");
		callToMenuEntry.put("Tofu", "Tell-tofu");
		callToMenuEntry.put("Crackers", "Tell-crackers");
		callToMenuEntry.put("Worms", "Tell-worms");
		callToMenuEntry.put("Pois. Tofu", "Tell-tofu");
		callToMenuEntry.put("Pois. Worms", "Tell-worms");
		callToMenuEntry.put("Pois. Meat", "Tell-meat");
	}

	@Provides
	baCallHighlightConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(baCallHighlightConfig.class);
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		final int groupId = event.getGroupId();

		if (groupId == Role.COLLECTOR.groupID)
		{
			role = Role.COLLECTOR;
		}
		else if (groupId == Role.HEALER.groupID)
		{
			role = Role.HEALER;
		}
		else if (groupId == Role.ATTACKER.groupID)
		{
			role = Role.ATTACKER;
		}
		else if (groupId == Role.DEFENDER.groupID)
		{
			role = Role.DEFENDER;
		}
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		if (role == null)
		{
			return;
		}

		Widget activeWidget = client.getWidget(role.groupID, role.childID);
		if (activeWidget == null)
		{
			return;
		}

		for (MenuEntry entry : client.getMenuEntries())
		{
			if (Text.removeTags(entry.getOption()).equals(callToMenuEntry.get(activeWidget.getText())))
			{
				entry.setOption(ColorUtil.prependColorTag(Text.removeTags(entry.getOption()), config.highlightCallColor()));
			}
			else
			{
				entry.setOption(Text.removeTags(entry.getOption()));
			}
		}
	}
}

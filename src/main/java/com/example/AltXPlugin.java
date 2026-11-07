Java
		package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.awt.Robot;
import java.awt.event.KeyEvent;

@Slf4j
@PluginDescriptor(
		name = "Alt+X Kill Trigger",
		description = "Automatically presses Alt+X when your character gets a kill",
		enabledByDefault = true
)
public class AltXPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AltXConfig config;

	private Robot robot;

	@Override
	protected void startUp() throws Exception
	{
		robot = new Robot();
		log.info("Alt+X Trigger plugin started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Alt+X Trigger plugin stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		// 1. Get the local player object safely
		Player localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
		{
			return; // Player isn't fully logged in yet
		}

		// 2. Extract the player's username
		String myUsername = localPlayer.getName();
		if (myUsername == null)
		{
			return;
		}

		String message = event.getMessage();

		// 3. Example verification: Check if the message is your kill count AND matches your character
		// (Many OSRS kill messages mention your name or personal game feeds)
		if (message.contains("has defeated") || message.contains("has defeated"))
		{
			// Optional: You can also verify if the chat sender or context involves your username if needed
			log.info("{}: has defeated", myUsername);
			triggerAltX();
		}
	}

	private void triggerAltX()
	{
		try
		{
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_X);
			robot.keyRelease(KeyEvent.VK_X);
			robot.keyRelease(KeyEvent.VK_ALT);

			log.info("Simulating Alt+X press.");
		}
		catch (Exception e)
		{
			log.error("Failed to simulate keystroke", e);
		}
	}

	@Provides
	AltXConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AltXConfig.class);
	}
}
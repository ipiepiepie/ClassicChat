package xyz.ipiepiepie.chat.listener;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.enums.Priority;
import org.useless.serverlibe.api.event.player.PlayerChatEvent;
import xyz.ipiepiepie.chat.ChatManager;
import xyz.ipiepiepie.chat.ChatMod;
import xyz.ipiepiepie.chat.object.Channel;

public class ChannelListener implements Listener {

	@EventListener(priority = Priority.LOWEST)
	public void chatListener(PlayerChatEvent event) {
		if (!ChatMod.CONFIG.isChannelsEnabled()) return;

		Player player = event.player;
		// get sent message
		String message = event.getMessage();
		// remove player name from message
		message = message.replaceFirst(String.format("<%s§r> ", player.getDisplayName()), "");
		// try to get channel for message
		Channel channel = ChatManager.getInstance().getChannel(player, message);

		// remove prefix if player used it in message
		if (channel.hasPrefix() && TextFormatting.removeAllFormatting(message).startsWith(channel.getPrefix()))
			message = message.replaceFirst(channel.getPrefix(), "");

		// send message to the channel
		channel.sendMessage(player, message);

		// prevent default minecraft message send
		event.setCancelled(true);
	}



}

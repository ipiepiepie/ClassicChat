package xyz.ipiepiepie.chat.listener;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.event.player.PlayerChatEvent;
import xyz.ipiepiepie.chat.ChatManager;
import xyz.ipiepiepie.chat.ChatMod;
import xyz.ipiepiepie.chat.object.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingListener implements Listener {

	@EventListener()
	public void chatListener(PlayerChatEvent event) {
		if (!ChatMod.CONFIG.isPingEnabled()) return;

		Player sender = event.player;
		// get sent message
		String message = event.getMessage();
		// fetch all @pings from message
		Matcher matcher = Pattern.compile("@(\\w+)").matcher(message);

		// list all pinged names to skip if they are already pinged
		List<String> pingedNames = new ArrayList<>();
		// iterate over pings
		while (matcher.find()) {
			String ping = matcher.group();
			String nickname = ping.replaceFirst("@", "");
			// skip pinged players
			if (pingedNames.contains(nickname)) continue;
			// get player by its nickname
			Player receiver = MinecraftServer.getInstance().playerList.playerEntities
				.stream()
				.filter(p -> TextFormatting.removeAllFormatting(p.getDisplayName()).equalsIgnoreCase(TextFormatting.removeAllFormatting(nickname)))
				.findFirst()
				.orElse(null);

			// skip current ping if there is no player
			if (receiver == null) continue;

			// check channels if enabled
			if (ChatMod.CONFIG.isChannelsEnabled()) {
				Channel channel = ChatManager.getInstance().getChannel(sender, message.replaceFirst(String.format("<%s§r> ", sender.getDisplayName()), ""));

				// if we can't reach pinging player, then we can't ping them
				if (!channel.canHear(sender, receiver)) {
					message = message.replace(ping, TextFormatting.LIGHT_GRAY + ping + TextFormatting.RESET);
					pingedNames.add(nickname);
					continue;
				// don't ping player if sender has cooldown in current channel
				} else if (channel.hasCooldown(sender)) {
					pingedNames.add(nickname);
					continue;
				}
			}

			// play ping sound to pinged player
			assert receiver.world != null;
			receiver.world.playSoundAtEntity(null, receiver, ChatMod.CONFIG.getPingSound(), 1f, 1f);

			// format ping in message
			message = message.replace(ping, TextFormatting.getColorFormatting(ChatMod.CONFIG.getPingColor()) + ping + TextFormatting.RESET);
			pingedNames.add(nickname);
		}

		event.setMessage(message);
	}

}

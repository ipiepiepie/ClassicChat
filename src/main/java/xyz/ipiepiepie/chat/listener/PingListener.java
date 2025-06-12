package xyz.ipiepiepie.chat.listener;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.enums.Priority;
import org.useless.serverlibe.api.event.player.PlayerChatEvent;
import xyz.ipiepiepie.chat.ChatMod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingListener implements Listener {

	@EventListener(priority = Priority.HIGH)
	public void chatListener(PlayerChatEvent event) {
		if (!ChatMod.CONFIG.isPingEnabled()) return;

		// get sent message
		String message = event.getMessage();
		// fetch all @pings from message
		Matcher matcher = Pattern.compile("@(\\w+)").matcher(message);

		// iterate over pings
		while (matcher.find()) {
			String ping = matcher.group();
			String nickname = ping.replaceFirst("@", "");
			// get player by its nickname
			Player player = MinecraftServer.getInstance().playerList.playerEntities
				.stream()
				.filter(p -> TextFormatting.removeAllFormatting(p.getDisplayName()).equalsIgnoreCase(TextFormatting.removeAllFormatting(nickname)))
				.findFirst()
				.orElse(null);

			// skip current ping if there is no player
			if (player == null) continue;

			// play ping sound to pinged player
			assert player.world != null;
			player.world.playSoundAtEntity(null, player, ChatMod.CONFIG.getPingSound(), 1f, 1f);

			// format ping in message
			message = message.replaceFirst(ping, TextFormatting.getColorFormatting(ChatMod.CONFIG.getPingColor()) + ping + TextFormatting.RESET);
		}

		event.setMessage(message);
	}

}

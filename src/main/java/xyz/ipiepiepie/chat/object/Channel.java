package xyz.ipiepiepie.chat.object;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import xyz.ipiepiepie.chat.ChatMod;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Channel {
	private final String name;
	private final String format;
	private final String prefix;
	private final int cooldown;
	private final int distance;
	// cooldown //
	private final Map<Player, Instant> lastMessageTime = new HashMap<>();

	public Channel(String name, String format, String prefix, int cooldown, int distance) {
		this.name = name;
		this.format = format;
		this.prefix = prefix;
		this.cooldown = cooldown;
		this.distance = distance;
	}

	/**
	 * Send message without using {@link #format} to the channel.
	 * @param sender sender of message
	 * @param message message text
	 */
	public void sendUnformattedMessage(Player sender, String message, boolean noAudienceNotification) {
		Instant lastMessageTime = this.lastMessageTime.get(sender);

		// cooldown check
		if (lastMessageTime != null && cooldown > 0) {
			long secondsFromLastMessage = Duration.between(Instant.now(), lastMessageTime).abs().getSeconds();

			// prevent spamming
			if (secondsFromLastMessage <= this.cooldown) {
				sender.sendMessage(TextFormatting.RED + "Wait " + TextFormatting.WHITE + (this.cooldown - secondsFromLastMessage) + " seconds " + TextFormatting.RED + "before sending message in this channel!");
				return;
			}
		}

		// get audience for the message
		List<PlayerServer> audience = getAudience(sender);

		// send message to the audience
		for (Player receiver : audience)
			receiver.sendMessage(message);

		// notify player if no one can hear due to the channel distance limitations
		if (audience.size() == 1 && this.distance > 0 && ChatMod.CONFIG.isNobodyHeardNotificationEnabled() && noAudienceNotification) // size == 1 since players can hear themselves :)
			sender.sendMessage(TextFormatting.RED + "Nobody heard you");

		// write current message for future cooldown
		if (cooldown > 0) this.lastMessageTime.put(sender, Instant.now());
	}

	/**
	 * Send message to the channel.
	 * @param sender sender of message
	 * @param message message text
	 */
	public void sendMessage(Player sender, String message) {
		this.sendUnformattedMessage(sender, format.replace("%player%", sender.getDisplayName()).replace("%message%", message), true);
	}

	/**
	 * Get {@link List} of {@link Player Players} who can hear {@code player}.
	 * @param player player to check who can hear them
	 * @return {@link List} of {@link Player Players}
	 */
	public List<PlayerServer> getAudience(Player player) {
		List<PlayerServer> audience = MinecraftServer.getInstance().playerList.playerEntities;

		// if channel has distance limitations, cut audience to only nearby players
		if (this.distance > 0) {
			audience = audience.stream().filter(other -> this.canHear(player, other)).collect(Collectors.toList());
		}

		return audience;
	}

	/**
	 * Check if one {@link Player} can hear another in this channel.
	 * @param first first {@link Player}
	 * @param second second {@link Player}
	 * @return {@code true} if they can hear each other, otherwise {@code false}
	 */
	public boolean canHear(Player first, Player second) {
		if (distance <= 0) return true;

		// check if their distance doesn't exceed the limit
		return first.distanceTo(second) <= this.distance;
	}

	/*==========================================* GETTERS *===========================================*/

	// NAME //

	public String getName() {
		return name;
	}

	// FORMAT //

	public String getFormat() {
		return format;
	}

	// PREFIX //

	public boolean hasPrefix() {
		return prefix != null;
	}

	public String getPrefix() {
		return prefix;
	}

	// COOLDOWN //

	public int getCooldown() {
		return cooldown;
	}

	// DISTANCE //

	public int getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		return name;
	}
}

package xyz.ipiepiepie.chat;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import xyz.ipiepiepie.chat.object.Channel;

import java.util.*;

public class ChatManager {
	private static ChatManager instance;
	// internal structures //
	private Channel defaultChannel;
	private final Map<String, Channel> channels = new HashMap<>();
	private final Map<UUID, Channel> playerChannels = new HashMap<>();
	private final Map<UUID, UUID> lastConversation = new HashMap<>();

	public static ChatManager getInstance() {
		return instance;
	}

	/*===========================================* CHANNEL *==========================================*/

	/**
	 * Reset {@link Channel} for {@link Player}.
	 * @param player player to reset channel for
	 */
	public void resetChannel(Player player) {
		this.playerChannels.remove(player.uuid);
	}

	/**
	 * Set {@link Channel} for {@link Player}.
	 * @param player player to set channel for
	 * @param channel some channel
	 */
	public void setChannel(Player player, Channel channel) {
		this.playerChannels.put(player.uuid, channel);
	}

	/**
	 * Get {@link Channel} by its name.
	 * @param name channel name
	 * @return {@link Channel} if exists, otherwise {@code null}
	 */
	public Channel getChannel(String name) {
		return channels.get(name);
	}

	/**
	 * Get {@link Channel} used by player.
	 * @param player player to get channel for
	 * @return specific {@link Channel} if used, otherwise {@link #defaultChannel default channel}.
	 */
	public Channel getChannel(Player player) {
		return playerChannels.getOrDefault(player.uuid, defaultChannel);
	}

	/**
	 * Get {@link Channel} for {@link Player Player's} message.
	 * <p>
	 * Firstly this method tries to find {@link Channel} by message's prefix. If there is no prefix in this message, then method returns {@link #getChannel(Player)} result.
	 *
	 * @param player message sender
	 * @param message message itself
	 * @return {@link Channel} for message.
	 */
	public Channel getChannel(Player player, String message) {
		char prefix = TextFormatting.removeAllFormatting(message).charAt(0);

		return channels.values()
			.stream()
			.filter(c -> c.hasPrefix() && c.getPrefix().equals(String.valueOf(prefix)))
			.findFirst()
			.orElse(getChannel(player));
	}

	/**
	 * Get {@link List} of all {@link Channel Channels}.
	 * @return {@link List} of all {@link Channel Channels}
	 */
	public List<Channel> getChannels() {
		return new ArrayList<>(channels.values());
	}

	/*===========================================* MESSAGE *==========================================*/

	/**
	 * Cache the latest /msg conversation for {@link Player Players}.
	 * @param first the first player
	 * @param second the second player
	 */
	public void setLastConversation(Player first, Player second) {
		lastConversation.put(first.uuid, second.uuid);
		lastConversation.put(second.uuid, first.uuid);
	}

	public UUID getLastConversation(Player player) {
		return lastConversation.get(player.uuid);
	}

	/*============================================* LOAD *============================================*/

	/**
	 * Load (or reload) data into internal structures.
	 */
	public void load(boolean init) {
		if (init) instance = this;

		if (ChatMod.CONFIG.isChannelsEnabled()) this.loadChannels();
	}

	/**
	 * Load {@link Channel Channels} from config.
	 */
	private void loadChannels() {
		// clear old channels data
		this.channels.clear();

		// load channels by names
		for (String name : ChatMod.CONFIG.getChannels()) {
			channels.put(name.toLowerCase(), new Channel(
				name.toLowerCase(),
				ChatMod.CONFIG.getChannelFormat(name),
				ChatMod.CONFIG.getChannelPrefix(name),
				ChatMod.CONFIG.getChannelCooldown(name),
				ChatMod.CONFIG.getChannelDistance(name)
			));
		}

		this.defaultChannel = getChannel(ChatMod.CONFIG.getDefaultChannel());
		// set default channel if it isn't set in config
		if (defaultChannel == null)
			defaultChannel = getChannel(channels.keySet().iterator().next());
	}

}

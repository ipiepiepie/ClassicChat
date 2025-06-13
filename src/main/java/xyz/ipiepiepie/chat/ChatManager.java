package xyz.ipiepiepie.chat;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import xyz.ipiepiepie.chat.object.Channel;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		// check if there is no prefix
		if (TextFormatting.removeAllFormatting(message).isEmpty()) return getChannel(player);
		// get prefix
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

	/*=====================================* CUSTOM COLOR CODES *=====================================*/

	/**
	 * Translate custom color codes in message.
	 * @param message message with potential color codes
	 * @return message with translated color codes
	 */
	public String translateCustomColorCodes(String message) {
		String result = ChatMod.CONFIG.isAdvancedColorCodesEnabled() ? translateAdvancedColorCodes(message) : message;
		// fetch all custom color codes from message
		Matcher matcher = Pattern.compile(Pattern.quote(ChatMod.CONFIG.getColorCodeSymbol()) + "[0123456789abcdefklmnor]").matcher(result);

		// iterate over pings
		while (matcher.find()) {
			String color = matcher.group();

			// replace color code
			result = result.replaceFirst(color, "§" + color.substring(1));
		}

		return result;
	}

	/**
	 * Translate advanced color codes (gradients at the moment).
	 * @param message message with potential color codes
	 * @return message with translated color codes
	 */
	public String translateAdvancedColorCodes(String message) {
		String result = translateRainbowColorCodes(message);
		// fetch all advanced color codes from message
		Pattern regex = Pattern.compile(Pattern.quote(ChatMod.CONFIG.getColorCodeSymbol()) + "\\{[0123456789abcdefklmnor]+(?:,\\s*\\d+)?}");
		Matcher matcher = regex.matcher(result);

		// iterate over advanced color codes
		while (matcher.find()) {
			String code = matcher.group();
			String pattern;
			int period = 1;

			// setup period if there is any comma
			if (code.contains(",")) {
				try {
					String stepsStr = code.split(",\\s*")[1];
					period = Integer.parseInt(stepsStr.substring(0, stepsStr.length() - 1));
				} catch (NumberFormatException ignore) {}
				pattern = code.split(",")[0].substring(2); // remove brace at the start
			} else {
				pattern = code.substring(2, code.length() - 1);
			}

			StringBuilder parser = new StringBuilder();
			// color codes boundaries
			int from = matcher.end();
			int to = result.length();

			// iterate over string characters and replace them with colors from pattern
			int step = 0;
			for (int i = from; i < result.length(); i++) {
				// stop coloring string if we reached another color code
				if (result.charAt(i) == '§' || result.charAt(i) == ChatMod.CONFIG.getColorCodeSymbol().charAt(0)) {
					to = i;
					break;
				}

				// skip whitespaces
				if (result.charAt(i) == ' ') {
					parser.append(" ");
					continue;
				}

				// place color code before reached character
				parser.append("§").append(pattern.charAt((step / period) % pattern.length())).append(result.charAt(i));

				// increase step counter
				step++;
			}

			// apply result
			result = result.substring(0, from) + parser + result.substring(to);

			// remove code from string
			result = result.replaceFirst(regex.pattern(), "");
			// add new result to matcher
			matcher = regex.matcher(result);
		}

		return result;
	}

	/**
	 * Translate {@code &{rainbow}} color codes.
	 * @param message message to find color codes
	 * @return message with translated &{rainbow} color codes
	 */
	public String translateRainbowColorCodes(String message) {
		String result = message;
		// fetch all advanced color codes from message
		Pattern regex = Pattern.compile(Pattern.quote(ChatMod.CONFIG.getColorCodeSymbol()) + "\\{rainbow+(?:,\\s*\\d+)?}");
		Matcher matcher = regex.matcher(result);

		while (matcher.find()) {
			String code = matcher.group();
			int period = 1;

			//
			if (code.contains(",")) {
				try {
					String stepsStr = code.split(",\\s*")[1];
					period = Integer.parseInt(stepsStr.substring(0, stepsStr.length() - 1));
				} catch (NumberFormatException ignore) {}
			}

			result = result.replaceFirst(regex.pattern(), String.format("&{e1453ba,%s}", period));
		}

		return result;
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

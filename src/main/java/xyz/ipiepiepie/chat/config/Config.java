package xyz.ipiepiepie.chat.config;

import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
	private final TomlConfigHandler config;

	public Config(String modID) {
		Toml toml = new Toml();

		// channels category //
		toml.addCategory("Channel");
		toml.addEntry("Channel.Enable", "enable channels feature", true);
		toml.addEntry("Channel.Default", "default channel used when players type to chat", "local");
		toml.addEntry("Channel.NobodyHeardNotification", "should mod notify players if nobody can hear them in limited by distance chat", true);
		toml.addCategory("Channel.Global");
		toml.addEntry("Channel.Global.Format", "§7[§1G§7]§r <%player%§r> %message%");
		toml.addEntry("Channel.Global.Cooldown", "cooldown between sending messages in seconds", 1);
		toml.addEntry("Channel.Global.Prefix", "prefix used to write to this channel", "!");
		toml.addCategory("Channel.Local");
		toml.addEntry("Channel.Local.Format", "<%player%§r> %message%");
		toml.addEntry("Channel.Local.Distance", "chat distance in blocks", 200);

		// messages category //
		toml.addCategory("Message");
		toml.addEntry("Message.Enable", "enable custom /msg command", true);
		toml.addEntry("Message.Format", "format of /msg messages", "§1[%sender% -> %receiver%]§r %message%");

		// ping category //
		toml.addCategory("Ping");
		toml.addEntry("Ping.Enable", "enable @ping feature", true);
		toml.addEntry("Ping.Color", "color to highlight ping in chat (use 'reset' to get rid of color highlight)", "orange");
		toml.addEntry("Ping.Sound", "sound, played to pinged player", "note.celesta");

		// roleplay category //
		toml.addCategory("Roleplay");
		toml.addEntry("Roleplay.Channel", "set channel for roleplay actions", "local");
		toml.addEntry("Roleplay.EnableMe", "enable /me command", true);
		toml.addEntry("Roleplay.EnableTry", "enable /try command", true);
		toml.addEntry("Roleplay.EnableRoll", "enable /roll command", true);

		// nick category //
		toml.addCategory("Nickname");
		toml.addEntry("Nickname.RemoveItalic", "remove italic formatting for changed via '/nick' command nicknames", true);

		// color category //
		toml.addCategory("ColorCode");
		toml.addEntry("ColorCode.Enable", "enable custom color codes for chat", true);
		toml.addEntry("ColorCode.Symbol", "custom color code for colors in chat", "&");
		toml.addEntry("ColorCode.AdvancedCodes", "enable &{} color codes, read more at wiki", true);

		config = new TomlConfigHandler(modID, toml);
		// add newly added nodes
		config.getRawParsed().addMissing(toml);
		config.writeConfig();
	}

	// CHANNELS //

	public boolean isChannelsEnabled() {
		return config.getBoolean("Channel.Enable");
	}

	public String getDefaultChannel() {
		return config.getString("Channel.Default");
	}

	public boolean isNobodyHeardNotificationEnabled() {
		return config.getBoolean("Channel.NobodyHeardNotification");
	}

	public List<String> getChannels() {
		List<String> result = new ArrayList<>();
		// regex for channels
		Matcher matcher = Pattern.compile("\\[Channel\\.([^]]+)]").matcher(config.getRawParsed().toString());

		// find all matches and print the channel names
		while (matcher.find()) {
			String channel = matcher.group();
			result.add(channel.substring(9, channel.length() - 1));
		}

		// exclude some config options
		result.remove("Enable");
		result.remove("Default");
		result.remove("NobodyHeardNotification");

		return result;
	}

	public String getChannelFormat(String channel) {
		return config.getString(String.format("Channel.%s.Format", channel));
	}

	public String getChannelPrefix(String channel) {
		return config.getString(String.format("Channel.%s.Prefix", channel));
	}

	public int getChannelCooldown(String channel) {
		String path = String.format("Channel.%s.Cooldown", channel);

		if (config.getRawParsed().contains(path))
			return config.getInt(path);
		else
			return 0;
	}

	public int getChannelDistance(String channel) {
		String path = String.format("Channel.%s.Distance", channel);

		if (config.getRawParsed().contains(path))
			return config.getInt(path);
		else
			return -1;
	}

	// MESSAGE //

	public boolean isMessagesEnabled() {
		return config.getBoolean("Message.Enable");
	}

	public String getMessageFormat() {
		return config.getString("Message.Format");
	}

	// PING //

	public boolean isPingEnabled() {
		return config.getBoolean("Ping.Enable");
	}

	public String getPingSound() {
		return Optional.of(config.getString("Ping.Sound")).orElse("reset");
	}

	public String getPingColor() {
		return config.getString("Ping.Color");
	}

	// ROLEPLAY //

	public String getRoleplayChannel() {
		return config.getString("Roleplay.Channel");
	}

	public boolean isMeEnabled() {
		return config.getBoolean("Roleplay.EnableMe");
	}

	public boolean isTryEnabled() {
		return config.getBoolean("Roleplay.EnableTry");
	}

	public boolean isRollEnabled() {
		return config.getBoolean("Roleplay.EnableRoll");
	}

	// NICKNAME //

	public boolean shouldRemoveItalicFromNickname() {
		return config.getBoolean("Nickname.RemoveItalic");
	}

	// COLOR //

	public boolean isColorCodeEnabled() {
		return config.getBoolean("ColorCode.Enable");
	}

	public boolean isAdvancedColorCodesEnabled() {
		return config.getBoolean("ColorCode.AdvancedCodes");
	}

	public String getColorCodeSymbol() {
		return config.getString("ColorCode.Symbol");
	}

}

package xyz.ipiepiepie.chat.config;

import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

public class Config {
	private final TomlConfigHandler config;

	public Config(String modID) {
		Toml toml = new Toml();

		// channels category //


		// ping category //
		toml.addCategory("Ping");
		toml.addEntry("Ping.Enabled", "enable @ping feature", true);
		toml.addEntry("Ping.Color", "color to highlight ping in chat (use 'reset' to get rid of color highlight)", "orange");
		toml.addEntry("Ping.Sound", "sound, played to pinged player", "note.celesta");

		// nick category //
		toml.addCategory("Nickname");
		toml.addEntry("Nickname.RemoveItalic", "remove italic formatting for changed via '/nick' command nicknames", true);

		config = new TomlConfigHandler(modID, toml);
	}

	// PING //

	public boolean isPingEnabled() {
		return config.getBoolean("Ping.Enabled");
	}

	public String getPingSound() {
		return config.getString("Ping.Sound");
	}

	public String getPingColor() {
		return config.getString("Ping.Color");
	}

	// NICKNAME //

	public boolean shouldRemoveItalicFromNickname() {
		return config.getBoolean("Nickname.RemoveItalic");
	}

}

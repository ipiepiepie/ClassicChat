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

		config = new TomlConfigHandler(modID, toml);
	}

	// PING //

	public boolean isPingEnabled() {
		return config.getBoolean("Ping.Enabled");
	}

	public String getPingSound() {
		//return config.getString("Ping.Sound");
		return "note.harp";
	}

	public String getPingColor() {
		return config.getString("Ping.Color");
	}


}

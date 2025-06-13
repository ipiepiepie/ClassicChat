package xyz.ipiepiepie.chat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.serverlibe.ServerLibe;
import xyz.ipiepiepie.chat.command.ChannelCommand;
import xyz.ipiepiepie.chat.command.RollCommand;
import xyz.ipiepiepie.chat.command.TryCommand;
import xyz.ipiepiepie.chat.config.Config;
import xyz.ipiepiepie.chat.listener.ChannelListener;
import xyz.ipiepiepie.chat.listener.PingListener;

public class ChatMod implements ModInitializer {
    public static final String MOD_ID = "cchat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Config CONFIG = new Config(MOD_ID);

	@Override
    public void onInitialize() {
		// register listeners
		this.registerListeners();
		// register commands
		this.registerCommands();

		// load internal structures
		new ChatManager().load(true);

        LOGGER.info("Classic Chat initialized.");
    }

	private void registerCommands() {
		if (CONFIG.isChannelsEnabled()) CommandManager.registerCommand(new ChannelCommand());
		if (CONFIG.isTryEnabled()) CommandManager.registerCommand(new TryCommand());
		if (CONFIG.isRollEnabled()) CommandManager.registerCommand(new RollCommand());
	}

	private void registerListeners() {
		ServerLibe.registerListener(new PingListener());
		ServerLibe.registerListener(new ChannelListener());
	}

}

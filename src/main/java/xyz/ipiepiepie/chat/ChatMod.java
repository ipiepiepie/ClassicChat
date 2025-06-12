package xyz.ipiepiepie.chat;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.serverlibe.ServerLibe;
import xyz.ipiepiepie.chat.config.Config;
import xyz.ipiepiepie.chat.listener.PingListener;


public class ChatMod implements ModInitializer {
    public static final String MOD_ID = "classic_chat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Config CONFIG = new Config(MOD_ID);

	@Override
    public void onInitialize() {
		// register listeners
		registerListeners();
        LOGGER.info("Classic Chat initialized.");
    }

	private void registerListeners() {
		ServerLibe.registerListener(new PingListener());
	}

}

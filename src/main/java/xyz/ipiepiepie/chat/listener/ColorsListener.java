package xyz.ipiepiepie.chat.listener;

import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.event.player.PlayerChatEvent;
import xyz.ipiepiepie.chat.ChatManager;
import xyz.ipiepiepie.chat.ChatMod;

public class ColorsListener implements Listener {

	@EventListener()
	public void chatListener(PlayerChatEvent event) {
		if (!ChatMod.CONFIG.isColorCodeEnabled()) return;

		// translate color codes
		event.setMessage(ChatManager.getInstance().translateCustomColorCodes(event.getMessage()));
	}

}

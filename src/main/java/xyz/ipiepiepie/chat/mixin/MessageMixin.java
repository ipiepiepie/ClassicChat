package xyz.ipiepiepie.chat.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.commands.CommandMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.ipiepiepie.chat.ChatMod;
import xyz.ipiepiepie.chat.command.MessageCommand;

@Mixin(value = CommandMessage.class, remap = false)
public class MessageMixin {

	/**
	 * Override default /message command
	 */
	@Inject(method = "register", at = @At("HEAD"), cancellable = true)
	private void overrideMessageCommand(CommandDispatcher<CommandSource> dispatcher, CallbackInfo ci) {
		if (!ChatMod.CONFIG.isMessagesEnabled()) return;

		new MessageCommand().register(dispatcher);

		ci.cancel();
	}

}

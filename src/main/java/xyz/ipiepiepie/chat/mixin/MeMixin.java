package xyz.ipiepiepie.chat.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.net.command.commands.CommandMe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.ipiepiepie.chat.ChatMod;
import xyz.ipiepiepie.chat.command.MeCommand;

@Mixin(value = CommandMe.class, remap = false)
public class MeMixin {

	/**
	 * Override default /me command
	 */
	@Inject(method = "register", at = @At("HEAD"), cancellable = true)
	private void overrideMeCommand(CommandDispatcher<CommandSource> dispatcher, CallbackInfo ci) {
		if (!ChatMod.CONFIG.isMeEnabled()) return;

		new MeCommand().register(dispatcher);

		ci.cancel();
	}

}

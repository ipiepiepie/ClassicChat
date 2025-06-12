package xyz.ipiepiepie.chat.mixin;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.ipiepiepie.chat.ChatMod;

@Mixin(value = PlayerServer.class, remap = false)
public abstract class NicknameMixin {

	/**
	 * Remove italic from changed nickname if enabled.
	 */
	@Inject(method = "getDisplayName", at = @At(value = "RETURN"), cancellable = true)
	public void getDisplayNameMixin(CallbackInfoReturnable<String> cir) {
		if (ChatMod.CONFIG.shouldRemoveItalicFromNickname())
			cir.setReturnValue(cir.getReturnValue().replace(TextFormatting.ITALIC.toString(), ""));
	}


}

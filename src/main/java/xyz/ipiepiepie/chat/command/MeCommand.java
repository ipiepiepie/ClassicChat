package xyz.ipiepiepie.chat.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.PlayerServer;
import xyz.ipiepiepie.chat.ChatManager;
import xyz.ipiepiepie.chat.ChatMod;
import xyz.ipiepiepie.chat.object.Channel;

public class MeCommand implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("me");

		this.me(builder);

		dispatcher.register(builder);
	}

	/*==========================================* COMMANDS *==========================================*/

	/**
	 * {@literal /me <action>}
	 */
	private void me(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderRequired.<CommandSource, String>argument("action", ArgumentTypeString.greedyString()).executes(context -> {
			PlayerServer sender = (PlayerServer) context.getSource().getSender();
			String action = context.getArgument("action", String.class);
			// validate sender argument
			if (sender == null) return 0;

			Channel channel = ChatManager.getInstance().getChannel(ChatMod.CONFIG.getRoleplayChannel());

			channel.sendUnformattedMessage(sender, TextFormatting.MAGENTA + TextFormatting.removeAllFormatting(sender.getDisplayName()) + " " + TextFormatting.PINK + action, false);

			return Command.SINGLE_SUCCESS;
		}));
	}

}

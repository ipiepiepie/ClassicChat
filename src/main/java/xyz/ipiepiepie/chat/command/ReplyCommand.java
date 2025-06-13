package xyz.ipiepiepie.chat.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import xyz.ipiepiepie.chat.ChatManager;
import xyz.ipiepiepie.chat.ChatMod;

import java.util.UUID;

public class ReplyCommand implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("reply");

		this.reply(builder);

		// register command itself
		CommandNode<CommandSource> command = dispatcher.register(builder);
		// register alias
		dispatcher.register(ArgumentBuilderLiteral.<CommandSource>literal("r").redirect(command));
	}

	/*==========================================* COMMANDS *==========================================*/

	private void reply(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderRequired.<CommandSource, String>argument("message", ArgumentTypeString.greedyString()).executes(context -> {
			PlayerServer sender = (PlayerServer) context.getSource().getSender();
			String text = context.getArgument("message", String.class);
			// validate sender argument
			if (sender == null) return 0;

			UUID lastReceiver = ChatManager.getInstance().getLastConversation(sender);

			// send error if there is no last conversation
			if (lastReceiver == null) {
				sender.sendMessage(TextFormatting.RED + "Player not found");
				return Command.SINGLE_SUCCESS;
			}

			PlayerServer receiver = MinecraftServer.getInstance().playerList.playerEntities.stream().filter(p -> p.uuid.equals(lastReceiver)).findFirst().orElse(null);

			if (receiver == null) {
				sender.sendMessage(TextFormatting.RED + "Player is offline");
			} else {
				// generate message for the current receiver
				String message = ChatMod.CONFIG.getMessageFormat()
					.replace("%sender%", TextFormatting.removeAllFormatting(sender.getDisplayName()))
					.replace("%receiver%", TextFormatting.removeAllFormatting(receiver.getDisplayName()))
					.replace("%message%", text);

				// send message
				sender.sendMessage(message);
				receiver.sendMessage(message);
			}

			return Command.SINGLE_SUCCESS;
		}));
	}

}

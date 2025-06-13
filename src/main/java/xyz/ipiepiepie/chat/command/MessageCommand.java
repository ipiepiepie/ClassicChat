package xyz.ipiepiepie.chat.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.net.command.helpers.EntitySelector;
import net.minecraft.server.entity.player.PlayerServer;
import xyz.ipiepiepie.chat.ChatManager;
import xyz.ipiepiepie.chat.ChatMod;

import java.util.List;

public class MessageCommand implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("message");

		this.message(builder);

		// register command itself
		CommandNode<CommandSource> command = dispatcher.register(builder);
		// register alias
		dispatcher.register(ArgumentBuilderLiteral.<CommandSource>literal("msg").redirect(command));
		dispatcher.register(ArgumentBuilderLiteral.<CommandSource>literal("whisper").redirect(command));
		dispatcher.register(ArgumentBuilderLiteral.<CommandSource>literal("tell").redirect(command));
	}

	/**
	 * {@literal /message <player> <message>}
	 */
	private void message(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderRequired.<CommandSource, EntitySelector>argument("targets", ArgumentTypeEntity.nickname()).then(ArgumentBuilderRequired.<CommandSource, String>argument("message", ArgumentTypeString.greedyString()).executes(context -> {
			PlayerServer sender = (PlayerServer) context.getSource().getSender();
			List<? extends Entity> targets = context.getArgument("targets", EntitySelector.class).get(context.getSource());
			String text = context.getArgument("message", String.class);
			// validate sender argument
			if (sender == null) return 0;

			// iterate over entity selector
			for (Entity entity : targets) {
				// we are sure that every entity here is a player
				Player receiver = (Player) entity;
				// generate message for the current receiver
				String message = ChatMod.CONFIG.getMessageFormat()
					.replace("%sender%", TextFormatting.removeAllFormatting(sender.getDisplayName()))
					.replace("%receiver%", TextFormatting.removeAllFormatting(receiver.getDisplayName()))
					.replace("%message%", text);

				// send message
				sender.sendMessage(message);
				receiver.sendMessage(message);

				// cache conversation for /r command
				ChatManager.getInstance().setLastConversation(sender, receiver);
			}

			return Command.SINGLE_SUCCESS;
		})));
	}

}

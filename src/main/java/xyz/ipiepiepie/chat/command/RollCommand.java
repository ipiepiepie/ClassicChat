package xyz.ipiepiepie.chat.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
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

import java.util.Random;

public class RollCommand implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("roll");

		this.roll(builder);
		this.rollNumber(builder);

		dispatcher.register(builder);
	}

	/*==========================================* COMMANDS *==========================================*/

	/**
	 * {@literal /roll}
	 */
	private void roll(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.executes(context -> {
			PlayerServer sender = (PlayerServer) context.getSource().getSender();
			// validate sender argument
			if (sender == null) return 0;

			Channel channel = ChatManager.getInstance().getChannel(ChatMod.CONFIG.getRoleplayChannel());
			// generate random number between 1 and 6
			int number = new Random().nextInt(6) + 1;

			channel.sendUnformattedMessage(sender, TextFormatting.MAGENTA + TextFormatting.removeAllFormatting(sender.getDisplayName())
				+ TextFormatting.PINK + " rolled a 6-sided dice and got the number " + TextFormatting.MAGENTA + number, false);

			return Command.SINGLE_SUCCESS;
		});
	}

	/**
	 * {@literal /roll <amount>}
	 */
	private void rollNumber(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("amount", ArgumentTypeInteger.integer(2, 1000)).executes(context -> {
			PlayerServer sender = (PlayerServer) context.getSource().getSender();
			int amount = context.getArgument("amount", Integer.class);
			// validate sender argument
			if (sender == null) return 0;

			Channel channel = ChatManager.getInstance().getChannel(ChatMod.CONFIG.getRoleplayChannel());
			// generate random number between 1 and 6
			int number = new Random().nextInt(amount) + 1;

			channel.sendUnformattedMessage(sender, TextFormatting.MAGENTA + TextFormatting.removeAllFormatting(sender.getDisplayName())
				+ TextFormatting.PINK + " rolled a " + amount + "-sided dice and got the number " + TextFormatting.MAGENTA + number, false);

			return Command.SINGLE_SUCCESS;
		}));
	}

}

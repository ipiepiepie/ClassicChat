package xyz.ipiepiepie.chat.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
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
import xyz.ipiepiepie.chat.command.argument.ArgumentTypeChannel;
import xyz.ipiepiepie.chat.object.Channel;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelCommand implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("channel");

		this.channel(builder);
		this.channelSelect(builder);
		this.channelPlayers(builder);

		// register command itself
		CommandNode<CommandSource> command = dispatcher.register(builder);
		// register alias
		dispatcher.register(ArgumentBuilderLiteral.<CommandSource>literal("ch").redirect(command));
	}

	/*==========================================* COMMANDS *==========================================*/

	/**
	 * {@literal /channel}
	 */
	private void channel(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.executes(context -> {
			PlayerServer sender = (PlayerServer) context.getSource().getSender();
			// validate sender argument
			if (sender == null) return 0;

			sender.sendMessage(TextFormatting.LIME + "You are currently using " + TextFormatting.GREEN + ChatManager.getInstance().getChannel(sender) + TextFormatting.LIME + " channel");

			return Command.SINGLE_SUCCESS;
		});
	}

	/**
	 * {@literal /channel select <channel>}
	 */
	private void channelSelect(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("select").then(ArgumentBuilderRequired.<CommandSource, String>argument("channel", ArgumentTypeChannel.channel()).executes(context -> {
			PlayerServer sender = (PlayerServer) context.getSource().getSender();
			Channel channel = ChatManager.getInstance().getChannel(context.getArgument("channel", String.class));
			// validate sender argument
			if (sender == null) return 0;

			// check if player tries to select already selected channel
			if (ChatManager.getInstance().getChannel(sender).equals(channel)) {
				sender.sendMessage(TextFormatting.RED + "You are already using " + TextFormatting.WHITE + channel + TextFormatting.RED + " channel");
				return Command.SINGLE_SUCCESS;
			}

			ChatManager.getInstance().setChannel(sender, channel);
			sender.sendMessage(TextFormatting.LIME + "Now you are using " + TextFormatting.GREEN + channel + TextFormatting.LIME + " channel");

			return Command.SINGLE_SUCCESS;
		})));
	}

	/**
	 * {@literal /channel players}
	 */
	private void channelPlayers(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("players").executes(context -> {
			PlayerServer sender = (PlayerServer) context.getSource().getSender();
			// validate sender argument
			if (sender == null) return 0;

			// prevent counting audiences if there is no other player on server
			if (MinecraftServer.getInstance().playerList.playerEntities.size() <= 1) {
				sender.sendMessage(TextFormatting.RED + "No one can hear you because you are the only one player on the server");
				return Command.SINGLE_SUCCESS;
			}

			Channel channel = ChatManager.getInstance().getChannel(sender);
			List<PlayerServer> audience = channel.getAudience(sender);

			// don't count player
			audience.remove(sender);

			if (audience.isEmpty()) {
				sender.sendMessage(TextFormatting.RED + "No one can hear you in the current channel");
			} else {
				sender.sendMessage(TextFormatting.GREEN + String.valueOf(audience.size()) + " players " + TextFormatting.LIME + "can hear you in the current channel: "
					+ TextFormatting.GREEN + audience.stream().map(p -> TextFormatting.removeAllFormatting(p.getDisplayName())).collect(Collectors.joining(", ")));
			}

			return Command.SINGLE_SUCCESS;
		}));
	}

}

package xyz.ipiepiepie.chat.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.net.command.ServerCommandSource;
import xyz.ipiepiepie.chat.ChatManager;
import xyz.ipiepiepie.chat.object.Channel;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeChannel implements ArgumentType<String> {

	public static ArgumentType<String> channel() {
		return new ArgumentTypeChannel();
	}

	public String parse(StringReader reader) throws CommandSyntaxException {
		return reader.readString();
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		if (context.getSource() instanceof ServerCommandSource) {
			// suggest channels based on input
			for (Channel channel : ChatManager.getInstance().getChannels()) {
				if (channel.getName().startsWith(builder.getRemaining()))
					builder.suggest(channel.getName());
			}
		}

		return builder.buildFuture();
	}

	public Collection<String> getExamples() {
		return Arrays.asList("global", "local");
	}

}

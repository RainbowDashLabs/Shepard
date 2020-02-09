package de.eldoria.shepard.contexts.commands.botconfig;

import de.eldoria.shepard.contexts.ContextCategory;
import de.eldoria.shepard.contexts.ContextSensitive;
import de.eldoria.shepard.contexts.commands.ArgumentParser;
import de.eldoria.shepard.contexts.commands.Command;
import de.eldoria.shepard.contexts.commands.argument.CommandArgument;
import de.eldoria.shepard.contexts.commands.argument.SubArgument;
import de.eldoria.shepard.database.queries.ContextData;
import de.eldoria.shepard.database.types.ContextSettings;
import de.eldoria.shepard.localization.enums.commands.GeneralLocale;
import de.eldoria.shepard.messagehandler.ErrorType;
import de.eldoria.shepard.messagehandler.MessageSender;
import de.eldoria.shepard.wrapper.MessageEventDataWrapper;

import static de.eldoria.shepard.localization.enums.commands.botconfig.ContextInfoLocale.DESCRIPTION;
import static java.lang.System.lineSeparator;

/**
 * Gives information about the settings of a registered and active {@link ContextSensitive}.
 */
public class ContextInfo extends Command {

    /**
     * Creates new context info command object.
     */
    public ContextInfo() {
        commandName = "contextInfo";
        commandDesc = DESCRIPTION.tag;
        commandAliases = new String[] {"cinfo"};
        commandArguments = new CommandArgument[] {new CommandArgument("context name", true,
                new SubArgument("context name", GeneralLocale.A_CONTEXT_NAME.tag))};
        category = ContextCategory.BOT_CONFIG;
    }

    @Override
    protected void internalExecute(String label, String[] args, MessageEventDataWrapper messageContext) {
        ContextSensitive context = ArgumentParser.getContext(args[0], messageContext);
        if (context != null) {
            ContextSettings data = ContextData.getContextData(context, messageContext);

            MessageSender.sendMessage("Information about context " + context.getContextName().toUpperCase()
                    + lineSeparator()
                    + "```yaml" + lineSeparator()
                    + data.toString() + lineSeparator() + "```", messageContext.getTextChannel());
        } else {
            MessageSender.sendSimpleError(ErrorType.INVALID_CONTEXT, messageContext.getTextChannel());
        }
    }
}

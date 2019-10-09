package de.eldoria.shepard.contexts.commands.util;

import de.eldoria.shepard.collections.CommandCollection;
import de.eldoria.shepard.contexts.commands.Command;
import de.eldoria.shepard.wrapper.MessageEventDataWrapper;

/**
 * A test command without specified behaviour.
 */
public class Test extends Command {

    public Test() {
        commandName = "test";
        commandDesc = "Testcommand!";
    }

    @Override
    protected void internalExecute(String label, String[] args, MessageEventDataWrapper messageContext) {
        Command help = CommandCollection.getInstance().getCommand("help");
        help.executeAsync(help, label, args, messageContext);
    }
}

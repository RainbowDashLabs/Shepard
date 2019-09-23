package de.eldoria.shepard.contexts.commands.fun;

import de.eldoria.shepard.wrapper.MessageEventDataWrapper;
import de.eldoria.shepard.messagehandler.MessageSender;
import de.eldoria.shepard.contexts.commands.Command;

public class Owo extends Command {

    /**
     * Creates a new owo keyword object.
     */
    public Owo() {
        commandName = "owo";
        commandDesc = "OWO";
    }


    @Override
    protected void internalExecute(String label, String[] args, MessageEventDataWrapper messageContext) {
        MessageSender.sendMessage(":regional_indicator_o::regional_indicator_w::regional_indicator_o:",
                messageContext.getChannel());
    }
}

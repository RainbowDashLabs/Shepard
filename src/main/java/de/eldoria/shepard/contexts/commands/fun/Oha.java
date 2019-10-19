package de.eldoria.shepard.contexts.commands.fun;

import de.eldoria.shepard.contexts.ContextCategory;
import de.eldoria.shepard.wrapper.MessageEventDataWrapper;
import de.eldoria.shepard.messagehandler.MessageSender;
import de.eldoria.shepard.contexts.commands.Command;

import java.util.Random;

public class Oha extends Command {

    /**
     * creates a new oha keyword object.
     */
    public Oha() {
        commandName = "oha";
        commandDesc = "Ohaaaaaa - Use \"ohad\" to delete your command afterwards.";
        commandAliases = new String[] {"ohad"};
        category = ContextCategory.FUN;
    }

    @Override
    protected void internalExecute(String label, String[] args, MessageEventDataWrapper messageContext) {
        String oha = "oha";
        Random rand = new Random();
        int loops = rand.nextInt(30) + 10;
        oha = oha + "a".repeat(loops);
        MessageSender.sendMessage(oha, messageContext.getChannel());

        if (label.equalsIgnoreCase("ohad")) {
            messageContext.getMessage().delete().queue();
        }
    }
}

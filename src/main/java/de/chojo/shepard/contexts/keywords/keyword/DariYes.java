package de.chojo.shepard.contexts.keywords.keyword;

import de.chojo.shepard.messagehandler.MessageSender;
import de.chojo.shepard.contexts.keywords.Keyword;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DariYes extends Keyword {

    /**
     * Creates a new dari yes keyword.
     */
    public DariYes() {
        keywords = new String[]{"nein", "no", "oder?", "nope"};
    }

    @Override
    public boolean execute(MessageReceivedEvent event, String key) {
        if (event.getAuthor().getId().equalsIgnoreCase("223192558468202496")) {
            MessageSender.sendMessage("Doch " + event.getAuthor().getAsMention(), event.getChannel());

        }
        return true;
    }

}

package de.eldoria.shepard.contexts.commands.fun;

import de.eldoria.shepard.contexts.ContextCategory;
import de.eldoria.shepard.contexts.commands.Command;
import de.eldoria.shepard.contexts.commands.CommandArg;
import de.eldoria.shepard.database.queries.GuessGameData;
import de.eldoria.shepard.database.types.GuessGameImage;
import de.eldoria.shepard.database.types.Rank;
import de.eldoria.shepard.messagehandler.ErrorType;
import de.eldoria.shepard.messagehandler.MessageSender;
import de.eldoria.shepard.minigames.ChannelEvaluator;
import de.eldoria.shepard.minigames.Evaluator;
import de.eldoria.shepard.minigames.guessgame.GuessGameEvaluator;
import de.eldoria.shepard.util.TextFormatting;
import de.eldoria.shepard.util.reactions.EmoteCollection;
import de.eldoria.shepard.wrapper.MessageEventDataWrapper;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

import static de.eldoria.shepard.util.Verifier.isArgument;
import static java.lang.System.lineSeparator;

public class GuessGame extends Command {

    private static final String TITLE = "NSFW or not! Guess now!";

    public GuessGame() {
        commandName = "guessGame";
        commandAliases = new String[] {"nsfwornot"};
        commandDesc = "Game where you have to guess if a cropped image is part of a hentai image or not.";
        commandArgs = new CommandArg[] {
                new CommandArg("action",
                        "Leave Empty to start a game." + lineSeparator()
                                + "**__s__core** -> Your score on this server" + lineSeparator()
                                + "**__g__lobal__s__core** -> Your global score." + lineSeparator()
                                + "**__t__op** -> The top 10 player on this server" + lineSeparator()
                                + "**__g__lobal__t__op** -> The top 10 player.", false)
        };
        category = ContextCategory.FUN;
    }

    @Override
    protected void internalExecute(String label, String[] args, MessageEventDataWrapper messageContext) {
        if (!isArgument(messageContext.getChannel().getName(),
                "guessgame", "guess-game", "nsfwornot", "nsfw-or-not")) {
            MessageSender.sendMessage("This is a minigame."
                            + "Minigame commands can only be executed in a minigame channel." + lineSeparator()
                            + "Please create a channel with one of the following names to play the game:"
                            + lineSeparator()
                            + "`guessgame`, `guess-game` for the **sfw version**" + lineSeparator()
                            + "`nsfwornot`, `nsfw-or-not` for the **nsfw version**",
                    messageContext.getChannel());
            return;
        }
        if (args.length == 0) {
            startGame(messageContext);
            return;
        }
        if (args.length != 1) {
            MessageSender.sendSimpleError(ErrorType.TOO_MANY_ARGUMENTS, messageContext.getChannel());
            return;
        }

        String cmd = args[0];

        if (isArgument(cmd, "score", "s")) {
            int userScore = GuessGameData.getUserScore(messageContext.getGuild(),
                    messageContext.getAuthor(), messageContext);
            MessageSender.sendMessage("Your score is: " + userScore, messageContext.getChannel());
            return;
        }

        if (isArgument(cmd, "globalScore", "gs")) {
            int userScore = GuessGameData.getGlobalUserScore(messageContext.getAuthor(), messageContext);
            MessageSender.sendMessage("Your global score is: " + userScore, messageContext.getChannel());
            return;
        }

        if (isArgument(cmd, "top", "t")) {
            sendTopScores(false, messageContext);
        }
        if (isArgument(cmd, "globalTop", "gt")) {
            sendTopScores(true, messageContext);
        }
    }

    private void sendTopScores(boolean global, MessageEventDataWrapper messageContext) {
        List<Rank> ranks = global
                ? GuessGameData.getGlobalTopScore(10, messageContext)
                : GuessGameData.getTopScore(messageContext.getGuild(), 10, messageContext);

        String rankTable = TextFormatting.getRankTable(ranks);

        MessageSender.sendMessage((global ? "**GLOBAL GUESS GAME RANKING**" : "**SERVER GUESS GAME RANKING**")
                        + lineSeparator() + rankTable,
                messageContext.getChannel());
    }

    private void startGame(MessageEventDataWrapper messageContext) {
        ChannelEvaluator<GuessGameEvaluator> channelEvaluator
                = Evaluator.getGuessGame();
        if (channelEvaluator.isEvaluationActive(messageContext.getTextChannel())) {
            MessageSender.sendMessage("One round is still in progress.", messageContext.getChannel());
            return;
        }

        GuessGameImage hentaiImage = GuessGameData.getHentaiImage(messageContext);
        if (hentaiImage == null) {
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(TITLE)
                .setDescription("Is this image part of an nsfw image or not?" + lineSeparator()
                        + "Click " + EmoteCollection.ANIM_CHECKMARK.getEmote().getAsMention()
                        + " for yes or " + EmoteCollection.ANIM_CROSS.getEmote().getAsMention()
                        + " for no!" + lineSeparator()
                        + "You have 30 seconds to guess!")
                .setImage(hentaiImage.getCroppedImage())
                .setFooter("Hint: Everything which isn't clearly NSFW is sfw!");

        messageContext.getChannel().sendMessage(builder.build())
                .queue(message -> {
                    message.addReaction(EmoteCollection.ANIM_CHECKMARK.getEmote()).queue();
                    message.addReaction(EmoteCollection.ANIM_CROSS.getEmote()).queue();
                    channelEvaluator.scheduleEvaluation(message, 30, new GuessGameEvaluator(message, hentaiImage));
                });
    }
}

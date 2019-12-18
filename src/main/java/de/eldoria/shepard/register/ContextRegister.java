package de.eldoria.shepard.register;

import de.eldoria.shepard.contexts.commands.admin.Changelog;
import de.eldoria.shepard.contexts.commands.admin.Greeting;
import de.eldoria.shepard.contexts.commands.admin.GuessGameConfig;
import de.eldoria.shepard.contexts.commands.admin.Invite;
import de.eldoria.shepard.contexts.commands.admin.Language;
import de.eldoria.shepard.contexts.commands.admin.ManageQuote;
import de.eldoria.shepard.contexts.commands.admin.Monitoring;
import de.eldoria.shepard.contexts.commands.admin.Permission;
import de.eldoria.shepard.contexts.commands.admin.Prefix;
import de.eldoria.shepard.contexts.commands.admin.RepeatCommand;
import de.eldoria.shepard.contexts.commands.admin.ShowKeyword;
import de.eldoria.shepard.contexts.commands.admin.Ticket;
import de.eldoria.shepard.contexts.commands.admin.TicketSettings;
import de.eldoria.shepard.contexts.commands.botconfig.BotPresence;
import de.eldoria.shepard.contexts.commands.botconfig.ContextInfo;
import de.eldoria.shepard.contexts.commands.botconfig.ManageContext;
import de.eldoria.shepard.contexts.commands.botconfig.ManageContextGuild;
import de.eldoria.shepard.contexts.commands.botconfig.ManageContextUsers;
import de.eldoria.shepard.contexts.commands.exclusive.PrivateAnswer;
import de.eldoria.shepard.contexts.commands.exclusive.SendPrivateMessage;
import de.eldoria.shepard.contexts.commands.fun.GuessGame;
import de.eldoria.shepard.contexts.commands.fun.KudoLottery;
import de.eldoria.shepard.contexts.commands.fun.Kudos;
import de.eldoria.shepard.contexts.commands.fun.LargeEmote;
import de.eldoria.shepard.contexts.commands.fun.MagicConch;
import de.eldoria.shepard.contexts.commands.fun.MockingSpongebob;
import de.eldoria.shepard.contexts.commands.fun.Oha;
import de.eldoria.shepard.contexts.commands.fun.Owo;
import de.eldoria.shepard.contexts.commands.fun.Quote;
import de.eldoria.shepard.contexts.commands.fun.RandomJoke;
import de.eldoria.shepard.contexts.commands.fun.Say;
import de.eldoria.shepard.contexts.commands.fun.Someone;
import de.eldoria.shepard.contexts.commands.fun.Uwu;
import de.eldoria.shepard.contexts.commands.util.Avatar;
import de.eldoria.shepard.contexts.commands.util.GetRaw;
import de.eldoria.shepard.contexts.commands.util.Help;
import de.eldoria.shepard.contexts.commands.util.HireMe;
import de.eldoria.shepard.contexts.commands.util.Home;
import de.eldoria.shepard.contexts.commands.util.Reminder;
import de.eldoria.shepard.contexts.commands.util.SystemInfo;
import de.eldoria.shepard.contexts.commands.util.Test;
import de.eldoria.shepard.contexts.commands.util.UserInfo;
import de.eldoria.shepard.contexts.commands.util.Vote;
import de.eldoria.shepard.localization.enums.commands.botconfig.Restart;
import de.eldoria.shepard.localization.enums.commands.botconfig.Upgrade;

public final class ContextRegister {

    private ContextRegister() {
    }

    /*private static void registerKeywords() {
        new AmIRight();
        new CommanderQuestion();
        new Communism();
        new DariNope();
        new DariYes();
        new Mlp();
        new Normandy();
        new Nudes();
        new Thing();
        new SomeoneKeyword();
    }*/

    private static void registerUtilCommands() {
        new Reminder();
        new UserInfo();
        new Avatar();
        new Help();
        new HireMe();
        new Vote();
        new Home();
        new SystemInfo();
        new GetRaw();
        new Test();
    }

    private static void registerFunCommands() {
        new MagicConch();
        new MockingSpongebob();
        new Oha();
        new Owo();
        new RandomJoke();
        new Say();
        new Uwu();
        new Quote();
        new Someone();
        new LargeEmote();
        new GuessGame();
        new Kudos();
        new KudoLottery();
    }

    private static void registerExclusiveCommands() {
        //new IsHaddeWorking();
        //new Meetings();
    }

    private static void registerBotConfigCommands() {
        new ContextInfo();
        new ManageContext();
        new ManageContextGuild();
        new ManageContextUsers();
        new Upgrade();
        new Restart();
    }

    private static void registerAdminCommands() {
        new Greeting();
        new Invite();
        new Prefix();
        new ShowKeyword();
        new TicketSettings();
        new Ticket();
        new Changelog();
        new ManageQuote();
        new Permission();
        new GuessGameConfig();
        new BotPresence();
        new PrivateAnswer();
        new SendPrivateMessage();
        new Monitoring();
        new Language();
        new RepeatCommand();
    }

    /**
     * Registers all contexts in command or keyword collection.
     */
    public static void registerContexts() {
        registerBotConfigCommands();
        registerAdminCommands();
        registerExclusiveCommands();
        registerUtilCommands();
        registerFunCommands();

        //registerKeywords();
    }
}

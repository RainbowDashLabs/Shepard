package de.eldoria.shepard;

import de.eldoria.shepard.basemodules.commanddispatching.CommandDispatchingModule;
import de.eldoria.shepard.basemodules.reactionactions.ReactionActionModule;
import de.eldoria.shepard.basemodules.standalone.StandaloneBaseModules;
import de.eldoria.shepard.commandmodules.SharedResources;
import de.eldoria.shepard.commandmodules.changelog.ChangelogModule;
import de.eldoria.shepard.commandmodules.commandsettings.CommandSettingsModule;
import de.eldoria.shepard.commandmodules.greeting.GreetingModule;
import de.eldoria.shepard.commandmodules.guessgame.GuessGameModule;
import de.eldoria.shepard.commandmodules.kudos.KudoModule;
import de.eldoria.shepard.commandmodules.language.LanguageModule;
import de.eldoria.shepard.commandmodules.monitoring.MonitoringModule;
import de.eldoria.shepard.commandmodules.prefix.PrefixModule;
import de.eldoria.shepard.commandmodules.presence.PresenceModule;
import de.eldoria.shepard.commandmodules.privatemessages.PrivateMessagesModule;
import de.eldoria.shepard.commandmodules.quote.QuoteModule;
import de.eldoria.shepard.commandmodules.reminder.ReminderModule;
import de.eldoria.shepard.commandmodules.repeatcommand.RepeatCommandModule;
import de.eldoria.shepard.commandmodules.standalone.StandaloneCommandsModule;
import de.eldoria.shepard.commandmodules.ticketsystem.TicketSystemModule;
import de.eldoria.shepard.core.CoreModule;
import de.eldoria.shepard.core.configuration.Config;
import de.eldoria.shepard.core.configuration.Loader;
import de.eldoria.shepard.modulebuilder.ModuleBuilder;
import de.eldoria.shepard.util.ExitCode;
import de.eldoria.shepard.webapi.ApiModule;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@Slf4j
public final class ShepardBot {
    private static JDA jda;
    private static ShepardBot instance;
    private SharedResources sharedResources;
    private Config config;
    private boolean loaded;

    private ShepardBot() {
    }


    /**
     * Main method.
     *
     * @param args Arguments.
     */
    public static void main(String[] args) {
        try {
            instance = new ShepardBot();

            instance.setup();

            instance.loaded = true;
        } catch (Exception e) {
            log.error("failed to start bot", e);
        }
    }

    /**
     * Checks if the bot is fully loaded.
     *
     * @return true if the bot instance is not null and {@link ShepardBot#loaded} is true.
     *      False if the bot is starting or going to shut down.
     */
    public static boolean isLoaded() {
        if (instance == null) {
            return false;
        }

        return instance.loaded;
    }

    private void setup() throws IOException {
        config = Loader.loadConfig();
        try {
            initiateJda();
        } catch (LoginException | InterruptedException e) {
            log.error(C.NOTIFY_ADMIN, "jda failed to log in", e);
        }
        sharedResources = SharedResources.build(this, jda, config);

        loadModules();


        log.info(C.STATUS, "Registered {} Commands,\nRegistered on {} Guilds!",
                sharedResources.getCommandHub().getCommands().size(),
                jda.getGuilds().size());

        log.info("Setup complete!");
    }

    private void initiateJda() throws LoginException, InterruptedException {
        jda = new JDABuilder(config.getGeneralSettings().getToken()).setMaxReconnectDelay(60).build();

        // optionally block until JDA is ready
        jda.awaitReady();

        log.info(C.STATUS, "JDA initialized");
    }

    /**
     * Registers listener at jda.
     *
     * @param listener List of listener.
     */
    public void registerListener(ListenerAdapter listener) {
        jda.addEventListener(listener);
    }

    /**
     * Close the shepard application.
     *
     * @param exitCode exit code do determine what should happen after shutdown
     *                 0 = shutdown
     *                 10 = restart
     */
    public void shutdown(ExitCode exitCode) {
        loaded = false;
        if (exitCode == ExitCode.SHUTDOWN) {
            log.info(C.STATUS, "shutting down");
        }
        if (exitCode == ExitCode.RESTART) {
            log.info(C.STATUS, "restarting");
        }

        if (jda != null) {
            jda.shutdown();
            log.info(C.STATUS, "JDA shut down. Closing Application in 2 Seconds!");
        }
        jda = null;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("Shutdown interrupted!");
        }

        System.exit(exitCode.code);
    }

    private void loadModules() {
        // Start base module without dependencies
        loadModule(new CoreModule(), new ReactionActionModule(), new StandaloneBaseModules());

        // Start command modules
        loadModule(new ChangelogModule(), new CommandSettingsModule(), new GreetingModule(), new GuessGameModule(),
                new KudoModule(), new LanguageModule(), new MonitoringModule(), new PrefixModule(),
                new PresenceModule(), new PrivateMessagesModule(), new QuoteModule(), new ReminderModule(),
                new RepeatCommandModule(), new TicketSystemModule(), new StandaloneCommandsModule());

        // start api services
        loadModule(new ApiModule());

        // start command dispatching. enables the bot to receive commands
        loadModule(new CommandDispatchingModule());
    }

    private void loadModule(ModuleBuilder... builders) {
        for (ModuleBuilder builder : builders) {
            builder.buildModule(sharedResources);
            log.debug("Loaded {}", builder.getClass().getSimpleName());
        }
    }
}
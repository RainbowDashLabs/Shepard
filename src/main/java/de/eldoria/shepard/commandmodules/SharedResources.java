package de.eldoria.shepard.commandmodules;

import de.eldoria.shepard.ShepardBot;
import de.eldoria.shepard.basemodules.commanddispatching.CommandHub;
import de.eldoria.shepard.basemodules.commanddispatching.CooldownManager;
import de.eldoria.shepard.basemodules.commanddispatching.util.ArgumentParser;
import de.eldoria.shepard.basemodules.commanddispatching.util.ExecutionValidator;
import de.eldoria.shepard.core.ShepardCollections;
import de.eldoria.shepard.core.configuration.Config;
import de.eldoria.shepard.database.ConnectionPool;
import de.eldoria.shepard.modulebuilder.ReqAssigner;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class SharedResources implements ReqAssigner {
    private ShepardBot shepardBot;
    private JDA jda;
    private Config config;
    private DataSource dataSource;
    private ConnectionPool connectionPool;
    private ShepardCollections collections;
    private CooldownManager cooldownManager;
    private ArgumentParser parser;
    private ExecutionValidator validator;
    private List<ListenerAdapter> listeners = new ArrayList<>();
    private CommandHub commandHub;

    private SharedResources(ShepardBot shepardBot, JDA jda, Config config) {
        this.shepardBot = shepardBot;
        this.jda = jda;
        this.config = config;
    }

    /**
     * Build a new shared resources object.
     * All Objects which are not delivered will be created by this method.
     *
     * @param shepardBot bot instance
     * @param jda        jda instance
     * @param config     config.
     * @return a new initialized shared resources object.
     */
    public static SharedResources build(ShepardBot shepardBot, JDA jda, Config config) {
        SharedResources sharedResources = new SharedResources(shepardBot, jda, config);
        sharedResources.init();
        return sharedResources;
    }

    private void init() {

        commandHub = new CommandHub();
        addAndInit(commandHub, this);

        collections = new ShepardCollections();
        addAndInit(collections, this);
        collections.init();

        addAndInit(this, collections.getPrivateMessages(), collections.getNormandy());

        connectionPool = new ConnectionPool();
        addAndInit(connectionPool, this);
        dataSource = connectionPool.getSource();

        cooldownManager = new CooldownManager();
        addAndInit(cooldownManager, this);

        parser = new ArgumentParser();
        addAndInit(parser, this);

        validator = new ExecutionValidator();
        addAndInit(validator, this);
    }

    /**
     * Add a listener to the listener list.
     *
     * @param listener listener to add.
     */
    public void addListener(ListenerAdapter listener) {
        listeners.add(listener);
    }
}
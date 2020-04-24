package de.eldoria.shepard.core.configuration;

import de.eldoria.shepard.core.configuration.configdata.Api;
import de.eldoria.shepard.core.configuration.configdata.BotList;
import de.eldoria.shepard.core.configuration.configdata.Database;
import de.eldoria.shepard.core.configuration.configdata.Presence;
import de.eldoria.shepard.core.configuration.configdata.Webhooks;
import lombok.Data;

/**
 * Class to deserialize the config.
 */
@Data
public class Config {
    /**
     * Bot API token.
     */
    private String token = null;
    /**
     * Beta state of bot.
     */
    private boolean beta = false;
    /**
     * Api configuration.
     */
    private Api api = null;
    /**
     * Botlist Configuration.
     */
    private BotList botlist = null;
    /**
     * Presence Configuration.
     */
    private Presence presence = null;
    /**
     * Database Configuration.
     */
    private Database database = null;
    /**
     * Default prefix.
     */
    private String prefix = null;

    /**
     * Webhook address.
     */
    private Webhooks webhooks = null;
}
package de.eldoria.shepard.scheduler;

import de.eldoria.shepard.ShepardBot;
import net.dv8tion.jda.api.entities.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PresenceChanger implements Runnable {
    private static PresenceChanger instance;
    private boolean customStatus;
    private ScheduledExecutorService executor;
    private List<Presence> presence;

    /**
     * Initializes the presence changer.
     */
    public static void initialize() {
        if (instance != null) return;

        instance = new PresenceChanger();

        instance.presence = new ArrayList<>();
        for (String message : ShepardBot.getConfig().getPresence().getPlaying()) {
            instance.presence.add(new Presence(PresenceState.PLAYING, message));
        }
        for (String message : ShepardBot.getConfig().getPresence().getListening()) {
            instance.presence.add(new Presence(PresenceState.LISTENING, message));
        }

        instance.executor = Executors.newSingleThreadScheduledExecutor();
        instance.executor.scheduleAtFixedRate(instance, 0, 5, TimeUnit.MINUTES);
    }

    /**
     * Get the instance of the presence changer.
     *
     * @return instance of presence changer
     */
    public static PresenceChanger getInstance() {
        initialize();
        return instance;
    }

    private void startScheduler() {
        if (executor.isShutdown() || executor.isTerminated()) {
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(instance, 0, 5, TimeUnit.MINUTES);
            customStatus = false;
        }
    }

    @Override
    public void run() {
        Presence presence = this.presence.get(Math.round((float) Math.random() * this.presence.size() - 1));
        switch (presence.state) {
            case PLAYING:
                ShepardBot.getJDA().getPresence().setActivity(Activity.playing(presence.message));
                break;
            case LISTENING:
                ShepardBot.getJDA().getPresence().setActivity(Activity.listening(presence.message));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + presence.state);
        }
    }

    /**
     * Set the status to playing.
     *
     * @param message playing message
     */
    public void setPlaying(String message) {
        ShepardBot.getJDA().getPresence().setActivity(Activity.playing(message));
        clearScheduler();
    }

    /**
     * Set the status to listening.
     *
     * @param message listening message
     */
    public void setListening(String message) {
        ShepardBot.getJDA().getPresence().setActivity(Activity.listening(message));
        clearScheduler();
    }

    /**
     * Set the status to streaming.
     *
     * @param message streaming message
     * @param url     twitch url
     */
    public void setStreaming(String message, String url) {
        ShepardBot.getJDA().getPresence().setActivity(Activity.streaming(message, url));
        clearScheduler();
    }

    /**
     * Clear the custom status and use default presence.
     */
    public void clearPresence() {
        ShepardBot.getJDA().getPresence().setActivity(null);
        startScheduler();
    }

    private void clearScheduler() {
        if (!customStatus) {
            executor.shutdown();
            System.out.println("Tasks canceled");
            customStatus = true;
        }
    }

    private enum PresenceState {
        PLAYING, LISTENING
    }

    private static final class Presence {
        private PresenceState state;
        private String message;

        private Presence(PresenceState state, String message) {
            this.state = state;
            this.message = message;
        }

        public PresenceState getState() {
            return state;
        }

        public String getMessage() {
            return message;
        }
    }
}
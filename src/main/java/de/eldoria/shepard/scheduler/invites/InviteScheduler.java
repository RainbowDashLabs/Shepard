package de.eldoria.shepard.scheduler.invites;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class InviteScheduler {

    private static InviteScheduler instance;

    private InviteScheduler() {
        ScheduledExecutorService autoRegister = Executors.newSingleThreadScheduledExecutor();

        autoRegister.scheduleAtFixedRate(new RegisterInvites(), 0, 10, TimeUnit.SECONDS);

        ScheduledExecutorService refreshInvites = Executors.newSingleThreadScheduledExecutor();

        refreshInvites.scheduleAtFixedRate(new RefreshInvites(), 0, 60, TimeUnit.MINUTES);
    }

    public static void initialize() {
        if (instance == null) {
            instance = new InviteScheduler();
        }
    }
}
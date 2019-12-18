package de.eldoria.shepard.scheduler;

import de.eldoria.shepard.database.queries.KudoData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KudoCounter implements Runnable {
    /**
     * Initializes the kudo counter if not active.
     */
    public void initialize() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this, 30, 60, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        KudoData.upcountKudos();
    }
}

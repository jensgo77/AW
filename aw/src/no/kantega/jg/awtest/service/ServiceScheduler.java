package no.kantega.jg.awtest.service;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;


/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 31.10.13
 * Time: 09:14
 */
public class ServiceScheduler {

        private final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        public void beepForAnHour() {
            final Runnable beeper = new Runnable() {
                    public void run() { Log.i("scheduletest", "Schedule task run"); }
                };
            final ScheduledFuture beeperHandle = scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);

            scheduler.schedule(new Runnable() {
                    public void run() { beeperHandle.cancel(true); }
                }, 60 * 60, SECONDS);
        }
}

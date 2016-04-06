package com.gunhansancar.eventbusexample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gunhansancar.eventbusexample.event.ServiceEvent;
import com.gunhansancar.eventbusexample.event.SimpleEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gunhansancar on 06/04/16.
 */
public class MyService extends Service {

    private Timer timer;
    private AtomicInteger counter = new AtomicInteger();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopTimer();
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(ServiceEvent event) {
        stopTimer();
        stopSelf();
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new SimpleTimerTask(counter), 0, 3000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private static class SimpleTimerTask extends TimerTask {

        private AtomicInteger current;

        public SimpleTimerTask(AtomicInteger current) {
            this.current = current;
        }

        @Override
        public void run() {
            EventBus.getDefault().postSticky(new SimpleEvent(current.addAndGet(1)));
        }
    }
}

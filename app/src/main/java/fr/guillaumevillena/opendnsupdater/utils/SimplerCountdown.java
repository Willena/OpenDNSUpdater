package fr.guillaumevillena.opendnsupdater.utils;

import android.os.CountDownTimer;

import fr.guillaumevillena.opendnsupdater.tasks.TaskFinished;

public abstract class SimplerCountdown extends CountDownTimer {
    private TaskFinished finishedListener;

    public SimplerCountdown() {
        super(1500, 500);
        super.start();
    }

    public SimplerCountdown(long durration) {
        super(durration, 500);

        super.start();
    }


    @Override
    public void onTick(long l) {

    }
}

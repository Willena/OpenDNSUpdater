package fr.guillaumevillena.opendnsupdater.tasks;

import android.os.AsyncTask;

public interface TaskFinished {
    void onTaskFinished(AsyncTask task, Boolean result);
}

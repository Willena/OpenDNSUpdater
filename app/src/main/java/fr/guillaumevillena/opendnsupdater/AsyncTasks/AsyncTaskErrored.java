package fr.guillaumevillena.opendnsupdater.AsyncTasks;

/**
 * Created by guill on 27/06/2018.
 */

public interface AsyncTaskErrored {
    void onError(Object... values);
}

package fr.guillaumevillena.opendnsupdater.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by guill on 29/06/2018.
 */

public abstract class BasicAsyncTask<E, P> extends AsyncTask<E, P, ResultItem> {


    private static final String TAG = BasicAsyncTask.class.getSimpleName();
    protected AsyncTaskFinished asyncTaskFinished;

    public void setFinishListener(AsyncTaskFinished listener){
        this.asyncTaskFinished = listener;
    }

    @Override
    protected void onPostExecute(ResultItem item) {

        Log.d(TAG, "onPostExecute: A post execute action !" + item);

        if (this.asyncTaskFinished != null)
            this.asyncTaskFinished.onFinished(item);

    }

}

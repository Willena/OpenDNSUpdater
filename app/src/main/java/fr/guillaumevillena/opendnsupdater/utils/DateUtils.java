package fr.guillaumevillena.opendnsupdater.utils;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    public static String getDate(Context ctx, long milliSeconds) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        Log.d(TAG, "getDate: " + calendar.getTime());
        return SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(calendar.getTime());
    }
}

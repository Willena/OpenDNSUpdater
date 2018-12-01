package fr.guillaumevillena.opendnsupdater.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Calendar;

public class DateUtils {

    public static String getDate(Context ctx, long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        java.text.DateFormat formatter = DateFormat.getDateFormat(ctx);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}

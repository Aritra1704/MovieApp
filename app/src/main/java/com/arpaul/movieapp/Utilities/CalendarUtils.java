package com.arpaul.movieapp.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ARPaul on 02-01-2016.
 */
public class CalendarUtils {

    private static final String DATE_FORMAT_WITH_COMMA = "dd MMM, yyyy";

    public static String getCommaFormattedDate(String date) {
        String reqDate = "";

        String str[] = date.split("-");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,StringUtils.getInt(str[2]));
        calendar.set(Calendar.MONTH,StringUtils.getInt(str[1]) - 1);
        calendar.set(Calendar.YEAR,StringUtils.getInt(str[0]));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_WITH_COMMA);
        reqDate = simpleDateFormat.format(calendar.getTime());

        return reqDate;
    }
}

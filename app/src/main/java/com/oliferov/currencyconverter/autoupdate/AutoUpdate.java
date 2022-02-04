package com.oliferov.currencyconverter.autoupdate;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AutoUpdate {

    public static final int TIME_AUTO_UPDATE = 86400;

    private long getTimeHasPassed(String date) {
        ZoneId zoneId = ZoneId.of("+03");
        String dateTimeInfo = date.substring(0, 10) + " " + date.substring(11, 19);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeInfo, dateTimeFormatter);
        LocalDateTime dateTimeCurrent = LocalDateTime.now(zoneId);
        Instant instantCurrent = dateTimeCurrent.atZone(zoneId).toInstant();
        Instant instant = dateTime.atZone(zoneId).toInstant();
        return instantCurrent.getEpochSecond() - instant.getEpochSecond();
    }

    public long isAutoUpdate(String date) {
        return getTimeHasPassed(date);
    }

}

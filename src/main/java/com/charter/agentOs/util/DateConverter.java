package com.charter.agentOs.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    public long epocStringToDateConverter(Date d){

        String date = d.toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime dateTime = LocalDateTime.parse(date,formatter);

        long epochTime;
        epochTime = dateTime.toEpochSecond(ZoneOffset.UTC);

        return epochTime;

    }
}

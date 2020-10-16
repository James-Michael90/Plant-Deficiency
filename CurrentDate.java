package com.visione.amndd.utils;

import java.text.DateFormat;
import java.util.Date;

public class CurrentDate {
    public static String getDate(Date date){
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
    }
}

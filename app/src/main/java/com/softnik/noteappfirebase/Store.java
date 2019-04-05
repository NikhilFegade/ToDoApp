package com.softnik.noteappfirebase;

import java.text.SimpleDateFormat;

public class Store {

    public static String getReadableDate(Long value){
        return  new SimpleDateFormat("EEE dd, MM - YYY 'at' hh:mm a").format(value);

    }
}

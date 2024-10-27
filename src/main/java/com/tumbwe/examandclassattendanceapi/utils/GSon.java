package com.tumbwe.examandclassattendanceapi.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalTime;

public class GSon {

    public static Gson getGson(){
        return new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
    }
}

package com.example.yulia.forecast.utils;


import android.util.Log;

public class AppLogger {

    private static String TAG = "Framework";
    private static String MSG = "HERE";
    private static boolean isLoggerOn = true;

    private static String formatMessage(String name, String message) {
        return name + " -> " + message;
    }

    private static String formatName(Class<?> cl) {
        return cl.getSimpleName();
    }

    public static void LogCut() {
        LogCut(TAG, MSG);
    }

    public static void LogCut(Class<?> cl) {
        LogCut(cl, MSG);
    }

    public static void LogCut(String message) {
        LogCut(TAG, message);
    }

    public static void LogCut(Class<?> cl, String message) {
        LogCut(formatName(cl), message);
    }

    public static void LogException(String clsName, String message, Throwable e) {
//        Analytics.captureHandledException(clsName, message + ": " + e.getMessage(), e);
        LogCut(clsName, message + ": " + e.getMessage());
    }

    public static void LogCut(String clsName, String message) {
//        if (Program.debug && isLoggerOn)
            Log.e(TAG, formatMessage(clsName, message));
    }
}
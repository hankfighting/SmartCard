package com.hank.oma.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author hank
 * 2019-10-29
 */
public final class LogUtil {
    public static final int LEVEL_LOG_I = 0x01;
    public static final int LEVEL_LOG_D = 0x02;
    public static final int LEVEL_LOG_E = 0x03;
    public static final int LEVEL_LOG_V = 0x04;
    public static final int LEVEL_LOG_W = 0x05;

    private static final String DEFAULT_TAG = "LogUtil";
    private static boolean isLog = true;

    private LogUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static boolean isLog() {
        return isLog;
    }

    public static void setLog(boolean isLog) {
        LogUtil.isLog = isLog;
    }

    public static void d(String tag, String msg) {
        if (!isLog || TextUtils.isEmpty(msg)) return;
        Log.d(tag, msg);
    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (!isLog || TextUtils.isEmpty(msg)) return;
        Log.v(tag, msg);
    }

    public static void v(String msg) {
        v(DEFAULT_TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (!isLog || TextUtils.isEmpty(msg)) return;
        Log.w(tag, msg);
    }

    public static void w(String msg) {
        w(DEFAULT_TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (!isLog || TextUtils.isEmpty(msg)) return;
        Log.i(tag, msg);
    }

    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (!isLog || TextUtils.isEmpty(msg)) return;
        Log.e(tag, msg);
    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }

    /**
     * @param tag 标签
     * @param msg msg
     * @author hank
     * @date 2018/9/5 11:58
     * @descrption 这里使用自己分节的方式来输出足够长度的 message
     */
    public static void dTAGLongInfo(String tag, String msg) {
        if (!isLog || TextUtils.isEmpty(msg)) return;
        msg = msg.trim();
        int index = 0;
        int maxLength = 3500;
        String sub;
        while (index < msg.length()) {
            if (msg.length() <= index + maxLength) {
                sub = msg.substring(index);
            } else {
                sub = msg.substring(index, index + maxLength);
            }

            index += maxLength;
            d(tag, sub.trim());
        }
    }

    public static void dLongInfo(String msg) {
        dTAGLongInfo(DEFAULT_TAG, msg);
    }


}

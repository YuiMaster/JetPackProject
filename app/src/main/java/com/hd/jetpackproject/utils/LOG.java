package com.hd.jetpackproject.utils;

import android.content.Context;
import android.util.Log;


import com.hd.jetpackproject.BuildConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author liaoyuhuan
 * @name ${PROJECT_NAME}
 * @class
 * @time 2018/4/11  11:00
 * @description
 */
public class LOG {
    private LOG() {
    }

    private static final String TAG = "hd/";

    public static File mLogFile = null;
    /**
     * false 调试
     * true 不调试
     */
    private static final boolean disableDebug = !BuildConfig.DEBUG;

    public static void init(@NotNull Context context) {
        if (mLogFile != null) {
            return;
        }
        try {
            File folderFile = new File(context.getExternalCacheDir().getAbsolutePath() + File.separator + "HdLog");
            mLogFile = new File(folderFile.getAbsolutePath() + File.separator + "LOG" + getDateToString(System.currentTimeMillis()) + ".txt");
            folderFile.mkdirs();
        } catch (Exception e) {
            mLogFile = null;
        }
    }

    public static int v(@NotNull String tag, @NotNull String data) {
        if (disableDebug) {
            return 0;
        }
        return Log.v(TAG + tag, data);
    }

    public static int V(@NotNull String tag, @NotNull String data) {
        return Log.v(TAG + tag, data);
    }

    public static int i(@NotNull String tag, @NotNull String data) {
        if (disableDebug) {
            return 0;
        }

        return Log.i(TAG + tag, data);
    }

    public static int d(@NotNull String tag, @NotNull String data) {
        if (disableDebug) {
            return 0;
        }
        return Log.d(TAG + tag, data);
    }

    public static int D(@NotNull String tag, @NotNull String data) {
        return Log.d(TAG + tag, data);
    }

    public static int w(@NotNull String tag, @NotNull String data) {
        if (disableDebug) {
            return 0;
        }
        return Log.w(TAG + tag, data);
    }

    public static int W(@NotNull String tag, @NotNull String data) {
        return Log.w(TAG + tag, data);
    }

    public static int e(@NotNull String tag, @NotNull String data) {
        if (disableDebug) {
            return 0;
        }
        return Log.e(TAG + tag, data);
    }

    public static int E(@NotNull String tag, @NotNull String data) {
        return Log.e(TAG + tag, data);
    }

    /**
     * 打印线程信息
     *
     * @param data 打印信息
     * @return
     */
    public static int printThreadInfo(@Nullable String data) {
        if (disableDebug) {
            return 0;
        }
        return Log.d("hd/thread", "" + data + " " + getThreadName());
    }

    public static void f(String tag, String text) {
        if (disableDebug) {
            return;
        }
        F(tag, text);
    }

    public static void F(String tag, String text) {
        if (mLogFile == null) {
            e(tag, text);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(mLogFile, true)) {
            try (BufferedWriter buf = new BufferedWriter(fileWriter)) {
                buf.append(tag);
                buf.append(getDateToString(System.currentTimeMillis()));
                buf.newLine();
                buf.append(text);
                buf.newLine();
            }
        } catch (IOException e) {
            // do nothing
        }
    }

    private static String getDateToString(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss.SSS");
        Date date2 = new Date(time);
        return sdr.format(date2);
    }

    /**
     * 取得线程名
     *
     * @return
     */
    @NotNull
    private static String getThreadName() {
        return Thread.currentThread().getName();
    }


    public static int t(@NotNull String tag, @NotNull String data) {
        if (disableDebug) {
            return 0;
        }
        return Log.d(TAG + tag, "时间 " + getDateToString(System.currentTimeMillis()) + data);
    }
}

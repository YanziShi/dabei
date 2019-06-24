package com.qckj.dabei.util.log;

import android.util.Log;

import com.qckj.dabei.BuildConfig;

/**
 * 日志管理类
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class Logger {

    private String tag;

    public Logger(String tag) {
        this.tag = tag;
    }

    public void d(String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg;
            if (args.length == 0) {
                msg = format;
            } else {
                msg = String.format(format, args);
            }
            Log.d(tag, msg);
        }
    }

    public void e(String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg;
            if (args.length == 0) {
                msg = format;
            } else {
                msg = String.format(format, args);
            }
            Log.e(tag, msg);
        }
    }

    public void i(String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg;
            if (args.length == 0) {
                msg = format;
            } else {
                msg = String.format(format, args);
            }
            Log.i(tag, msg);
        }
    }

    public void w(String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg;
            if (args.length == 0) {
                msg = format;
            } else {
                msg = String.format(format, args);
            }
            Log.w(tag, msg);
        }
    }

}

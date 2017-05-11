package com.king.rongcloud.util;

import android.util.Log;

public class LogUtil {
    /**
     * 根据打印日志标识，判断是否打印日志 </p>
     * <p/>
     * 级别：verbose
     *
     * @param TAG
     * @param msg
     * @param t
     */
    /**
     * try catch 捕获日志
     */
    public static final String COMMCATCH = "catch";
    public static void v(String TAG, String msg, Throwable t) {
        if (msg != null) {
            if (true) {
                Log.v(TAG, msg, t);
            }
        }
    }
    /**
     * 根据打印日志标识，判断是否打印日志</p>
     * <p/>
     * 级别：verbose
     *
     * @param TAG
     * @param msg
     */
    public static void v(String TAG, String msg) {
        if (msg != null) {
            if (true) {
                Log.v(TAG, msg);
            }
        }
    }
    /**
     * 根据打印日志标识，判断是否打印日志 </p>
     * <p/>
     * 级别：debug
     *
     * @param TAG
     * @param msg
     * @param t
     */
    public static void d(String TAG, String msg, Throwable t) {
        if (msg != null) {
            if (true) {
                Log.d(TAG, msg, t);
            }
        }
    }

    /**
     * 根据打印日志标识，判断是否打印日志</p>
     * <p/>
     * 级别：debug
     *
     * @param TAG
     * @param msg
     */
    public static void d(String TAG, String msg) {
        if (msg != null) {
            if (true) {
                Log.d(TAG, msg);
            }
        }
    }

    /**
     * 根据打印日志标识，判断是否打印日志</p>
     * <p/>
     * 级别：info
     *
     * @param TAG
     * @param msg
     */
    public static void i(String TAG, String msg) {
        if (msg != null) {
            if (true) {
                Log.i(TAG, msg);
            }
        }
    }

    /**
     * 根据打印日志标识，判断是否打印日志</p>
     * <p/>
     * 级别：info
     *
     * @param TAG
     * @param msg
     * @param t
     */
    public static void i(String TAG, String msg, Throwable t) {
        if (msg != null) {
            if (true) {
                Log.i(TAG, msg, t);
            }
        }
    }

    /**
     * 根据打印日志标识，判断是否打印日志</p>
     * <p/>
     * 级别：warn
     *
     * @param TAG
     * @param msg
     */
    public static void w(String TAG, String msg) {
        if (msg != null) {
            if (true) {
                Log.w(TAG, msg);
            }
        }
    }

    /**
     * 根据打印日志标识，判断是否打印日志</p>
     * <p/>
     * 级别：warn
     *
     * @param TAG
     * @param msg
     * @param t
     */
    public static void w(String TAG, String msg, Throwable t) {
        if (msg != null) {
            if (true) {
                Log.w(TAG, msg, t);
            }
        }
    }

    /**
     * 根据打印日志标识，判断是否打印日志</p>
     * <p/>
     * 级别：error
     *
     * @param TAG
     * @param msg
     */
    public static void e(String TAG, String msg) {
        if (msg != null) {
            if (true) {
                Log.e(TAG, msg);
            }
        }
    }

    /**
     * 根据打印日志标识，判断是否打印日志</p>
     * <p/>
     * 级别：error
     *
     * @param TAG
     * @param msg
     * @param t
     */
    public static void e(String TAG, String msg, Throwable t) {
        if (msg != null) {
            if (true) {
                Log.e(TAG, msg, t);
            }
        }
    }
    /**
     * 专用做打印异常信息的日志
     * <p/>
     * 级别：error
     *
     * @param e
     */
    public static void exceptionPrint(Exception e) {
        if (e != null) {
            if (true) {
                LogUtil.e(COMMCATCH, "Exception>>  " + e.getMessage(), e);
            }
        }
    }
}

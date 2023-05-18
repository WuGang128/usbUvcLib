package com.jiangdg.utils

import android.util.Log

const val maxLength = 200

object UvcLog {
    private var isDebug = false
    fun setDebug(debug: Boolean) {
        isDebug = debug
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
//            LogUtil.writerLog(tag,msg)
        }
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }

    @JvmStatic
    fun e(tag: String, msg: String, throwable: Throwable) {
        if (isDebug) {
            Log.e(tag, msg, throwable)
        }
    }

    @JvmStatic
    fun w(tag: String, msg: Throwable) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }

    @JvmStatic
    fun w(tag: String, msg: String, throwable: Throwable) {
        if (isDebug) {
            Log.w(tag, msg, throwable)
        }
    }

    @JvmStatic
    fun w(tag: String, msg: String) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }

    @JvmStatic
    fun v(tag: String, msg: String) {
        if (isDebug) {
            Log.v(tag, msg)
        }
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (isDebug) {
            Log.i(tag, msg)
        }
    }
}
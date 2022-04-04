package com.haljaa200.groceriesh.util

import android.util.Log
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class Vlog {
    private val callerClassAndLine: String
        get() {
            val stElements = Thread.currentThread().stackTrace
            for (i in 1 until stElements.size) {
                val ste = stElements[i]
                if (ste.className != Log::class.java.name && ste.className.indexOf("java.lang.Thread") != 0) {
                    return String.format(" (%s:%s)", ste.fileName, ste.lineNumber)
                }
            }
            return ""
        }

    private val callerMethod: String
        get() {
            val stElements = Thread.currentThread().stackTrace
            for (i in 1 until stElements.size) {
                val ste = stElements[i]
                if (ste.className != Log::class.java.name && ste.className.indexOf("java.lang.Thread") != 0) {
                    return ste.methodName
                }
            }
            return ""
        }

    private val currentTime: String
        get() {
            val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
            return sdf.format(Date())
        }

    private fun initTagString(tag: String): String {
        val logTag = StringBuilder("$currentTime • ")

        if (tag.isEmpty())
            logTag.append("$callerClassAndLine - $callerMethod")
        else
            logTag.append("$tag:$callerClassAndLine")
        return logTag.toString()
    }

    companion object {
        fun i(message: String, tag: String = "") {
            val logTag = Vlog().initTagString(tag)

            Log.i(logTag, message)
        }

        fun w(message: String, tag: String = "") {
            val logTag = Vlog().initTagString(tag)

            Log.w(logTag, message)
        }

        fun e(message: String, tag: String = "") {
            val logTag = Vlog().initTagString(tag)

            Log.e(logTag, message)
        }

        fun d(message: String, tag: String = "") {
            val logTag = Vlog().initTagString(tag)

            Log.d(logTag, message)
        }

        fun firebase(message: String, tag: String = "") {
            val logTag = Vlog().initTagString(tag)
            //TODO log to firebase
        }

        fun api(message: String, tag: String = "") {
            val logTag = Vlog().initTagString(tag)
            //TODO log to server api
        }
    }
}
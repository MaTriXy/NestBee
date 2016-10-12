package com.mhw.nestbee.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.mhw.nestbee.App
import com.mhw.nestbee.NewMeetingActivity
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by yarolegovich on 07.10.2016.
 */

const val LOG_ON = true

fun Any.logD(vararg msg: Any) {
    if (LOG_ON) {
        Log.d(javaClass.simpleName, TextUtils.join(",", msg.map(Any::toString)))
    }
}

fun Any.logE(msg: String) {
    if (LOG_ON) {
        Log.e(javaClass.simpleName, msg)
    }
}

fun Any.logE(msg: String, e: Throwable) {
    if (LOG_ON) {
        Log.e(javaClass.simpleName, msg, e)
    }
}

fun Context.message(msg: String) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun JSONObject.toRequestBody() = RequestBody.create(MediaType.parse("application/json"), this.toString())

fun List<Int>.toJsonArray(): JSONArray {
    val result = JSONArray()
    forEach { result.put(it) }
    return result
}



fun start(token: Class<out Activity>) {
    val intent = Intent(App.instance, token)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    App.instance.startActivity(intent)
}

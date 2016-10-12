package com.mhw.nestbee.util

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.mhw.nestbee.App
import com.mhw.nestbee.NewMeetingActivity
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.model.Meeting
import com.mhw.nestbee.model.UserInterest
import com.pusher.android.PusherAndroid
import com.pusher.android.PusherAndroidOptions
import com.pusher.android.notifications.PushNotificationRegistration
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by yarolegovich on 09.10.2016.
 */
object Pushes {

    fun channelName(id: Int) = "my_channel_$id"

    fun subscribe(channel: String) {
        val pusher = registration()
        pusher.subscribe(channel)
        pusher.setFCMListener { msg ->
            logD(msg.data)
            if (!msg.data.containsKey("tags")) return@setFCMListener
            Handler(Looper.getMainLooper()).post {
                val d = msg.data
                val users = JSONArray(d["users"])
                val avatars = (0..users.length() - 1).map { users.getString(it) }
                val tags = JSONArray(d["tags"])
                val interests = (0..tags.length() - 1).map { tags.getJSONObject(it) }
                        .map { UserInterest(it["group_id"] as Int, it["tag"] as String) }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
                val meeting = Meeting(
                        d["id"]!!.toInt(),
                        d["location_id"]!!.toInt(),
                        dateFormat.parse(d["date"]!!),
                        d["password"]!!,
                        interests,
                        avatars)
                logD(meeting)
                UserData.saveMeeting(meeting)
                start(NewMeetingActivity::class.java)
            }
        }
    }

    fun unsubscribe(channel: String) {
        registration().unsubscribe(channel)
    }

    private fun registration(): PushNotificationRegistration {
        val pusher = createPusher()
        val nativePusher = pusher.nativePusher()
        nativePusher.registerFCM(App.instance);
        pusher.connect()
        return nativePusher
    }

    private fun createPusher(): PusherAndroid {
        val pusherOptions = PusherAndroidOptions()
        pusherOptions.setCluster(App.PUSHER_CLUSTER)
        val pusher = PusherAndroid(App.PUSHER_APP_KEY, pusherOptions)
        return pusher
    }
}
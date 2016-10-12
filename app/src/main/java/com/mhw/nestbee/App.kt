package com.mhw.nestbee

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.facebook.FacebookSdk
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.model.Meeting
import com.mhw.nestbee.model.UserInterest
import com.mhw.nestbee.util.Pushes
import com.mhw.nestbee.util.logD
import com.pusher.android.PusherAndroid
import com.pusher.android.PusherAndroidOptions
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.vk.sdk.VKSdk
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by yarolegovich on 07.10.2016.
 */
class App : MultiDexApplication() {

    companion object {

        const val PUSHER_APP_KEY = "f8559f1f94c6b06f361e"
        const val PUSHER_CLUSTER = "eu"

        var instance by Delegates.notNull<App>()
    }

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        FacebookSdk.sdkInitialize(this)
        instance = this

        if (UserData.isLoggedIn()) {
            Pushes.subscribe(Pushes.channelName(UserData.getUserId()))
        }
    }
}
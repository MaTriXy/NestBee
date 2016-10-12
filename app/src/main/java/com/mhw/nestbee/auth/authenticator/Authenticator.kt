package com.mhw.nestbee.auth.authenticator

import android.app.Activity
import android.content.Intent
import com.mhw.nestbee.auth.authenticator.UserCallback

/**
 * Created by yarolegovich on 20.03.2016.
 */
interface Authenticator {

    companion object {
        const val VK = "vk"
        const val FACEBOOK = "facebook"

        fun create(type: String) = when (type) {
            VK -> VkAuthenticator()
            FACEBOOK -> FacebookAuthenticator()
            else -> throw IllegalArgumentException("unknown type")
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun setCallback(callback: UserCallback)
    fun doLogin(activity: Activity)
}
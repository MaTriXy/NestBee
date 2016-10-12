package com.mhw.nestbee.auth.authenticator

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.mhw.nestbee.model.User
import com.mhw.nestbee.util.logD
import com.mhw.nestbee.util.logE
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiUser
import com.vk.sdk.api.model.VKList

/**
 * Created by yarolegovich on 07.10.2016.
 */
class VkAuthenticator() : Authenticator {

    private var callback: UserCallback? = null

    override fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                VKApi.users().get(VKParameters.from(
                        VKApiConst.FIELDS,
                        "photo_200,first_name,last_name")
                ).executeWithListener(object : VKRequest.VKRequestListener() {
                    override fun onComplete(response: VKResponse) {
                        val user = (response.parsedModel as VKList<VKApiUser>)[0]
                        logD("id, token = " + res.userId, res.accessToken)
                        logD(user)
                        callback?.onUserLogged(User(
                                res.email,
                                "${user.first_name} ${user.last_name}",
                                user.photo_200,
                                res.userId,
                                res.accessToken))
                    }
                })
            }

            override fun onError(error: VKError) {
                if (error.errorMessage != null) {
                    callback?.onError(error.errorMessage)
                    logE(error.errorMessage, error.httpError)
                }
            }

        })
    }

    override fun setCallback(callback: UserCallback) {
        this.callback = callback
    }

    override fun doLogin(activity: Activity) {
        VKSdk.login(activity, *getScope())
    }

    private fun getScope() = arrayOf("email,audio,photos")
}
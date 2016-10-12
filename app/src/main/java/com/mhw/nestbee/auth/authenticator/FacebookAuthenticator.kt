package com.mhw.nestbee.auth.authenticator

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.mhw.nestbee.model.User
import com.mhw.nestbee.util.logD
import com.mhw.nestbee.util.logE
import com.mhw.nestbee.auth.authenticator.Authenticator
import com.mhw.nestbee.auth.authenticator.UserCallback
import org.json.JSONObject
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by yarolegovich on 19.03.2016.
 */
class FacebookAuthenticator() : Authenticator, FacebookCallback<LoginResult> {

    private var callbackManager: CallbackManager? = null
    private var callback: UserCallback? = null

    override fun doLogin(activity: Activity) {
        callbackManager = com.facebook.CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, this)
        LoginManager.getInstance().logInWithReadPermissions(activity, getScope())
    }

    override fun setCallback(callback: UserCallback) {
        this.callback = callback
    }

    override fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSuccess(loginResult: LoginResult) {
        val parameters = Bundle()
        parameters.putString("fields", "name,last_name,email,picture")
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { jObj, graphResponse ->
            if (jObj != null) {
                logD(jObj)
                callback?.onUserLogged(User(
                        jObj["email"] as String,
                        jObj["name"] as String,
                        ((jObj["picture"] as JSONObject)["data"] as JSONObject)["url"] as String))
            }
        }
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onCancel() {
        callback?.onError("Request cancelled")
        logE("onCancel facebook")
    }

    override fun onError(e: FacebookException) {
        callback?.onError(e.message!!)
        logE("onError facebook", e)
    }

    private fun getScope() = Arrays.asList("public_profile", "email")

}
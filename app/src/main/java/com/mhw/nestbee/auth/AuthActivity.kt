package com.mhw.nestbee.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mhw.nestbee.App
import com.mhw.nestbee.HomeActivity
import com.mhw.nestbee.R
import com.mhw.nestbee.SurveyActivity
import com.mhw.nestbee.auth.authenticator.Authenticator
import com.mhw.nestbee.auth.authenticator.UserCallback
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.model.User
import com.mhw.nestbee.model.UserSurveyTag
import com.mhw.nestbee.net.NetworkManager
import com.mhw.nestbee.util.Callback
import com.mhw.nestbee.util.Pushes
import com.mhw.nestbee.util.logD
import com.pusher.android.PusherAndroid
import com.pusher.android.PusherAndroidOptions
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity(), UserCallback, View.OnClickListener {

    private val vkAuthenticator by lazy {
        Authenticator.create(Authenticator.VK)
    }
    private val facebookAuthenticator by lazy {
        Authenticator.create(Authenticator.FACEBOOK)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (UserData.isLoggedIn()) {
            proceedNext(UserData.hadPassedSurvey())
        }

        vkAuthenticator.setCallback(this)
        facebookAuthenticator.setCallback(this)

        background.setImageResource(R.drawable.nestbee_login)
        signInFacebook.clickable().setOnClickListener(this)
        signInVk.clickable().setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = (view.parent as View).id
        when (id) {
            R.id.signInFacebook -> facebookAuthenticator.doLogin(this)
            R.id.signInVk -> vkAuthenticator.doLogin(this)
        }
    }

    override fun onUserLogged(user: User) {
        NetworkManager.logIn(user, Callback({ loginResult ->
            Pushes.subscribe(Pushes.channelName(loginResult.id))
            Log.d("VKTOKEN", user.accessToken ?: "LOL")
            UserData.logIn(loginResult, user)
            proceedNext(loginResult.hasTags)
        }, { onError(it) }))
    }


    override fun onError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun proceedNext(hasTags: Boolean) {
        val intent = if (hasTags) {
            Intent(this, HomeActivity::class.java)
        } else Intent(this, SurveyActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookAuthenticator.handleActivityResult(requestCode, resultCode, data)
        vkAuthenticator.handleActivityResult(requestCode, resultCode, data)
    }
}

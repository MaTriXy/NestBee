package com.mhw.nestbee.auth.authenticator

import com.mhw.nestbee.model.User


/**
 * Created by yarolegovich on 19.03.2016.
 */
interface UserCallback {
    fun onUserLogged(user: User)
    fun onError(message: String)
}
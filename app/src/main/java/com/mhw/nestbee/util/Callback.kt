package com.mhw.nestbee.util

import com.mhw.nestbee.net.NetworkManager

/**
 * Created by yarolegovich on 08.10.2016.
 */
class Callback<T>(val doOnSuccess: (T) -> Unit, val doOnError: (String) -> Unit) : NetworkManager.Callback<T> {
    override fun onError(msg: String) = doOnError(msg)

    override fun onSuccess(result: T)= doOnSuccess(result)
}
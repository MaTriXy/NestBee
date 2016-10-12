package com.mhw.nestbee.model

/**
 * Created by yarolegovich on 07.10.2016.
 */
class User {

    val email: String
    val name: String
    val image: String
    val userId: String?
    val accessToken: String?

    constructor(email: String, name: String, image: String) : this(email, name, image, null, null)

    constructor(email: String, name: String, image: String, userId: String?, accessToken: String?) {
        this.email = email
        this.name = name
        this.image = image
        this.userId = userId
        this.accessToken = accessToken
    }

    fun isVkUser() = userId != null && accessToken != null

}
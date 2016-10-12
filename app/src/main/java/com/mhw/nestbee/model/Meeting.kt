package com.mhw.nestbee.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by yarolegovich on 09.10.2016.
 */
data class Meeting(
        val id:  Int,
        val locationId: Int,
        val date: Date,
        val password: String,
        val interests: List<UserInterest>,
        val attendees: List<String>)
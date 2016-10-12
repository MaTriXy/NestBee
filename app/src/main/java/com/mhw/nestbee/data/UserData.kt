package com.mhw.nestbee.data

import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mhw.nestbee.App
import com.mhw.nestbee.model.LoginResult
import com.mhw.nestbee.model.Meeting
import com.mhw.nestbee.model.User
import com.mhw.nestbee.model.UserInterest
import com.mhw.nestbee.util.Pushes
import com.pusher.android.PusherAndroid
import com.vk.sdk.VKSdk
import java.util.*

/**
 * Created by yarolegovich on 08.10.2016.
 */
object UserData {

    private const val STORAGE = "storage"

    private const val USER_NAME = "username"
    private const val USER_ID = "userId"
    private const val PASSED_SURVEY = "passed_survey"
    private const val USER_INTERESTS = "interests"
    private const val IS_SEARCH_ACTIVE = "search_state"
    private const val USER_PHOTO = "photo"
    private const val MEETING = "match_found"
    private const val MEETING_ACCEPTED = "match_accepted"
    private const val SELECTED_MUSIC = "music"

    private val storage = App.instance.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)

    fun hadPassedSurvey() = storage.getBoolean(PASSED_SURVEY, false)

    fun isLoggedIn() = storage.getString(USER_NAME, null) != null

    fun getUserId() = storage.getInt(USER_ID, -1)

    fun getUsername() = storage.getString(USER_NAME, "anonymous")

    fun isSearchActive() = storage.getBoolean(IS_SEARCH_ACTIVE, false)

    fun hasMeeting() = storage.getString(MEETING, "") != ""

    fun getSelectedMusic(): List<Int> = storage.getStringSet(SELECTED_MUSIC, emptySet()).map { it.toInt() }

    fun saveSelectedMusic(ids: List<Int>) {
        storage.edit().putStringSet(SELECTED_MUSIC, ids.map(Int::toString).toSet()).apply()
    }

    fun saveMeeting(meeting: Meeting) {
        storage.edit().putString(MEETING, Gson().toJson(meeting)).apply()
    }

    fun rejectMeeting() {
        storage.edit().remove(MEETING).apply()
    }

    fun isMeetingAccepted() = storage.getBoolean(MEETING_ACCEPTED, false)

    fun acceptMeeting() {
        storage.edit().putBoolean(MEETING_ACCEPTED, true).apply()
    }

    fun getMeeting(): Meeting {
        if (!hasMeeting()) {
            throw IllegalStateException()
        }
        return Gson().fromJson(storage.getString(MEETING, ""), Meeting::class.java)
    }

    fun getUserInterests(): List<UserInterest> {
        val json = storage.getString(USER_INTERESTS, null)
        return if (json != null) {
            Gson().fromJson(json, object : TypeToken<ArrayList<UserInterest>>() {}.type)
        } else emptyList<UserInterest>()
    }

    fun setSearchState(isActive: Boolean) {
        storage.edit().putBoolean(IS_SEARCH_ACTIVE, isActive).commit()
    }

    fun markSurveyPassed() {
        storage.edit().putBoolean(PASSED_SURVEY, true).commit()
    }

    fun getUserImage() = storage.getString(USER_PHOTO, "")

    fun logIn(loginResult: LoginResult, user: User) {
        storage.edit().putInt(USER_ID, loginResult.id)
                .putString(USER_NAME, user.name)
                .putString(USER_PHOTO, user.image)
                .putBoolean(PASSED_SURVEY, loginResult.hasTags)
                .commit()
    }

    fun saveUserInterests(interests: List<UserInterest>) {
        val json = Gson().toJson(ArrayList(interests))
        storage.edit().putString(USER_INTERESTS, json).apply()
    }

    fun logOut() {
        VKSdk.logout()
        LoginManager.getInstance().logOut()
        Pushes.unsubscribe(Pushes.channelName(getUserId()))
        storage.edit().clear().apply()
    }
}
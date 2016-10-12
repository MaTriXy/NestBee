package com.mhw.nestbee.net

import android.os.Handler
import android.os.Looper
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.model.*
import com.mhw.nestbee.util.logD
import com.mhw.nestbee.util.logE
import com.mhw.nestbee.util.toJsonArray
import com.mhw.nestbee.util.toRequestBody
import okhttp3.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.Method

/**
 * Created by yarolegovich on 08.10.2016.
 */


object NetworkManager {

    private const val BASE = "http://mhw.dev-better.com"
    private const val TAGS = "/tags"
    private const val CHECKED_TAGS = "/user/tag"
    private const val SEARCH_STATUS = "/user/active"
    private const val LOGIN = "/login"
    private const val ACCEPT = "/accept"

    private object Json {
        const val TAG_ID = "id"
        const val TAG_NAME = "name"
        const val TAG_VALUES = "values"
    }

    private val client = OkHttpClient()

    fun getSurveyForm(c: Callback<List<UserSurveyTag>>) {
        val request = Request.Builder()
                .url(BASE + TAGS)
                .build()
        client.newCall(request).enqueue(CallbackWrapper(c, { response ->
            val jsonQuestions = JSONArray(response.body().string())
            logD(jsonQuestions)
            (0..jsonQuestions.length() - 1)
                    .map { index -> jsonQuestions[index] as JSONObject }
                    .map { question ->
                        val valuesJson = (question[Json.TAG_VALUES] as JSONObject)
                        val values = valuesJson.keys().asSequence()
                                .map { key -> key to valuesJson[key] as String }
                                .map { idNamePair ->
                                    TagVariant(
                                            idNamePair.first.toInt(),
                                            idNamePair.second)
                                }.toList()
                        UserSurveyTag(
                                question[Json.TAG_ID] as Int,
                                question[Json.TAG_NAME] as String,
                                values)
                    }
        }))
    }

    fun sendSurveyResults(checkedInterests: List<Pair<Int, List<Int>>>, c: Callback<Boolean>) {
        val payload = JSONObject().put("user_id", UserData.getUserId())
        checkedInterests.forEach { tagIdCheckedVariantsPair ->
            val groupId = tagIdCheckedVariantsPair.first.toString()
            val checkedVariants = tagIdCheckedVariantsPair.second.toJsonArray()
            payload.put(groupId, checkedVariants)
        }
        logD(payload)
        val request = Request.Builder()
                .url(BASE + CHECKED_TAGS)
                .method("PUT", payload.toRequestBody())
                .build()
        client.newCall(request).enqueue(CallbackWrapper(c, { r ->
            logD(r.body().string())
            r.isSuccessful
        }))
    }

    fun accept(meetindId: Int, accept: Boolean, c: Callback<Boolean>) {
        val payload = JSONObject()
                .put("user_id", UserData.getUserId())
                .put("location_id", meetindId)
                .put("accepted", accept)
        val request = Request.Builder()
                .url(BASE + ACCEPT)
                .method("POST", payload.toRequestBody())
                .build()
        client.newCall(request).enqueue(CallbackWrapper(c, { it.isSuccessful }))
    }

    fun changeSearchStatus(isSearchActive: Boolean, c: Callback<Boolean>) {
        val payload = JSONObject()
                .put("user_id", UserData.getUserId())
                .put("is_active", if (isSearchActive) 1 else 0)
        val request = Request.Builder()
                .url(BASE + SEARCH_STATUS)
                .method("PUT", payload.toRequestBody())
                .build()
        client.newCall(request).enqueue(CallbackWrapper(c, { r -> r.isSuccessful }))
    }

    fun logIn(user: User, c: Callback<LoginResult>) {
        val payload = JSONObject().put("email", user.email)
                .put("name", user.name)
                .put("avatar", user.image)
        if (user.isVkUser()) {
            payload.put("access_token", user.accessToken)
                    .put("user_id", user.userId)
        }
        val body = payload.toRequestBody()
        val request = Request.Builder()
                .url(BASE + LOGIN)
                .method("POST", body)
                .build()
        client.newCall(request).enqueue(CallbackWrapper(c, { r ->
            val response = JSONObject(r.body().string())
            val selectedJson = response.getJSONArray("selected")
            val interests = (0..selectedJson.length() - 1).map { selectedJson[it] as Int }
            logD(interests)
            UserData.saveSelectedMusic(interests)
            LoginResult(
                    response["id"] as Int,
                    response["has_tags"] as Boolean)
        }))
    }

    interface Callback<T> {
        fun onError(msg: String)
        fun onSuccess(result: T)
    }

    private class CallbackWrapper<T>(
            val c: Callback<T>?,
            val onResponseAction: (Response) -> T) : okhttp3.Callback {

        override fun onFailure(call: Call?, e: IOException) {
            e.message?.let { msg ->
                logE(msg, e)
                c?.onError(msg)
            }
        }

        override fun onResponse(call: Call?, response: Response) {
            val result = response.use { onResponseAction(it) }
            logD(response.code(), response.message())
            Handler(Looper.getMainLooper()).post { c?.onSuccess(result) }
        }
    }
}
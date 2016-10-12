package com.mhw.nestbee

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.model.TagVariant
import com.mhw.nestbee.model.UserSurveyTag
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        nestbeeMap.putPointerOnLocation(1)
    }
}

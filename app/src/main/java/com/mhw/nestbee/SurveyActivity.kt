package com.mhw.nestbee

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhw.nestbee.adapter.TagAdapter
import com.mhw.nestbee.adapter.TagVariantsAdapter
import com.mhw.nestbee.auth.AuthActivity
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.model.UserSurveyTag
import com.mhw.nestbee.net.NetworkManager
import com.mhw.nestbee.util.Callback
import com.mhw.nestbee.util.logD
import com.mhw.nestbee.util.message
import kotlinx.android.synthetic.main.activity_survey.*
import kotlinx.android.synthetic.main.toolbar.*

class SurveyActivity : ProgressDialogActivity() {

    private var surveyAdapter: TagAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        surveyTagsList.layoutManager = LinearLayoutManager(this)

        userName.text = UserData.getUsername()

        NetworkManager.getSurveyForm(Callback({ onSurveyLoaded(it) }, { onError(it) }))

        procceedButton.setOnClickListener {
            surveyAdapter?.let { adapter ->
                logD(adapter.getSurveyResults())
                showDialog()
                NetworkManager.sendSurveyResults(
                        adapter.getSurveyResults(),
                        Callback({
                            hideDialog()
                            onSurveySentToServer(it)
                        }, {
                            hideDialog()
                            onError(it)
                        }))
            }
        }
        logOut.setOnClickListener {
            UserData.logOut()
            val intent = Intent(this, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        val image = Uri.parse(UserData.getUserImage())
        userImage.setImageURI(image)
    }

    private fun onSurveyLoaded(survey: List<UserSurveyTag>) {
        logD(survey)
        progressBar.visibility = View.GONE
        surveyAdapter = TagAdapter(survey)
        surveyTagsList.adapter = surveyAdapter
        surveyTagsList.visibility = View.VISIBLE
    }

    private fun onSurveySentToServer(success: Boolean) {
        if (success) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            UserData.markSurveyPassed()
            UserData.saveUserInterests(surveyAdapter?.getUserInterests() ?: emptyList())
            startActivity(intent)
            finish()
        } else {
            message("Something went wrong!")
        }
    }

    private fun onError(msg: String) {
        message(msg)
    }
}

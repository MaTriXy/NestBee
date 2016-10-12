package com.mhw.nestbee

import android.app.Activity
import android.app.Fragment
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.mhw.nestbee.adapter.UserInterestsAdapter
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.net.NetworkManager
import com.mhw.nestbee.util.Callback
import com.mhw.nestbee.util.Pushes
import com.mhw.nestbee.util.message
import com.mhw.nestbee.util.start
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.properties.Delegates

class HomeActivity : ProgressDialogActivity() {

    private var adapter by Delegates.notNull<UserInterestsAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userInterests.layoutManager = LinearLayoutManager(this)
        adapter = UserInterestsAdapter()
        userInterests.adapter = adapter
        adapter.setEmptyView(empty)

        reflectSearchState()
        toggleSearchFab.setOnClickListener { toggleSearchStatus() }
        toggleSearchFab.setImageResource(getIcon())
        editProfile.setOnClickListener {
            val intent = Intent(this, SurveyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toggleSearchStatus() {
        if (UserData.isMeetingAccepted()) {
            start(NewMeetingActivity::class.java)
        } else {
            showDialog()
            NetworkManager.changeSearchStatus(!UserData.isSearchActive(), Callback({ success ->
                hideDialog()
                if (success) {
                    UserData.setSearchState(!UserData.isSearchActive())
                    reflectSearchState()
                }
            }, {
                hideDialog()
                message(it)
            }));
        }
    }

    private fun reflectSearchState() {
        searchStatus.text = getStateHintLabel()
        toggleSearchFab.backgroundTintList = ColorStateList.valueOf(getStateFabColor())
    }

    private fun getStateFabColor() = ContextCompat.getColor(this,
            if (UserData.isMeetingAccepted()) {
                R.color.materialGreen
            } else {
                if (UserData.isSearchActive()) {
                    R.color.materialRed
                } else R.color.colorAccent
            })

    private fun getStateHintLabel() = getString(
            if (UserData.isMeetingAccepted()) {
                R.string.label_to_meetup
            } else {
                if (UserData.isSearchActive()) {
                    R.string.label_active
                } else R.string.label_inactive
            })

    private fun getIcon() =
            if (UserData.isMeetingAccepted()) {
                R.drawable.ic_today_white_24dp
            } else R.drawable.ic_search_white_24dp

}

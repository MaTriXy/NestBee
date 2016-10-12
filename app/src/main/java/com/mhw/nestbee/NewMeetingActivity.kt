package com.mhw.nestbee

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.net.NetworkManager
import com.mhw.nestbee.util.Callback
import com.mhw.nestbee.util.message
import com.mhw.nestbee.util.start
import kotlinx.android.synthetic.main.activity_new_meeting.*
import java.text.SimpleDateFormat
import java.util.*

class NewMeetingActivity : ProgressDialogActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_meeting)

        val meeting = UserData.getMeeting()
        checkIfMeetingIsUpToDate()

        nestbeeMap.putPointerOnLocation(meeting.locationId)
        meetingLocation.text = getLocation(meeting.locationId)
        meetingTime.text = formatTime(meeting.date)
        meetingPassword.text = meeting.password
        meetingInterests.showInterests(meeting.interests)
        attendeesList.showAttendees(meeting.attendees)
        backButton.setOnClickListener { start(HomeActivity::class.java) }

        if (UserData.isMeetingAccepted()) {
            hideButtons()
        }

        meetingAccept.setOnClickListener { accept(true) }
        meetingReject.setOnClickListener { accept(false) }
    }

    private fun accept(accept: Boolean) {
        showDialog()
        NetworkManager.accept(UserData.getMeeting().id, accept, Callback({ callSucceeded ->
            hideDialog()
            if (callSucceeded) {
                if (accept) {
                    UserData.acceptMeeting()
                    hideButtons()
                } else {
                    cancelMeeting()
                }
            } else {
                message("Call failed")
            }
        }, {
            hideDialog()
            message(it)
            cancelMeeting()
        }))
    }

    private fun hideButtons() {
        meetingButtons.visibility = View.GONE
        meetingAcceptedMessage.visibility = View.VISIBLE
    }

    private fun checkIfMeetingIsUpToDate() {
        val meeting = UserData.getMeeting()
        if (meeting.date.before(Date())) {
            cancelMeeting()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        start(HomeActivity::class.java)
    }

    private fun cancelMeeting() {
        UserData.rejectMeeting()
        start(HomeActivity::class.java)
    }

    private fun formatTime(t: Date): SpannableString {
        val timeFormat = SimpleDateFormat("h:mm a EEEE, MMM dd", Locale.getDefault())
        val timeStr = timeFormat.format(t)
        val spanLength = timeStr.indexOfFirst { it == 'M' } + 1
        val str = SpannableString(timeStr)
        str.setSpan(StyleSpan(Typeface.BOLD), 0, spanLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return str
    }

    private fun getLocation(locationId: Int): SpannableString {
        val location = when (locationId) {
            1 -> "Stage"
            2 -> "IoT stand"
            3 -> "NonStop stand"
            4 -> "IQOS stand"
            5 -> "Entrance"
            6 -> "WebAcademy stand"
            7 -> "Rotor stand"
            else -> IllegalArgumentException()
        }.toString()
        val spanLength = location.length
        val str = SpannableString("$location. Dovzhenka 1")
        str.setSpan(StyleSpan(Typeface.BOLD), 0, spanLength, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return str
    }
}

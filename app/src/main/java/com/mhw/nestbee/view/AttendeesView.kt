package com.mhw.nestbee.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.mhw.nestbee.R
import com.mhw.nestbee.util.dpToPx

/**
 * Created by yarolegovich on 09.10.2016.
 */
class AttendeesView : FrameLayout {

    companion object {
        val IMAGE_SIZE = dpToPx(40)
        val IMAGE_OFFSET = dpToPx(30)
    }

    private var offset = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun showAttendees(avatars: List<String>) {
        val inflater = LayoutInflater.from(context)
        avatars.map { url ->
            val destination = inflater.inflate(R.layout.view_image, this, false) as BezelImageView
            destination.setImageURI(Uri.parse(url))
            val lp = FrameLayout.LayoutParams(destination.layoutParams)
            lp.leftMargin = offset
            offset += IMAGE_OFFSET
            destination.layoutParams = lp
            destination
        }.forEach { addView(it) }
    }
}
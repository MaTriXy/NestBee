package com.mhw.nestbee.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mhw.nestbee.R
import com.mhw.nestbee.model.UserInterest
import com.mhw.nestbee.util.Icons
import org.jetbrains.anko.find

/**
 * Created by yarolegovich on 09.10.2016.
 */
class InterestsLayout : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    fun showInterests(interests: List<UserInterest>) {
        val inflater = LayoutInflater.from(context)
        interests.map { it to inflater.inflate(R.layout.view_interest, this, false) }.forEach {
            it.second.find<TextView>(R.id.labelName).text = it.first.name
            val icon = Icons.getFor(it.first.groupId)
            it.second.find<ImageView>(R.id.labelIcon).setImageResource(icon)
            addView(it.second)
        }
    }
}
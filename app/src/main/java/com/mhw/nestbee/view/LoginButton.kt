package com.mhw.nestbee.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.mhw.nestbee.R

/**
 * Created by yarolegovich on 08.10.2016.
 */
class LoginButton : FrameLayout {

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    fun init(attr: AttributeSet) {
        setBackgroundResource(R.drawable.login_btn_bg)
        inflate(context, R.layout.view_login_button, this)

        val ta = context.obtainStyledAttributes(attr, R.styleable.LoginButton)
        try {
            val text = findViewById(R.id.text) as TextView
            val icon = findViewById(R.id.icon) as ImageView

            text.text = ta.getText(R.styleable.LoginButton_text)
            icon.setImageDrawable(ta.getDrawable(R.styleable.LoginButton_socialIcon))
        } finally {
            ta.recycle()
        }
    }

    fun clickable() = getChildAt(0)
}
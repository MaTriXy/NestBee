package com.mhw.nestbee.view

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.mhw.nestbee.R
import com.mhw.nestbee.util.dpToPx
import com.mhw.nestbee.util.logD
import kotlin.properties.Delegates

/**
 * Created by yarolegovich on 08.10.2016.
 */

class ExpandableTagCircle : View {

    companion object {
        private val DEFAULT_RADIUS = dpToPx(20)
    }

    fun setInterArcSpaceHeight(h: Float) {
        val lp = layoutParams
        lp.height = (h + DEFAULT_RADIUS * 2f).toInt()
        layoutParams = lp
        requestLayout()
    }

    fun getInterArcSpaceHeight(): Float {
        return layoutParams.height - DEFAULT_RADIUS * 2f
    }

    private val tagPaint: Paint
    private val upperArc: RectF
    private val lowerArc: RectF
    private val interArcSpace: RectF

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        tagPaint = Paint()
        tagPaint.color = ContextCompat.getColor(context, R.color.colorAccent)
        tagPaint.isAntiAlias = true

        upperArc = RectF()
        lowerArc = RectF()
        interArcSpace = RectF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val diameter = DEFAULT_RADIUS * 2f
        upperArc.set(0f, 0f, diameter, diameter)
        lowerArc.set(0f, h - diameter, diameter, h.toFloat())
        interArcSpace.set(0f, diameter / 2f, diameter, h - diameter / 2f)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawArc(upperArc, 180f, 180f, true, tagPaint)
        canvas.drawArc(lowerArc, 0f, 180f, true, tagPaint)
        canvas.drawRect(interArcSpace, tagPaint)
    }

    fun tagColor(color: Int) {
        tagPaint.color = color
    }
}
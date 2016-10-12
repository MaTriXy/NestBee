package com.mhw.nestbee.view

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.mhw.nestbee.R
import com.mhw.nestbee.util.dpToPx

/**
 * Created by yarolegovich on 09.10.2016.
 */

class NestBeeMap : FrameLayout {

    private companion object {
        val ORIGINAL_WIDTH = 1000f
        val ORIGINAL_HEIGHT = 603f

        val STAGE = PointF(410f, 143f)
        val IOT = PointF(706f, 67f)
        val NON_STOP = PointF(854f, 129f)
        val IQOS = PointF(854f, 345f)
        val WELCOME = PointF(697f, 464f)
        val WEB_ACADEMY = PointF(381f, 466f)
        val ROTOR = PointF(140f, 278f)

        val MARKER_SIZE = dpToPx(36)
    }

    private var positions: List<PointF> = emptyList()
    private var point = -1

    private val marker: ImageView

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setImageResource(R.drawable.nestbee_map)
        addView(imageView)

        marker = ImageView(context)
        marker.setImageResource(R.drawable.ic_location_on_white_36dp)
        marker.setColorFilter(Color.parseColor("#D32F2F"))
        val lp = generateDefaultLayoutParams()
        lp.width = MARKER_SIZE
        lp.height = MARKER_SIZE
        marker.layoutParams = lp
        addView(marker)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val mapper = Mapper(w / ORIGINAL_WIDTH, h / ORIGINAL_HEIGHT)
        positions = listOf(
                mapper.map(STAGE), mapper.map(IOT),
                mapper.map(NON_STOP), mapper.map(IQOS),
                mapper.map(WELCOME), mapper.map(WEB_ACADEMY),
                mapper.map(ROTOR))
        if (point >= 0) {
            putMarker()
        }
    }

    fun putPointerOnLocation(locationId: Int) {
        point = locationId - 1
        if (width != 0 && height != 0) {
            putMarker()
        }
    }

    private fun putMarker() {
        val position = positions[point]
        marker.animate().x(position.x - MARKER_SIZE / 2f)
                .y(position.y - MARKER_SIZE)
                .setDuration(0)
                .start()
    }

    private class Mapper(val slopeX: Float, val slopeY: Float) {
        fun map(original: PointF): PointF {
            return PointF(original.x * slopeX, original.y * slopeY)
        }
    }


}
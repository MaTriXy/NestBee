package com.mhw.nestbee.util

import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * Created by yarolegovich on 08.10.2016.
 */


fun dpToPx(dp: Int): Int{
    val resources = Resources.getSystem()
    val metrics = resources.displayMetrics
    return (dp.toFloat() * (metrics.densityDpi.toFloat()) / DisplayMetrics.DENSITY_DEFAULT).toInt()
}
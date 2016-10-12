package com.mhw.nestbee.model

import java.util.*

/**
 * Created by yarolegovich on 08.10.2016.
 */
data class UserSurveyTag(
        val id: Int,
        val name: String,
        val variants: List<TagVariant>) {

    private val checked = HashSet<Int>()
    var isExpanded = false

    fun setChecked(isChecked: Boolean, id: Int) {
        if (isChecked) {
            checked.add(id)
        } else {
            checked.remove(id)
        }
    }

    fun isChecked(id: Int) = checked.contains(id)

    fun allChecked() = checked.toList()
}

data class TagVariant(
        val id: Int,
        val name: String)
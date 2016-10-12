package com.mhw.nestbee.adapter

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.mhw.nestbee.R
import com.mhw.nestbee.model.TagVariant
import com.mhw.nestbee.model.UserSurveyTag
import com.mhw.nestbee.util.dpToPx
import org.jetbrains.anko.find

/**
 * Created by yarolegovich on 08.10.2016.
 */
class TagVariantsAdapter : RecyclerView.Adapter<TagVariantsAdapter.ViewHolder>() {

    private val height = dpToPx(17) + TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 16f,
            Resources.getSystem().displayMetrics)

    private var tag: UserSurveyTag? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_survey_tag_variant, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tag!!.variants[position]
        holder.variantId = item.id
        holder.variantName.text = item.name
        holder.checkBox.isChecked = tag!!.isChecked(item.id)
    }

    override fun getItemCount() = tag?.variants?.size ?: 0

    fun showVariantsFor(t: UserSurveyTag) {
        tag = t
        notifyDataSetChanged()
    }

    fun getHeight(): Float {
        return (height * itemCount).toFloat()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var variantId: Int = 0

        val variantName: TextView
        val checkBox: CheckBox

        init {
            variantName = v.find(R.id.tagVariant)
            checkBox = v.find(R.id.tagVariantCheckBox)

            v.setOnClickListener { v ->
                checkBox.toggle()
                tag?.setChecked(checkBox.isChecked, variantId)
            }
        }
    }
}
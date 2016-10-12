package com.mhw.nestbee.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mhw.nestbee.R
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.model.UserInterest
import com.mhw.nestbee.model.UserSurveyTag
import com.mhw.nestbee.util.logD
import com.mhw.nestbee.view.ExpandableTagLayout
import org.jetbrains.anko.find

/**
 * Created by yarolegovich on 08.10.2016.
 */
class TagAdapter(private val data: List<UserSurveyTag>) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    init {
        data.find { it.id == 2 }?.let { musicTag ->
            UserData.getSelectedMusic().forEach {
                musicTag.setChecked(true, it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_survey_tag, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.tagLayout.setTag(item)
    }

    override fun getItemCount() = data.size

    fun getSurveyResults() = data.map { it.id to it.allChecked() }
            .filter { it.second.isNotEmpty() }

    fun getUserInterests() = data.flatMap { tag ->
        tag.allChecked().map { checkedId -> tag.variants.find { it.id == checkedId } }
                .filterNotNull()
                .map { tag.id to it.name }
    }.map { groupIdVariantNamePair ->
        UserInterest(groupIdVariantNamePair.first, groupIdVariantNamePair.second)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tagLayout by lazy { v.find<ExpandableTagLayout>(R.id.tagLayout) }
    }
}
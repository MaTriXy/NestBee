package com.mhw.nestbee.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mhw.nestbee.R
import com.mhw.nestbee.data.UserData
import com.mhw.nestbee.util.Icons
import kotlin.properties.Delegates

/**
 * Created by yarolegovich on 08.10.2016.
 */
class UserInterestsAdapter : RecyclerView.Adapter<UserInterestsAdapter.ViewHolder>() {

    private var data = UserData.getUserInterests()

    private var tintColor by Delegates.notNull<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_user_interest, parent, false)
        return ViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        tintColor = ContextCompat.getColor(recyclerView.context, R.color.grayIcon)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.icon.setImageResource(Icons.getFor(item.groupId))
        holder.icon.setColorFilter(tintColor)
    }

    override fun getItemCount() = data.size

    fun setEmptyView(empty: View) {
        empty.visibility = if (data.isEmpty()) {
            View.VISIBLE
        } else View.GONE
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name by lazy { v.findViewById(R.id.interestName) as TextView }
        val icon by lazy { v.findViewById(R.id.interestIcon) as ImageView }
    }
}
package com.mhw.nestbee.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import com.mhw.nestbee.R
import com.mhw.nestbee.adapter.TagVariantsAdapter
import com.mhw.nestbee.model.UserSurveyTag
import com.mhw.nestbee.util.Icons
import com.mhw.nestbee.util.dpToPx
import org.jetbrains.anko.find

/**
 * Created by yarolegovich on 08.10.2016.
 */
class ExpandableTagLayout : FrameLayout {

    companion object {
        private val HEIGHT_COLLAPSED = dpToPx(44).toFloat()
    }

    private var expanded = false
    private var animator: Animator? = null

    private val tagCircle: ExpandableTagCircle
    private val tagName: TextView
    private val tagVariantList: View
    private val tagVariantAdapter: TagVariantsAdapter
    private val tagIcon: ImageView

    private var tag: UserSurveyTag? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        setBackgroundColor(Color.WHITE)
        inflate(context, R.layout.view_expandable_tag_layout, this)
        find<View>(R.id.tagContainer).setOnClickListener { toggleExpansion() }

        tagCircle = find(R.id.tagCircle)
        tagVariantAdapter = TagVariantsAdapter()
        tagVariantList = find<RecyclerView>(R.id.tagVariants)
        tagVariantList.layoutManager = LinearLayoutManager(context)
        tagVariantList.adapter = tagVariantAdapter
        tagName = find(R.id.tagName)
        tagIcon = find(R.id.tagIcon)
    }

    private fun toggleExpansion() {
        animator?.let { it.cancel() }
        tagVariantList.animate().cancel()
        tagVariantList.animate().alpha(0f)
                .setDuration(200)
                .withEndAction {
                    tagVariantList.visibility = View.GONE
                    if (expanded) {
                        collapse()
                    } else {
                        expand()
                    }
                    expanded = !expanded
                    tag?.isExpanded = expanded
                }.start()
    }

    private fun expand() {
        if (!expanded) {
            animator = animator(HEIGHT_COLLAPSED + tagVariantAdapter.getHeight())
            animator!!.start()
        }
    }

    private fun collapse() {
        if (expanded) {
            animator = animator(0f)
            animator!!.start()
        }
    }

    private fun animator(height: Float): Animator {
        val animator = ObjectAnimator.ofFloat(
                tagCircle, "interArcSpaceHeight",
                tagCircle.getInterArcSpaceHeight(),
                height)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 1000
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (expanded) {
                    showList()
                }
            }
        })
        return animator
    }

    private fun showList() {
        tagVariantList.alpha = 0f
        tagVariantList.animate().alpha(255f).setDuration(300)
                .withStartAction {
                    tagVariantList.visibility = View.VISIBLE
                }.start()
    }

    fun setTag(tag: UserSurveyTag) {
        if (expanded != tag.isExpanded) {
            toggleExpansion()
        }
        tagIcon.setImageResource(Icons.getFor(tag.id))
        tagIcon.setColorFilter(Color.WHITE)
        tagName.text = tag.name
        tagVariantAdapter.showVariantsFor(tag)
    }
}
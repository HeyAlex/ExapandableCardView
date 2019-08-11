package com.github.heyalex.expandable_cardview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.simple_cardview_header.view.*

class SimpleExpandableCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ExpandableCardView(context, attrs, defStyleAttr) {

    private var title: String?
    private var iconDrawable: Drawable?
    private var degreeAnimation: Float
    private lateinit var defaultAnimationOnExpanding: Animation
    private lateinit var defaultAnimationOnCollapsing: Animation

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleExpandableCardView)
        val defaultDegree = context.resources.getInteger(R.integer.degree).toFloat()
        degreeAnimation = typedArray.getFloat(
            R.styleable.SimpleExpandableCardView_degreeIconAnimation,
            defaultDegree
        )
        title = typedArray.getString(R.styleable.SimpleExpandableCardView_title)
        val drawable = typedArray.getDrawable(R.styleable.SimpleExpandableCardView_icon)
        iconDrawable = drawable ?: ContextCompat.getDrawable(context, R.drawable.ic_expand_more)

        headerViewRes = R.layout.simple_cardview_header
        typedArray.recycle()
        initAnimation()
    }

    private fun initAnimation() {
        defaultAnimationOnExpanding = RotateAnimation(
            0f,
            degreeAnimation,
            Animation.RELATIVE_TO_SELF,
            DEFAULT_PIVOT,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )

        defaultAnimationOnCollapsing = RotateAnimation(
            degreeAnimation,
            0f,
            Animation.RELATIVE_TO_SELF,
            DEFAULT_PIVOT,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )

        defaultAnimationOnExpanding.fillAfter = true
        defaultAnimationOnCollapsing.fillAfter = true
        defaultAnimationOnExpanding.duration = animDuration
        defaultAnimationOnCollapsing.duration = animDuration
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        header_textview.text = title
        ViewCompat.setBackground(header_expand_icon, iconDrawable)
    }


    override fun beforeExpandStart() {
        header_expand_icon.startAnimation(defaultAnimationOnExpanding)
    }

    override fun beforeCollapseStart() {
        header_expand_icon.startAnimation(defaultAnimationOnCollapsing)
    }

    fun setIcon(@DrawableRes drawableRes: Int) {
        iconDrawable = ContextCompat.getDrawable(context, drawableRes)
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setIcon(icon: Drawable) {
        iconDrawable = icon
    }

    companion object {
        const val DEFAULT_PIVOT = 0.5f
    }
}
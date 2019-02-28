package com.github.heyalex.expandable_cardview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.expandable_cardview.view.*

open class ExpandableCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private lateinit var headerView: View
    private lateinit var contentView: View
    private var isExpandedOnStart: Boolean = false

    @LayoutRes
    protected var headerViewRes: Int = 0

    @LayoutRes
    private var contentViewRes: Int = 0

    private var slideAnimator: ValueAnimator? = null

    var isExpanded = true
        private set

    private var isMoving: Boolean = false
    private var animDuration: Long

    private val defaultClickListener = OnClickListener {
        if (isExpanded)
            collapse()
        else
            expand()
    }

    private var listener: OnExpandChangeListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.expandable_cardview, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView)
        headerViewRes =
            typedArray.getResourceId(R.styleable.ExpandableCardView_header_view, View.NO_ID)
        contentViewRes =
            typedArray.getResourceId(R.styleable.ExpandableCardView_content_view, View.NO_ID)
        isExpandedOnStart = typedArray.getBoolean(R.styleable.ExpandableCardView_expanded, false)
        val defaultDuration = context.resources.getInteger(R.integer.duration)
        animDuration = typedArray.getInteger(
            R.styleable.ExpandableCardView_animation_duration,
            defaultDuration
        ).toLong()
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        card_header.layoutResource = headerViewRes
        headerView = card_header.inflate()

        card_content.layoutResource = contentViewRes
        contentView = card_content.inflate()

        initClickListeners()

        if (!isExpandedOnStart) {
            collapse(timeAnim = 0)
        }

        headerView.setOnClickListener {
            if (isExpanded) {
                collapse()
            } else {
                expand()
            }
        }
    }

    private fun slideAnimator(start: Int, end: Int): ValueAnimator {
        return ValueAnimator.ofInt(start, end).apply {
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                val layoutParams = contentView.layoutParams
                layoutParams?.height = value
                contentView.layoutParams = layoutParams
            }
        }
    }

    fun expand(timeAnim: Long = animDuration) {
        if (isMoving) return
        isMoving = true
        contentView.visibility = View.VISIBLE

        contentView.measure(
            View.MeasureSpec.makeMeasureSpec(card_root.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val targetHeight = contentView.measuredHeight

        slideAnimator = slideAnimator(0, targetHeight).apply {
            onAnimationEnd {
                isExpanded = true
                isMoving = false
            }
            duration = timeAnim
            start()
        }
    }

    fun collapse(timeAnim: Long = animDuration) {
        if (isMoving) return
        isMoving = true
        val finalHeight = contentView.height

        slideAnimator = slideAnimator(finalHeight, 0).apply {
            onAnimationEnd {
                contentView.visibility = View.GONE
                isExpanded = false
                isMoving = false
            }
            duration = timeAnim
            start()
        }
    }

    fun removeOnExpandChangeListener() {
        this.listener = null
    }

    fun setOnExpandChangeListener(expandChangeListener: OnExpandChangeListener) {
        listener = expandChangeListener
    }

    fun setOnExpandChangeListener(expandChangeUnit: (Boolean) -> Unit) {
        listener = object : OnExpandChangeListener {
            override fun OnExpandChanged(isExpanded: Boolean) {
                expandChangeUnit(isExpanded)
            }
        }
    }

    private fun ValueAnimator.onAnimationEnd(onAnimationEnd: () -> Unit) {
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                onAnimationEnd()
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }
        })
    }

    private fun initClickListeners() {
        val views = getViewsByTag(card_root, context.resources.getString(R.string.click_marker))
        views.forEach {
            it.setOnClickListener(defaultClickListener)
        }
    }

    private fun getViewsByTag(root: ViewGroup, tag: String): ArrayList<View> {
        val views = ArrayList<View>()
        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag))
            }

            val tagObj = child.tag
            if (tagObj != null && tagObj == tag) {
                views.add(child)
            }

        }
        return views
    }
}
package com.github.heyalex.expandable_cardview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.expandable_cardview.view.*


class ExpandableCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var headerView: View? = null
    private var contentView: View? = null

    @LayoutRes
    private var headerViewRes: Int = 0

    @LayoutRes
    private var contentViewRes: Int = 0

    var isExpanded = true
        private set

    init {
        LayoutInflater.from(context).inflate(R.layout.expandable_cardview, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView)
        headerViewRes =
            typedArray.getResourceId(R.styleable.ExpandableCardView_header_view, View.NO_ID)
        contentViewRes =
            typedArray.getResourceId(R.styleable.ExpandableCardView_content_view, View.NO_ID)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        card_header.layoutResource = headerViewRes
        headerView = card_header.inflate()

        card_content.layoutResource = contentViewRes
        contentView = card_content.inflate()

        headerView?.setOnClickListener {
            if (isExpanded) {
                isExpanded = false
                collapse(contentView!!)
            } else {
                isExpanded = true
                expand(contentView!!)
            }

        }
    }

    private var mAnimator: ValueAnimator? = null

    private fun slideAnimator(start: Int, end: Int): ValueAnimator {
        val mAnimator = ValueAnimator.ofInt(start, end)

        mAnimator.addUpdateListener { valueAnimator ->
            //Update Height
            val value = valueAnimator.animatedValue as Int
            val layoutParams = contentView?.getLayoutParams()
            layoutParams?.height = value
            contentView?.setLayoutParams(layoutParams)
        }
        return mAnimator
    }

    private fun expand(v: View) {
        v.visibility = View.VISIBLE

        v.measure(
            View.MeasureSpec.makeMeasureSpec(root.getWidth(), View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val targetHeight = v.measuredHeight

        mAnimator = slideAnimator(0, targetHeight)
        mAnimator?.setDuration(400)
        mAnimator?.start()
    }

    private fun collapse(v: View) {
        val finalHeight = v.height

        mAnimator = slideAnimator(finalHeight, 0)

        mAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                //Height=0, but it set visibility to GONE
                contentView?.setVisibility(View.GONE)
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        mAnimator?.start()
    }
//    private fun animateCardView(to: Float) {
//        contentView?.animate()?.y(to)?.setInterpolator(AccelerateDecelerateInterpolator())
//            ?.setDuration(400)
//    }
//
//    fun expand() {
//        isExpanded = true
//        contentView?.measuredHeight?.toFloat()?.let {
//            animateCardView(it)
//        }
//    }
//
//    fun collapse() {
//        isExpanded = false
//        animateCardView(0f)
//    }


}
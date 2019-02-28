package com.github.heyalex.expandable_cardview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import kotlinx.android.synthetic.main.simple_cardview_header.view.*

class SimpleExpandableCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ExpandableCardView(context, attrs, defStyleAttr) {

    private var title: String?
    private var iconDrawable: Drawable?

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleExpandableCardView)
        title = typedArray.getString(R.styleable.SimpleExpandableCardView_title)
        iconDrawable = typedArray.getDrawable(R.styleable.SimpleExpandableCardView_icon)
        headerViewRes = R.layout.simple_cardview_header
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        header_textview.text = title
        header_icon.background = iconDrawable
    }

    fun setIcon(@DrawableRes drawableRes: Int) {
        iconDrawable = ContextCompat.getDrawable(context, drawableRes)
    }

    fun setTitle(title : String) {
        this.title = title
    }

    fun setIcon(icon: Drawable) {
        iconDrawable = icon
    }
}
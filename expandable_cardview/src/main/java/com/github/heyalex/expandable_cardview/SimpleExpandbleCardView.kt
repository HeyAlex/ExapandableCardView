package com.github.heyalex.expandable_cardview

import android.content.Context
import android.util.AttributeSet

class SimpleExpandableCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ExpandableCardView(context, attrs, defStyleAttr) {

    private var title: String?

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleExpandableCardView)
        title = typedArray.getString(R.styleable.SimpleExpandableCardView_title)
        headerViewRes = R.layout.simple_cardview_header
        typedArray.recycle()
    }

}
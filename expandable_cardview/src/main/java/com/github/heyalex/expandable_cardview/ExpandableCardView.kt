package com.github.heyalex.expandable_cardview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IntegerRes
import androidx.cardview.widget.CardView

class ExpandableCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var headerView: View? = null
    private var contentView: View? = null

    @IntegerRes
    private var headerViewRes: Int = 0

    @IntegerRes
    private var contentViewRes: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.expandable_cardview, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView)
        headerViewRes = typedArray.getResourceId(R.styleable.ExpandableCardView_header_view, View.NO_ID)
        contentViewRes = typedArray.getResourceId(R.styleable.ExpandableCardView_content_view, View.NO_ID)
        typedArray.recycle()
    }

}
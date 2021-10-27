package org.phantancy.fgocalc.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.databinding.LayoutListItemBinding

class ListItemView : FrameLayout {
    private lateinit var binding: LayoutListItemBinding

    constructor(context: Context) : super(context) {
        initView(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }


    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        binding = LayoutListItemBinding.inflate(LayoutInflater.from(context),this,true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListItemView, defStyleAttr, 0)
        var title = typedArray.getString(R.styleable.ListItemView_itemTitle)
        var content = typedArray.getString(R.styleable.ListItemView_itemContent)
        binding.tvTitle.text = title
        binding.tvContent.text = content
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setContent(content: String) {
        binding.tvContent.text = content
    }


}
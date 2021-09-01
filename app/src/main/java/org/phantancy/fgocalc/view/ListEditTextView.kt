package org.phantancy.fgocalc.view

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.databinding.LayoutEdittextItemBinding

class ListEditTextView : LinearLayout {
    private lateinit var binding: LayoutEdittextItemBinding

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
        binding = LayoutEdittextItemBinding.inflate(LayoutInflater.from(context), this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListEditTextView, defStyleAttr, 0)
        var title = typedArray.getString(R.styleable.ListEditTextView_itemTitle)
        var content = typedArray.getString(R.styleable.ListEditTextView_itemContent)
        var hint = typedArray.getString(R.styleable.ListEditTextView_itemHint)
        binding.tvTitle.text = title
        binding.etContent.hint = hint
        binding.etContent.setText(content)
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setContent(content: String) {
        binding.etContent.setText(content)
    }

    fun setHint(hint: String) {
        binding.etContent.hint = hint
    }

    fun addWatcher(watcher: TextWatcher){
        binding.etContent.addTextChangedListener(watcher)
    }

}
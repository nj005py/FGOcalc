package org.phantancy.fgocalc.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.databinding.LayoutListItemBinding

class ListItemView : LinearLayout {
    private lateinit var ctx: Context
    private lateinit var binding: LayoutListItemBinding

    constructor(context: Context) : super(context) {
        ctx = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        ctx = context
        init()
        parseAttrs(attrs)
    }

    fun init() {
        val view = LayoutInflater.from(ctx).inflate(R.layout.layout_list_item, this)
        binding = LayoutListItemBinding.bind(view)
    }

    fun parseAttrs(attrs: AttributeSet){
        attrs?.let {
            val count = attrs.attributeCount
            var title = ""
            var content = ""
            for (i in 0 until count){
                val attrId = attrs.getAttributeNameResource(i)
                when(attrId){
                    R.attr.itemTitle -> {title = attrs.getAttributeValue(i)}
                    R.attr.itemContent -> {content = attrs.getAttributeValue(i)}
                }
            }
            binding.tvTitle.text = title
            binding.tvContent.text = content
        }
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setContent(content: String) {
        binding.tvContent.text = content
    }


}
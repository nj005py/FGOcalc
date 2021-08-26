package org.phantancy.fgocalc.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import org.phantancy.fgocalc.R

class ListItemView : LinearLayout {
    lateinit var ctx: Context
    lateinit var tvTitle: TextView
    lateinit var tvContent: TextView

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
        tvTitle = view.findViewById(R.id.tv_title)
        tvContent = view.findViewById(R.id.tv_content)
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
            tvTitle.text = title
            tvContent.text = content
        }
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setContent(content: String) {
        tvContent.text = content
    }


}
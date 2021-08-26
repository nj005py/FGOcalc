package org.phantancy.fgocalc.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import org.phantancy.fgocalc.R

class ListEditTextView: LinearLayout {
    lateinit var ctx:Context
    lateinit var tvTitle: TextView
    lateinit var etContent: EditText

    constructor(context: Context) : super(context){
        ctx = context
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        ctx = context
        init()
        parseAttrs(attrs)
    }

    fun init(){
        val view = LayoutInflater.from(ctx).inflate(R.layout.layout_edittext_item,this)
        tvTitle = view.findViewById(R.id.tv_title)
        etContent = view.findViewById(R.id.et_content)
    }

    fun parseAttrs(attrs: AttributeSet){
        attrs?.let {
            val count = attrs.attributeCount
            var title = ""
            var hint = "hint"
            for (i in 0 until count){
                val attrId = attrs.getAttributeNameResource(i)
                when(attrId){
                    R.attr.itemTitle -> { title = attrs.getAttributeValue(i)}
                    android.R.attr.hint -> {
                        hint = attrs.getAttributeValue(i)
                    }
                }
            }
            tvTitle.text = title
            etContent.hint = hint
        }
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

}
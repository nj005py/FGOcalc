package org.phantancy.fgocalc.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import org.phantancy.fgocalc.R

class ListItemView : LinearLayout{
    lateinit var ctx:Context
    lateinit var tvTitle: TextView
    constructor(context: Context) : super(context){
        ctx = context
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        ctx = context
        init()
    }

    fun init(){
        val view = LayoutInflater.from(ctx).inflate(R.layout.layout_list_item,this)
        tvTitle = view.findViewById(R.id.tv_title)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }


}
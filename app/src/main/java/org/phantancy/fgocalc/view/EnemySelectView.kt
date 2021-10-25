package org.phantancy.fgocalc.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.data.ConditionData
import org.phantancy.fgocalc.databinding.LayoutEnemySelectViewBinding

class EnemySelectView : FrameLayout {
    private lateinit var binding: LayoutEnemySelectViewBinding
    private lateinit var  ctx:Context
    var mListener: EnemySelectListener? = null

    constructor(context: Context) : super(context) {
        this.ctx = context
        initView(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.ctx = context
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.ctx = context
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        binding = LayoutEnemySelectViewBinding.inflate(LayoutInflater.from(context), this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EnemySelectView, defStyleAttr, 0)
        var title = typedArray.getString(R.styleable.EnemySelectView_itemTitle)
        binding.tvTitle.text = title
        setSpAdapter(binding.spEnemyClass, ConditionData.getEnemyNpModsKeys())

        binding.spEnemyClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                mListener?.let {
                    it.onSelected(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setSelection(v: Int){
        binding.spEnemyClass.setSelection(v)
    }

    private fun setSpAdapter(sp: Spinner, x: Array<String>) {
        val spAdapter = ArrayAdapter(ctx, R.layout.entity_spinner, x)
        spAdapter.setDropDownViewResource(R.layout.entity_spinner)
        sp.adapter = spAdapter
    }

    interface EnemySelectListener{
        fun onSelected(position:Int)
    }

}
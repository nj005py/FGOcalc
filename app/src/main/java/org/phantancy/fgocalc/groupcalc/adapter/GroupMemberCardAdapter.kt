package org.phantancy.fgocalc.groupcalc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.phantancy.fgocalc.common.Constant
import org.phantancy.fgocalc.databinding.EntityCardPickBinding
import org.phantancy.fgocalc.groupcalc.entity.bo.CardBO

/**
 * 编队计算：成员配卡
 */
class GroupMemberCardAdapter: RecyclerView.Adapter<GroupMemberCardAdapter.MyViewHolder>() {
    private var mList: List<CardBO>? = null
    var visibleCount = 6

    inner class MyViewHolder(binding: EntityCardPickBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: EntityCardPickBinding
        fun bindView(position: Int) {
            getItem(position)?.let {
                val isVisible = it.isVisible
                val visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.ivPickCard.setVisibility(visibility)
                binding.ivPickCard.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(),
                        Constant.getCardDrawable(it.type)))

                binding.ivPickCard.setOnClickListener(View.OnClickListener {
                    if (visibleCount > 3) {
                        groupMemberCardListener?.handleClickEvent(getItem(position)!!)
                        binding.ivPickCard.setVisibility(View.INVISIBLE)
                        visibleCount--
                    }
                })
            }

        }

        init {
            this.binding = binding
        }
    }

    fun submitList(x: List<CardBO>?) {
        x?.let {
            mList = x
            visibleCount = 6
            notifyDataSetChanged()
        }
    }

    fun returnEntity(x: CardBO) {
        val position = x.position
        mList!![position].isVisible = true
        visibleCount++
        notifyItemChanged(position)
    }

    fun getItem(position: Int): CardBO? {
        return mList!![position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EntityCardPickBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (mList == null) 0 else mList?.size!!
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(position)
    }

    interface GroupMemberCardListener {
        fun handleClickEvent(x: CardBO)
    }

    var groupMemberCardListener: GroupMemberCardListener? = null

}
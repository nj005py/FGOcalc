package org.phantancy.fgocalc.groupcalc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.phantancy.fgocalc.common.Constant
import org.phantancy.fgocalc.data.ServantAvatarData
import org.phantancy.fgocalc.databinding.EntityCardPickGroupBinding
import org.phantancy.fgocalc.groupcalc.entity.bo.CardBO

class GroupChosenCardAdapter : RecyclerView.Adapter<GroupChosenCardAdapter.GroupViewHolder>(){
    var mList = arrayListOf<CardBO>()

    inner class GroupViewHolder(var binding: EntityCardPickGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(position: Int) {
            val x = mList.get(position)
            //卡片
            binding.ivPickCard.setImageDrawable(ContextCompat.getDrawable(binding.root.context, Constant.getCardDrawable(x.type)))
            //头像
            binding.ivSvtAvatar.setImageDrawable(ContextCompat.getDrawable(binding.root.context, ServantAvatarData.getServantAvatar(x.svtId)))
            binding.ivPickCard.setOnClickListener {
                groupChosenCardListener?.let {
                    it.handleClickEvent(x)
                    notifyItemRemoved(position)
                    mList.remove(x)
                    notifyItemRangeChanged(position, itemCount - 1)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(EntityCardPickGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bindView(position)
    }

    fun cleanList() {
        mList.clear()
        notifyDataSetChanged()
    }

    var groupChosenCardListener:GroupChosenCardListener? = null

    interface GroupChosenCardListener {
        fun handleClickEvent(x: CardBO?)

        //是否是同个从者组合拳
        fun handleBraveChain(isBraveChain: Boolean)
    }

}
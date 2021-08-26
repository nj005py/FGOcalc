package org.phantancy.fgocalc.groupcalc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.databinding.EntityGroupAddMemberBinding
import org.phantancy.fgocalc.databinding.EntityGroupMemberBinding
import org.phantancy.fgocalc.groupcalc.entity.bo.CardBO
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupMemberVO

/**
 * 编队计算：编队成员适配器
 */
class GroupMemberAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mList: ArrayList<GroupMemberVO> = ArrayList()
    val maxSize = 3
    var mListener: GroupMemberListener? = null
    var chosenCardsCount: Int = 0

    companion object {
        @JvmStatic
        val VIEW = 1

        @JvmStatic
        val ADD = 2
    }

    inner class MemberViewHolder(val binding: EntityGroupMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(member: GroupMemberVO, position: Int) {
            val svt = member.svtEntity
            if (svt.avatarRes != -1) {
                Glide.with(binding.root.context)
                        .load(svt.avatarRes)
                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.ivSvtAvatar)
            } else {
                Glide.with(binding.root.context)
                        .load(svt.avatarUrl)
                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.ivSvtAvatar)
            }
            //点击头像移除成员
            binding.ivSvtAvatar.setOnClickListener {
                mListener?.removeMember(member, position)
            }
            //点击详细设置
            binding.llSetting.setOnClickListener {
                mListener?.setSetting(member, position)
            }
            val adapter = GroupMemberCardAdapter()
            binding.rvCards.adapter = adapter
            adapter.submitList(member.cards)
            adapter.groupMemberCardListener = object : GroupMemberCardAdapter.GroupMemberCardListener {
                override fun handleClickEvent(x: CardBO, position: Int) {
                    if (chosenCardsCount < 3) {
                        //未满3张，可以选
                        //隐藏选中的卡
                        adapter.mList?.get(position)?.isVisible = false
                        adapter.submitList(adapter.mList)
                        chosenCardsCount++
                        mListener?.chooseCard(x)
                    }
                }
            }
        }
    }

    inner class AddViewHolder(val binding: EntityGroupAddMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(position: Int) {
            binding.cvAddMember.setOnClickListener {
                mListener?.addMember(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW) {
            MemberViewHolder(EntityGroupMemberBinding.inflate(layoutInflater, parent, false))
        } else {
            AddViewHolder(EntityGroupAddMemberBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (mList.size < maxSize) (mList.size + 1) else mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW) {
            (holder as MemberViewHolder).bindView(mList.get(position), position)
        } else {
            (holder as AddViewHolder).bindView(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isAdd(position)) ADD else VIEW
    }

    fun isAdd(position: Int): Boolean {
        val size = if (mList.size == 0) 0 else mList.size
        return (size == position)
    }

    fun submitList(list: ArrayList<GroupMemberVO>) {
        this.mList = list
        notifyDataSetChanged()
    }

    //退回已选的卡
    fun returnEntity(x: CardBO) {
        //成员表位置，卡片位置设置显示
        mList[x.svtPosition].cards[x.position].isVisible = true
        val list = mList
        submitList(list)
        if (chosenCardsCount > 0) chosenCardsCount--
    }

    interface GroupMemberListener {
        fun addMember(position: Int)
        fun removeMember(member: GroupMemberVO, position: Int)

        fun setSetting(member: GroupMemberVO, position: Int)
        fun chooseCard(x: CardBO)
    }
}
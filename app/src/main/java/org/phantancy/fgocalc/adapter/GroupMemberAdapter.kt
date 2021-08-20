package org.phantancy.fgocalc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.databinding.EntityGroupAddMemberBinding
import org.phantancy.fgocalc.databinding.EntityGroupMemberBinding
import org.phantancy.fgocalc.databinding.EntityGroupServantBinding
import org.phantancy.fgocalc.entity.GroupMemberEntity
import org.phantancy.fgocalc.entity.ServantEntity

/**
 * 编队计算：编队成员适配器
 */
class GroupMemberAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val mList : ArrayList<GroupMemberEntity> = ArrayList()
    val maxSize = 3
    var mListener: GroupMemberAdapter.GroupMemberListener? = null

    companion object {
        @JvmStatic
        val VIEW = 1

        @JvmStatic
        val ADD = 2
    }

    inner class MemberViewHolder(val binding: EntityGroupMemberBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindView(member: GroupMemberEntity, position: Int){
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
            binding.ivSvtAvatar.setOnClickListener{
                mListener?.removeMember(member,position)
            }
            val adapter = GroupMemberCardAdapter()
            binding.rvCards.adapter = adapter

        }
    }

    inner class AddViewHolder(val binding: EntityGroupAddMemberBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindView(position: Int){
            binding.cvAddMember.setOnClickListener {
                mListener?.addMember(position)
            }
//            binding.btnSetting.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == GroupServantAdapter.VIEW) {
            MemberViewHolder(EntityGroupMemberBinding.inflate(layoutInflater,parent,false))
        } else {
            AddViewHolder(EntityGroupAddMemberBinding.inflate(layoutInflater,parent,false))
        }
    }

    override fun getItemCount(): Int {
        return if (mList.size < maxSize) mList.size + 1 else mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == GroupServantAdapter.VIEW) {
            (holder as MemberViewHolder).bindView(mList.get(position),position)
        } else {
            (holder as AddViewHolder).bindView(position)
        }
    }

    interface GroupMemberListener {
        fun addMember(position: Int)
        fun removeMember(member: GroupMemberEntity, position: Int)
//        fun setSetting(position: Int)
    }
}
package org.phantancy.fgocalc.adapter

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.databinding.EntityGroupServantBinding
import org.phantancy.fgocalc.entity.ServantEntity

class GroupServantAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val mList : ArrayList<ServantEntity> = ArrayList()
    val maxSize = 3
    companion object {
        @JvmStatic
        val VIEW = 1

        @JvmStatic
        val ADD = 2
    }
    var mListener: GroupSvtListener? = null

    inner class SvtViewHolder(val binding: EntityGroupServantBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindView(svt: ServantEntity, position: Int){
            if (svt.avatarRes != -1) {
                Glide.with(binding.root.context)
                        .load(svt.avatarRes)
                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.ivAvatar)
            } else {
                Glide.with(binding.root.context)
                        .load(svt.avatarUrl)
                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.ivAvatar)
            }
            binding.ivAvatar.setOnClickListener{
                mListener?.removeSvt(svt)
//                mList.remove(svt)
//                notifyDataSetChanged()
            }
            binding.btnSetting.visibility = View.VISIBLE
            binding.btnSetting.setOnClickListener {
                mListener?.setSetting(position)
            }
        }
    }

    inner class AddViewHolder(val binding: EntityGroupServantBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindView(position: Int){
            Glide.with(binding.ivAvatar)
                    .load(R.drawable.loading)
                    .into(binding.ivAvatar)
            binding.ivAvatar.setOnClickListener {
                mListener?.addSvt(position)
            }
            binding.btnSetting.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW) {
            SvtViewHolder(EntityGroupServantBinding.inflate(layoutInflater,parent,false))
        } else {
            AddViewHolder(EntityGroupServantBinding.inflate(layoutInflater,parent,false))
        }
    }

    override fun getItemCount(): Int {
        return if (mList.size < maxSize) mList.size + 1 else mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW) {
            (holder as SvtViewHolder).bindView(mList.get(position),position)
        } else {
            (holder as AddViewHolder).bindView(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isAdd(position)) ADD else VIEW
    }

    fun isAdd(position: Int): Boolean{
        val size = if (mList.size == 0) 0 else mList.size
        return (size == position)
    }

    fun submitList(svts: ArrayList<ServantEntity>){
        this.mList.clear()
        this.mList.addAll(svts)
        notifyDataSetChanged()
    }

    interface GroupSvtListener {
        fun addSvt(position: Int)
        fun removeSvt(svt: ServantEntity)
        fun setSetting(position: Int)
    }
}
package org.phantancy.fgocalc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.phantancy.fgocalc.databinding.EntitySettingBinding
import org.phantancy.fgocalc.entity.SettingEntity

class SettingAdapter : ListAdapter<SettingEntity,SettingAdapter.ViewHolder>(diff) {
    var settingInterface: SettingInterface? = null

    inner class ViewHolder(val binding: EntitySettingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(x : SettingEntity){
            binding.tvSetting.text = x.des
            binding.flSetting.setOnClickListener { settingInterface?.handleClick(x.code)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EntitySettingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    interface SettingInterface{
        fun handleClick(code: Int)
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<SettingEntity>(){
            override fun areItemsTheSame(oldItem: SettingEntity, newItem: SettingEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SettingEntity, newItem: SettingEntity): Boolean {
                return oldItem.des == newItem.des
            }
        }
    }


}
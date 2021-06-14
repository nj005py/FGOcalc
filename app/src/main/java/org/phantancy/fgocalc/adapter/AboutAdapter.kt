package org.phantancy.fgocalc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.phantancy.fgocalc.databinding.ActyAboutBinding
import org.phantancy.fgocalc.databinding.EntityAboutBinding
import org.phantancy.fgocalc.entity.AboutEntity


class AboutAdapter: ListAdapter<AboutEntity, AboutAdapter.ViewHolder>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<AboutEntity>() {
            override fun areItemsTheSame(oldItem: AboutEntity, newItem: AboutEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: AboutEntity, newItem: AboutEntity): Boolean {
                return oldItem.des == newItem.des
            }
        }
    }

    inner class ViewHolder(val binding: EntityAboutBinding): RecyclerView.ViewHolder(binding.root){
        fun bindView(x: AboutEntity){
            Glide.with(binding.ivAvatar)
                    .load(x.avatar)
                    .into(binding.ivAvatar)
            binding.tvDes.setText(x.des)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EntityAboutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}
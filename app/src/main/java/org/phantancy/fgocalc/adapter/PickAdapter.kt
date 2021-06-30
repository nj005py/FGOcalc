package org.phantancy.fgocalc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.phantancy.fgocalc.databinding.EntityCardPickBinding
import org.phantancy.fgocalc.databinding.EntityCardPickGroupBinding
import org.phantancy.fgocalc.entity.CardPickEntity
import java.util.*

class PickAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mList: MutableList<CardPickEntity>
    private var entityListener: IEntityListener? = null

    fun addEntity(x: CardPickEntity) {
        mList.add(x)
        if (mList.size == 3) {
            //判断组合拳
            val svtSource1 = mList[0].svtSource
            val svtSource2 = mList[1].svtSource
            val svtSource3 = mList[2].svtSource
            if (svtSource1 == svtSource2 && svtSource2 == svtSource3) {
                entityListener!!.handleBraveChain(true)
            } else {
                entityListener!!.handleBraveChain(false)
            }
        } else {
            entityListener!!.handleBraveChain(false)
        }
        notifyDataSetChanged()
    }

    val entities: List<CardPickEntity>
        get() = mList

    fun cleanList() {
        mList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CardPickEntity.SINGLE_CALC -> SingleViewHolder(EntityCardPickBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> GroupViewHolder(EntityCardPickGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CardPickEntity.SINGLE_CALC -> (holder as SingleViewHolder).bindView(position)
            else -> (holder as GroupViewHolder).bindView(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return mList[position].calcType
    }

    fun getItem(position: Int): CardPickEntity {
        return mList[position]
    }

    fun setEntityListenr(x: IEntityListener?) {
        entityListener = x
    }

    inner class SingleViewHolder(var binding: EntityCardPickBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(position: Int) {
            val x = getItem(position)
            binding.ivPickCard.setImageDrawable(ContextCompat.getDrawable(binding.root.context, x.img))
            binding.ivPickCard.setOnClickListener {
                entityListener?.let {
                    it.handleClickEvent(x)
                    notifyItemRemoved(position)
                    mList.remove(x)
                    notifyItemRangeChanged(position, itemCount - 1)
                }
            }
        }

    }

    inner class GroupViewHolder(var binding: EntityCardPickGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(position: Int) {
            val x = getItem(position)
            //卡片
            binding.ivPickCard.setImageDrawable(ContextCompat.getDrawable(binding.root.context, x.img))
            //头像
            binding.ivSvtAvatar.setImageDrawable(ContextCompat.getDrawable(binding.root.context, x.svtAvatar))
            binding.ivPickCard.setOnClickListener {
                entityListener?.let {
                    it.handleClickEvent(x)
                    notifyItemRemoved(position)
                    mList.remove(x)
                    notifyItemRangeChanged(position, itemCount - 1)
                }
            }
        }
    }

    interface IEntityListener {
        fun handleClickEvent(x: CardPickEntity?)

        //是否是同个从者组合拳
        fun handleBraveChain(isBraveChain: Boolean)
    }

    init {
        mList = ArrayList()
    }
}
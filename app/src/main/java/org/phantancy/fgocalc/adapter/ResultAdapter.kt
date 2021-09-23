package org.phantancy.fgocalc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.phantancy.fgocalc.common.Constant
import org.phantancy.fgocalc.databinding.EntityResultCardBinding
import org.phantancy.fgocalc.databinding.EntityResultSumBinding
import org.phantancy.fgocalc.entity.ResultEntity
import java.util.ArrayList

class ResultAdapter() : RecyclerView.Adapter<ResultAdapter.CardViewHolder>() {

    companion object {
        val diff = object : DiffUtil.ItemCallback<ResultEntity>() {
            override fun areItemsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
                return oldItem.type == newItem.type
            }

            override fun areContentsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
                return oldItem.cardType == newItem.cardType
            }
        }
    }

    private var mList:List<ResultEntity>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
//        return when(viewType){
//            ResultEntity.TYPE_CARD -> CardViewHolder(EntityResultCardBinding.inflate(inflater,parent,false))
//            ResultEntity.TYEP_SUM -> SumViewHolder(EntityResultSumBinding.inflate(inflater,parent,false))
//            else -> CardViewHolder(EntityResultCardBinding.inflate(inflater,parent,false))
//        }
        return CardViewHolder(EntityResultCardBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
//        when (getItemViewType(position)) {
//            ResultEntity.TYPE_CARD -> {
//                (holder as CardViewHolder).bindView(getItem(position))
//            }
//            else -> {
//                (holder as SumViewHolder).bindView(getItem(position))
//            }
//        }
        holder.bindView(getItem(position))
    }

//    override fun getItemViewType(position: Int): Int {
//        return getItem(position).type
//    }

    fun getItem(position: Int): ResultEntity {
        return mList?.get(position) ?: ResultEntity(ResultEntity.TYPE_CARD,"","","","","","")
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    fun submitList(list: List<ResultEntity>) {
        list?.let {
            this.mList = list
            notifyDataSetChanged()
        }
    }

    inner class CardViewHolder(val binding: EntityResultCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(x: ResultEntity) {
            if (x.type == ResultEntity.TYPE_CARD) {
                //选卡
                val img = Constant.getCardDrawable(x.cardType)
                val res = "伤害：${x.dmgMin} - ${x.dmgMax}\nNP：${x.np}\n打星：${x.star}"
                Glide.with(binding.ivCard)
                        .load(img)
                        .into(binding.ivCard)
                Glide.with(binding.ivAvatar)
                        .load(x.avatar)
                        .into(binding.ivAvatar)
                binding.tvRes.setText(res)
            } else {
                //总结
                val img = x.avatar!!
                val res = x.sum!!
                Glide.with(binding.ivCard)
                        .load(img)
                        .into(binding.ivCard)
                binding.ivAvatar.setBackgroundResource(0)//清除头像
                binding.tvRes.setText(res)
            }

        }
    }

    inner class SumViewHolder(val binding: EntityResultSumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(x : ResultEntity) {
            binding.tvRes.setText(x.sum)
        }
    }

}
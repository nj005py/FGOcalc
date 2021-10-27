package org.phantancy.fgocalc.groupcalc.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.phantancy.fgocalc.databinding.EntityCardPickBinding
import org.phantancy.fgocalc.entity.CardPickEntity
import org.phantancy.fgocalc.entity.ServantEntity

//class GroupCardsAdapter : RecyclerView.Adapter<>() {
//    var mList = ArrayList<CardPickEntity>()
//
//    inner class ViewHolder(val binding: EntityCardPickBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bindView(position: Int) {
//            val it = mList[position]
//            val visibility = if (it.isVisible) View.VISIBLE else View.INVISIBLE
//            binding.ivPickCard.visibility = visibility
//            binding.ivPickCard.setImageDrawable(ContextCompat.getDrawable(binding.root.context,it.img))
//            //点击事件
//        }
//
//    }
//}
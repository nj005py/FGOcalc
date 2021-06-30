package org.phantancy.fgocalc.common

import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.entity.CardPickEntity

class CardLogic {
    companion object{
        //解析指令卡
        @JvmStatic
        fun parseCardPickEntity(id: Int, color: Char): CardPickEntity? {
            if (color == 'q') {
                return CardPickEntity(id, "q", R.drawable.quick)
            }
            return if (color == 'a') {
                CardPickEntity(id, "a", R.drawable.arts)
            } else CardPickEntity(id, "b", R.drawable.buster)
        }

        //解析宝具卡
        @JvmStatic
        fun parseCardPickNp(id: Int, color: String): CardPickEntity? {
            if (color == "np_q") {
                return CardPickEntity(id, color, R.drawable.np_q)
            }
            return if (color == "np_a") {
                CardPickEntity(id, color, R.drawable.np_a)
            } else CardPickEntity(id, color, R.drawable.np_b)
        }

        //解析编队指令卡 int id, String name, int img, int svtSource
        @JvmStatic
        fun parseGroupCardPickEntity(id: Int, color: Char,svtSource: Int,svtAvatar: Int): CardPickEntity? {
            if (color == 'q') {
                return CardPickEntity(id, "q", R.drawable.quick, svtSource, svtAvatar)
            }
            return if (color == 'a') {
                CardPickEntity(id, "a", R.drawable.arts, svtSource, svtAvatar)
            } else CardPickEntity(id, "b", R.drawable.buster, svtSource, svtAvatar)
        }

        //解析编队宝具卡
        @JvmStatic
        fun parseGroupCardPickNp(id: Int, color: String,svtSource: Int,svtAvatar: Int): CardPickEntity? {
            if (color == "np_q") {
                return CardPickEntity(id, color, R.drawable.np_q, svtSource, svtAvatar)
            }
            return if (color == "np_a") {
                CardPickEntity(id, color, R.drawable.np_a, svtSource, svtAvatar)
            } else CardPickEntity(id, color, R.drawable.np_b, svtSource, svtAvatar)
        }
    }


}
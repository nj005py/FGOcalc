package org.phantancy.fgocalc.event

import com.jeremyliao.liveeventbus.core.LiveEvent

class DatabaseEvent(val code: Int): LiveEvent{
    companion object {
        const val ERROR = 0
        const val SUCCESS = 1
        const val RELOAD = 2
        const val UPDATED = 3
    }
}
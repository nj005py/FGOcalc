package org.phantancy.fgocalc.util

import androidx.core.content.ContextCompat
import org.phantancy.fgocalc.common.App

object ResUtils {
    fun getString(resId: Int):String {
        return App.getAppContext().getString(resId)
    }
}
package org.phantancy.fgocalc.util

import android.os.Parcel
import androidx.collection.SimpleArrayMap

fun writeSimpleArrayMap(parcel: Parcel, map: SimpleArrayMap<String, Double>) {
    if (map == null) {
        throw NullPointerException("map is null")
    }
    if (parcel == null) {
        throw NullPointerException("parcel is null")
    }
    parcel.writeInt(map.size())
    for (i in 0 until map.size()) {
        val key = map.keyAt(i)
        val value = map.valueAt(i)
        parcel.writeString(key)
        parcel.writeDouble(value!!)
    }
}

fun readSimpleArrayMap(parcel: Parcel, map: SimpleArrayMap<String, Double>) {
    if (map == null) {
        throw NullPointerException("map is null")
    }
    if (parcel == null) {
        throw NullPointerException("parcel is null")
    }
    val size = parcel.readInt()
    for (i in 0 until size) {
        val key = parcel.readString()
        val value = parcel.readDouble()
        map.put(key, value)
    }
}

fun writeDoubleList(parcel: Parcel, list: ArrayList<Double>){
    parcel.writeInt(list.size)
    for (i in 0 until list.size){
        parcel.writeDouble(list[i])
    }
}

fun readDoubleList(parcel: Parcel, list: ArrayList<Double>){
    val size = parcel.readInt()
    for (i in 0 until size){
        list.add(parcel.readDouble())
    }
}

fun writeIntList(parcel: Parcel, list: ArrayList<Int>){
    parcel.writeInt(list.size)
    for (i in 0 until list.size){
        parcel.writeInt(list[i])
    }
}

fun readIntList(parcel: Parcel, list: ArrayList<Int>){
    val size = parcel.readInt()
    for (i in 0 until size){
        list.add(parcel.readInt())
    }
}
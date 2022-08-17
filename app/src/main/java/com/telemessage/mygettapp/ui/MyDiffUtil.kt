package com.telemessage.mygettapp.ui

import androidx.recyclerview.widget.DiffUtil
import com.telemessage.mygettapp.model.Taxi
import javax.inject.Inject

class MyDiffUtil @Inject constructor(
    private val oldList: List<Taxi>,
    private val newList: List<Taxi>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].taxiId == newList[newItemPosition].taxiId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].taxiId != newList[newItemPosition].taxiId -> {
                false
            }
            oldList[oldItemPosition].cabStationName != newList[newItemPosition].cabStationName -> {
                false
            }
            oldList[oldItemPosition].cabStationIcon != newList[newItemPosition].cabStationIcon -> {
                false
            }
            oldList[oldItemPosition].lat != newList[newItemPosition].lat -> {
                false
            }
            oldList[oldItemPosition].lng != newList[newItemPosition].lng -> {
                false
            }
            else -> true


        }


    }
}
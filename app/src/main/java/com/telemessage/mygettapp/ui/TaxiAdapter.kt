package com.telemessage.mygettapp.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.telemessage.mygettapp.R
import com.telemessage.mygettapp.model.Taxi
import javax.inject.Inject

class TaxiAdapter @Inject constructor() : RecyclerView.Adapter<TaxiAdapter.ViewHolder>() {
    private var list = emptyList<Taxi>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cabStationIcon: ImageView = itemView.findViewById(R.id.cabStationIcon)
        val estimatedTimeOfArrivalTV: TextView =
            itemView.findViewById(R.id.estimatedTimeOfArrivalTV)
        val cabStationNameTV: TextView = itemView.findViewById(R.id.cabStationNameTV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.taxi_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Glide.with(holder.cabStationIcon)
            .load(Uri.parse(list[position].cabStationIcon?:""))
            .fitCenter()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.cabStationIcon)

        holder.cabStationNameTV.text = list[position].cabStationName
        holder.estimatedTimeOfArrivalTV.text = list[position].estimatedTime


    }

    fun getItem(position: Int): Taxi = list[position]

    override fun getItemCount(): Int = list.size

    fun setData(newMediaList: List<Taxi>) {
        val diffutil = MyDiffUtil(list, newMediaList)
        val diffResult = DiffUtil.calculateDiff(diffutil)
        list = newMediaList
        diffResult.dispatchUpdatesTo(this)
    }
}
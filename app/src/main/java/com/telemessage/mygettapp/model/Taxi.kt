package com.telemessage.mygettapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "TAXIS")
@Parcelize
data class Taxi(
    @PrimaryKey(autoGenerate = true)
    var taxiId: Int = 0,
    val cabStationIcon: String? = null,
    val cabStationName: String,
    var lat: Double,
    var lng: Double,
    var estimatedTime: String = "--",
    var estimatedDistance :Double=0.0,

    ) : Parcelable

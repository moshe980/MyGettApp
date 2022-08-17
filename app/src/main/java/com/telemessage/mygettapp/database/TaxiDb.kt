package com.telemessage.mygettapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.telemessage.mygettapp.database.dal.TaxiDao
import com.telemessage.mygettapp.model.Taxi

@Database(
    entities = [
        Taxi::class,
    ],
    version = 2, exportSchema = false
)
abstract class TaxiDb : RoomDatabase() {
    abstract fun taxiDao(): TaxiDao
}
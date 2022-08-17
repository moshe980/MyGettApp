package com.telemessage.mygettapp.di

import android.content.Context
import androidx.room.Room
import com.telemessage.mygettapp.database.TaxiDb
import com.telemessage.mygettapp.database.logic.DataSource
import com.telemessage.mygettapp.database.logic.DataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    private val dbName = "WeatherDB"

    @Provides
    fun provideDataResource(@ApplicationContext context: Context): DataSource {
        val db = Room.databaseBuilder(context, TaxiDb::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build()

        return DataSourceImpl(db.taxiDao())
    }
}
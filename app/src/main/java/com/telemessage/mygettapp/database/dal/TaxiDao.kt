package com.telemessage.mygettapp.database.dal

import androidx.room.*
import com.telemessage.mygettapp.model.Taxi
import kotlinx.coroutines.flow.Flow

@Dao
interface TaxiDao {
    @Query("SELECT * FROM TAXIS")
    fun findAllITaxis(): Flow<List<Taxi>>

    @Transaction
    @Query("SELECT * FROM TAXIS WHERE cabStationName=:id")
    fun findTaxiById(id: String): Flow<Taxi>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTaxi(taxi: Taxi)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllITaxis(taxis: List<Taxi>)

    @Transaction
    @Delete()
    suspend fun deleteTaxi(taxi: Taxi)
}
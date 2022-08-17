package com.telemessage.mygettapp.repository

import com.telemessage.mygettapp.model.Taxi
import kotlinx.coroutines.flow.Flow

interface TaxiRepository {
    fun getAllITaxis(): Flow<List<Taxi>>
    fun getTaxiById(id: String): Flow<Taxi>
    suspend fun saveTaxi(taxi: Taxi)
    suspend fun saveAllITaxis(taxis: List<Taxi>)
    suspend fun deleteTaxi(taxi: Taxi)
}
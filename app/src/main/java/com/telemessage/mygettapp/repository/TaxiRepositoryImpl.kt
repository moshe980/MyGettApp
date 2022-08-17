package com.telemessage.mygettapp.repository

import com.telemessage.mygettapp.database.logic.DataSource
import com.telemessage.mygettapp.model.Taxi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaxiRepositoryImpl @Inject constructor(private val dataSource: DataSource) : TaxiRepository {
    override fun getAllITaxis(): Flow<List<Taxi>> = dataSource.getAllITaxis()

    override fun getTaxiById(id: String): Flow<Taxi> = dataSource.getTaxiById(id)

    override suspend fun saveTaxi(taxi: Taxi) = dataSource.saveTaxi(taxi)

    override suspend fun saveAllITaxis(taxis: List<Taxi>) = dataSource.saveAllITaxis(taxis)

    override suspend fun deleteTaxi(taxi: Taxi) = dataSource.deleteTaxi(taxi)


}
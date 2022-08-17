package com.telemessage.mygettapp.database.logic

import com.telemessage.mygettapp.database.dal.TaxiDao
import com.telemessage.mygettapp.model.Taxi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataSourceImpl @Inject constructor(private val taxiDao: TaxiDao) : DataSource {
    override fun getAllITaxis(): Flow<List<Taxi>> = taxiDao.findAllITaxis()

    override fun getTaxiById(id: String): Flow<Taxi> = taxiDao.findTaxiById(id)

    override suspend fun saveTaxi(taxi: Taxi) = taxiDao.saveTaxi(taxi)

    override suspend fun saveAllITaxis(taxis: List<Taxi>) = taxiDao.saveAllITaxis(taxis)

    override suspend fun deleteTaxi(taxi: Taxi) = taxiDao.deleteTaxi(taxi)
}
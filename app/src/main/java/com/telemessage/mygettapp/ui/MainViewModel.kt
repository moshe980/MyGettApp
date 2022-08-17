package com.telemessage.mygettapp.ui

import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telemessage.mygettapp.model.Taxi
import com.telemessage.mygettapp.repository.TaxiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaxiRepository,
    private val geocoder: Geocoder
) : ViewModel() {
    private val adapter = TaxiAdapter()
    private val speedInKmPerHour = 50
    private val executor = Executors.newSingleThreadScheduledExecutor()

    private val _pickupAddress = MutableStateFlow("")
    private val _destinationAddress = MutableStateFlow("")
    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    val isSearchEnabled: Flow<Pair<Boolean, Boolean>> =
        combine(_pickupAddress, _destinationAddress) { pickupAddress, destinationAddress ->
            val regexString = "^[a-zA-Z ]+$"

            val isPickupAddressCorrect = pickupAddress.matches(regexString.toRegex())
            val isDestinationAddressCorrect = destinationAddress.matches(regexString.toRegex())

            return@combine Pair(isPickupAddressCorrect, isDestinationAddressCorrect)

        }


    fun setPickupAddress(pickupAddress: String) {
        _pickupAddress.value = pickupAddress
    }

    fun setDestinationAddress(destinationAddress: String) {
        _destinationAddress.value = destinationAddress
    }

    fun searchForTaxis(pickupAddress: String, destinationAddress: String) = viewModelScope.launch {
        try {
            val pickupLocation = geocoder.getFromLocationName(pickupAddress, 1)[0]
            val destinationLocation = geocoder.getFromLocationName(destinationAddress, 1)[0]

            _searchUiState.value = SearchUiState.Loading
            try {
                repository.getAllITaxis().collect { taxis ->
                    _searchUiState.value = SearchUiState.Success
                    taxis.forEach {
                        val time = distanceToSeconds(it, pickupLocation)
                        it.estimatedTime = formatDurationTime(time.toLong())
                    }
                    val sortedTaxis = taxis.sortedWith(compareBy(Taxi::estimatedDistance))
                    adapter.setData(sortedTaxis)
                }

            } catch (e: Throwable) {
                _searchUiState.value = SearchUiState.Error(e.message ?: "Unknown Error!")
            }

        }catch(e:Throwable){
            _searchUiState.value = SearchUiState.Error("Unknown Location!")

        }

    }

    private fun distanceToSeconds(
        it: Taxi,
        pickupLocation: Address
    ): Double {
        val startPoint = Location("locationA")
        startPoint.latitude = it.lat
        startPoint.longitude = it.lng

        val endPoint = Location("locationA")
        endPoint.latitude = pickupLocation.latitude
        endPoint.longitude = pickupLocation.longitude

        val distanceInMeter = startPoint.distanceTo(endPoint).toDouble()

        val distanceInKm = distanceInMeter / 1000
        it.estimatedDistance = distanceInKm
        return distanceInKm / speedInKmPerHour.toDouble() * 3600
    }

    private fun formatDurationTime(durationSeconds: Long): String {
        var hours = 0L
        var minutes = 0L
        var seconds = durationSeconds
        if (seconds >= 3600) {
            hours = seconds / 3600
            seconds -= hours * 3600
        }
        if (seconds >= 60) {
            minutes = seconds / 60
            seconds -= minutes * 60
        }
        if (hours == 0L) {
            return Formatter().format("%1\$02dm", minutes).toString()

        }
        return Formatter().format("%1\$02dh %2\$02dm", hours, minutes).toString()
    }

    fun insertStubData() {
        saveTaxi(Taxi(100, null, "Castle", 32.0225140372802, 34.77535211596226))
        saveTaxi(Taxi(101, null, "Shekem", 32.0811624762583, 34.777857274033614))
        saveTaxi(Taxi(102, null, "Habima", 32.072822981190406, 34.779162917811995))
        saveTaxi(Taxi(103, null, "Gordon", 32.08261184123802, 34.76753819772066))
        saveTaxi(Taxi(104, null, "Azrieli", 32.074414457829484, 34.79182478712483))
        saveTaxi(Taxi(105, null, "Hedera", 32.087681940114464, 35.85683673279981))
        saveTaxi(Taxi(106, null, "Eilat", 29.611833470979704, 34.93828386840645))
    }

    fun startTimer() {
        val job = viewModelScope.launch {

            repository.getAllITaxis().collect { taxis ->
                delay(5000)
                taxis.forEach {
                    it.lat = Random.nextDouble(29.0, 35.0)
                    it.lng = Random.nextDouble(29.0, 35.0)

                    repository.saveTaxi(it)
                }
            }
        }
        executor.scheduleAtFixedRate({
            job.start()

        }, 0, 5, TimeUnit.SECONDS)

    }

    private fun saveTaxi(taxi: Taxi) = viewModelScope.launch { repository.saveTaxi(taxi) }

    fun getAdapter() = adapter

    sealed class SearchUiState {
        object Success : SearchUiState()
        data class Error(val message: String) : SearchUiState()
        object Loading : SearchUiState()
        object Empty : SearchUiState()
    }
}
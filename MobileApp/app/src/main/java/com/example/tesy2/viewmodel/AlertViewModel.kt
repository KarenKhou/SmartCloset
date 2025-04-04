package com.example.tesy2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tesy2.data.repository.BluetoothRepository
import kotlinx.coroutines.flow.StateFlow

//class AlertViewModel : ViewModel() {
//    private val _alertText = MutableStateFlow("")
//    val alertText: StateFlow<String> = _alertText.asStateFlow()
//
//    init {
//        simulateBluetoothReception()
//    }
//
//    private fun simulateBluetoothReception() {
//        // Simule une réception "ALERT" après 3 secondes
//        CoroutineScope(Dispatchers.IO).launch {
//            delay(10000)
//            _alertText.value = "ALERT"
//        }
//    }
//}


class AlertViewModel : ViewModel() {
    private val repository = BluetoothRepository()

    val alertText: StateFlow<String> = repository.alertFlow

    // ✅ Appelle cette fonction uniquement après avoir la permission Bluetooth
    fun connectBluetooth() {
        repository.connectToHC05()
    }


    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
    }
}

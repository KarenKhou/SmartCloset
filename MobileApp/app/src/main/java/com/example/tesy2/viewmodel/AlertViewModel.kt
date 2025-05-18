package com.example.tesy2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tesy2.data.repository.BluetoothRepository
import kotlinx.coroutines.flow.MutableStateFlow
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
    @androidx.annotation.RequiresPermission(allOf = [
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.BLUETOOTH_SCAN
    ])
    fun connectBluetooth() {
        repository.connectToHC05()
    }
    fun triggerFakeAlert() {
        (alertText as MutableStateFlow).value = "ALERT"
        println("fake alert")
    }





    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
    }

}

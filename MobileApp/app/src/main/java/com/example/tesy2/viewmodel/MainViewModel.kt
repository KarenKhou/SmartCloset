package com.example.tesy2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tesy2.data.repository.BluetoothRepository
import kotlinx.coroutines.flow.StateFlow



class MainViewModel : ViewModel() {

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
    fun clearAlert() {
        repository.clearAlert()
    }



    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
    }
}

package com.example.tesy2.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.*

class AlertViewModel : ViewModel() {
    private val _alertText = MutableStateFlow("")
    val alertText: StateFlow<String> = _alertText.asStateFlow()

    init {
        simulateBluetoothReception()
    }

    private fun simulateBluetoothReception() {
        // Simule une réception "ALERT" après 3 secondes
        CoroutineScope(Dispatchers.IO).launch {
            delay(10000)
            _alertText.value = "ALERT"
        }
    }
}

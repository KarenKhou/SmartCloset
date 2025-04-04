package com.example.tesy2.data.repository


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import java.io.InputStream
import java.util.*

class BluetoothRepository {

    private val _alertFlow = MutableStateFlow("")
    val alertFlow: StateFlow<String> = _alertFlow

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null

    private val scope = CoroutineScope(Dispatchers.IO)
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    @androidx.annotation.RequiresPermission(allOf = [
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.BLUETOOTH_SCAN
    ])
    fun connectToHC05(deviceName: String = "HC-05") {
        Log.d("Bluetooth", "Connected to HC-05")

        bluetoothAdapter?.bondedDevices?.firstOrNull { it.name == deviceName }?.let { device ->
            scope.launch {
                try {

                    bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                    Log.d("Bluetooth", "Connected to HC-05")
                    bluetoothAdapter?.cancelDiscovery()
                    bluetoothSocket?.connect()
                    Log.d("Bluetooth", "Connected to HC-05")
                    inputStream = bluetoothSocket?.inputStream
                    Log.d("Bluetooth", "Connected to HC-05")
                    listenForData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun listenForData() {
        val buffer = ByteArray(1024)
        var bytes: Int
        Log.d("Bluetooth", "listening...")

        while (true) {
            try {
                bytes = inputStream?.read(buffer) ?: break
                val message = String(buffer, 0, bytes).trim()
                if ("ALERT" in message) {
                    _alertFlow.value = "ALERT"
                    Log.d("Bluetooth", "ALERTTTTT")

                }
            } catch (e: Exception) {
                break
            }
        }
    }

    fun disconnect() {
        inputStream?.close()
        bluetoothSocket?.close()
    }
}

package com.example.bluetoothtest

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.switchmaterial.SwitchMaterial
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private val deviceName = "ESP32 board"
    private var deviceAddress = "08:3A:F2:A9:DE:BE"
    var bluetoothDevice: BluetoothDevice? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switchBluetooth = findViewById<SwitchMaterial>(R.id.sw_bluetooth)
        val btnLedState = findViewById<Button>(R.id.btn_led_state)
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        val pairedDevicesList: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        for (device in pairedDevicesList!!) {
            Log.d("CHECK-BLUE", "Device: ${device.name}: ${device.address}, ${device.uuids}")
            if (device.name == deviceName) {
                bluetoothDevice = device
            }
        }

        val myBluetoothService = MyBluetoothService(this)
        val connectThread = myBluetoothService.ConnectThread(bluetoothDevice!!)

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Changes the visibility of the button and the switch
            when (it.resultCode) {
                RESULT_OK -> {
                    btnLedState.visibility = View.VISIBLE
                    switchBluetooth.isChecked = true
                }
                RESULT_CANCELED -> {
                    btnLedState.visibility = View.INVISIBLE
                    switchBluetooth.isChecked = false
                }
            }
        }

        // Init state for switchBluetooth
        switchBluetooth.isChecked = bluetoothAdapter.isEnabled
        // Init state for btnLedState
        when (switchBluetooth.isChecked) {
            true -> btnLedState.visibility = View.VISIBLE
            false -> btnLedState.visibility = View.INVISIBLE
        }

        switchBluetooth.setOnCheckedChangeListener { buttonView, isChecked ->
            when {
                isChecked -> {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    getResult.launch(enableBtIntent)
                }
                !isChecked -> {
                    bluetoothAdapter.disable()
                    btnLedState.visibility = View.INVISIBLE
                }
            }
        }

        btnLedState.setOnClickListener {
            // Changes the text of the button
            if (btnLedState.text.toString() == getString(R.string.turn_led_on)) {
                btnLedState.text = getString(R.string.turn_led_off)
            } else {
                btnLedState.text = getString(R.string.turn_led_on)
            }
        }
    }

}
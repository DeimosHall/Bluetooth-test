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
import java.util.*

class MainActivity : AppCompatActivity() {

    private val deviceName = "ESP32 board"
    private var deviceAddress = ""

    companion object {
        val myUUID = UUID.fromString("d3f7b72b-3f98-4c98-8a79-d0d55603b737")
        val bluetoothSocket : BluetoothSocket? = null
        //lateinit var bluetoothAdapter: BluetoothAdapter

    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switchBluetooth = findViewById<SwitchMaterial>(R.id.sw_bluetooth)
        val btnLedState = findViewById<Button>(R.id.btn_led_state)
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        val handler: Handler? = null
        val blueService = MyBluetoothService(handler!!)

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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

        val pairedDevicesList: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        for (device in pairedDevicesList!!) {
            Log.d("CHECK", "${device.name}: ${device.address}")
            if (device.name == deviceName) {
                deviceAddress = device.address
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
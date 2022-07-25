package com.example.bluetoothtest

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.bluetoothtest.BuildConfig.DEBUG
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switchBluetooth = findViewById<SwitchMaterial>(R.id.sw_bluetooth)
        val btnLedState = findViewById<Button>(R.id.btn_led_state)
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

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

        switchBluetooth.isChecked = bluetoothAdapter!!.isEnabled // Init state for switchBluetooth
        when (switchBluetooth.isChecked) {                      // Init state for btnLedState
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
            if (btnLedState.text.toString() == getString(R.string.turn_led_on)) {
                btnLedState.text = getString(R.string.turn_led_off)
            } else {
                btnLedState.text = getString(R.string.turn_led_on)
            }
        }
    }
}
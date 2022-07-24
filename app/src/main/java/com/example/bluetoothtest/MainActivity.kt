package com.example.bluetoothtest

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
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
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        switchBluetooth.isChecked = bluetoothAdapter!!.isEnabled
        when (switchBluetooth.isChecked) {
            true -> btnLedState.visibility = View.VISIBLE
            false -> btnLedState.visibility = View.INVISIBLE
        }

        Log.d("STATE", "Visible = ${View.VISIBLE.toString()}")
        Log.d("STATE", "Invisible = ${View.INVISIBLE}")

        switchBluetooth.setOnCheckedChangeListener { buttonView, isChecked ->
            when {
                isChecked -> {
                    bluetoothAdapter.enable()
                    btnLedState.visibility = View.VISIBLE
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
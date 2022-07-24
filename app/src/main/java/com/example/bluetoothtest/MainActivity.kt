package com.example.bluetoothtest

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switchBluetooth = findViewById<SwitchMaterial>(R.id.sw_bluetooth)
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        switchBluetooth.isChecked = bluetoothAdapter!!.isEnabled

        switchBluetooth.setOnCheckedChangeListener { buttonView, isChecked ->
            when {
                isChecked -> bluetoothAdapter.enable()
                !isChecked -> bluetoothAdapter.disable()
            }
        }
    }
}
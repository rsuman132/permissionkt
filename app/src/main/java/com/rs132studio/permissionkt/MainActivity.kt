package com.rs132studio.permissionkt

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val FINE_LOCATION_RQ = 101
    val CAMERA_RQ = 102
    val STORAGE_RQ = 103
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTapped()
    }

    private fun buttonTapped() {
        location_btn.setOnClickListener{
            checkForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", FINE_LOCATION_RQ)
        }
        camera_btn.setOnClickListener{
            checkForPermission(android.Manifest.permission.CAMERA, "camera", CAMERA_RQ)
        }
        location_btn.setOnClickListener {
            checkForPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, "storage", STORAGE_RQ)
        }
    }

    private fun checkForPermission(permission : String, name : String, requestCode : Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(this, permission) == (PackageManager.PERMISSION_GRANTED) -> {
                    Toast.makeText(this, "$name Permission Granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innercheck(name : String){
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "$name permission denied", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }

        when(requestCode) {
            FINE_LOCATION_RQ -> innercheck("location")
            CAMERA_RQ -> innercheck("camera")
            STORAGE_RQ -> innercheck("storage")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Permission Required")
            setMessage("Permission is access your $name is required to use this app")
            setPositiveButton("OK") {dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }

        val dialog = builder.create()
        dialog.show()

    }

}
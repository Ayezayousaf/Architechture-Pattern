package com.architechturepattern.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.architechturepattern.databinding.ActivitySplashScreenBinding


class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val multiplePermissionList =
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.TIRAMISU) {
            mutableListOf(Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO)
        }else{
            mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val allPermissionsGranted = sharedPreferences.getBoolean("AllPermissionsGranted", false)

        if (allPermissionsGranted) {
            navigateToDashboard()
            return
        }
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.splashButton.setOnClickListener(View.OnClickListener {
            askPermission()
        })
    }
    private fun askPermission() {
        val ungrantedPermissions = multiplePermissionList.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (ungrantedPermissions.isNotEmpty()) {
            permissionLauncher.launch(ungrantedPermissions.toTypedArray())
        } else {
            Toast.makeText(this, "Permissions already granted!", Toast.LENGTH_SHORT).show()
            savePermissionsGranted()
            navigateToDashboard()
        }
    }
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            val deniedPermissions = permissionsResult.filter { !it.value }
            if (deniedPermissions.isEmpty()) {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show()
                val intent=Intent(this, Dashboard::class.java)
                startActivity(intent)
                finish()

            }
            else {
                val deniedPermissionNames = deniedPermissions.keys.map { permission ->
                    when (permission) {

                        Manifest.permission.READ_EXTERNAL_STORAGE -> "Photo and Media"
                        Manifest.permission.READ_MEDIA_VIDEO -> "Read Media Video"
                        Manifest.permission.READ_MEDIA_AUDIO -> "Read Media Audio"
                        Manifest.permission.READ_MEDIA_IMAGES -> "Read Media Images"

                        else -> permission
                    }
                }
                Toast.makeText(this, "Permissions denied: $deniedPermissionNames", Toast.LENGTH_LONG).show()
                val showRational=deniedPermissions.keys.any{
                    shouldShowRequestPermissionRationale(it)
                }
                if(!showRational){
                    showPermissionDialog()
                }
            }
        }
    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("These permissions are required for the app to function properly. Please enable them in the settings.")
            .setPositiveButton("OK") { _,_  ->
                openSetting()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun openSetting()
    {
        val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }
    private fun savePermissionsGranted() {
        with(sharedPreferences.edit()) {
            putBoolean("AllPermissionsGranted", true)
            apply()
        }
    }
    private fun navigateToDashboard() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
        finish()
    }
}
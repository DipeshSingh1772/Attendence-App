package com.example.attendenceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.biometric.BiometricPrompt
import androidx.core.os.CancellationSignal
import java.util.*
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.os.Build
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.from
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class FrontPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.front_page)

        supportActionBar?.hide()
        val executor = ContextCompat.getMainExecutor(this)
        val biometricManager = BiometricManager.from(this)


//        Handler().postDelayed({
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        },3000)

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                authUser(executor)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(
                    this,
                    "Funtion Not Supported",
                    Toast.LENGTH_LONG
                ).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(
                    this,
                    "Funtion Not Supported",
                    Toast.LENGTH_LONG
                ).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(
                    this,
                    "Bad Authentication",
                    Toast.LENGTH_LONG
                ).show()
        }


    }

    private fun authUser(executor: Executor) {

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login Using Fingerprint")
            .setSubtitle("Place your finger on the fingerprint scanner to login")
            .setDeviceCredentialAllowed(true)
            .build()


        // 1
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                // 2
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@FrontPageActivity, MainActivity::class.java))
                    finish()
                }
                // 3
                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Wrong Finger",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // 4
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext,
                        "Authentication Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        val button = findViewById<ImageView>(R.id.start_authentication)
        button.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

    }

}
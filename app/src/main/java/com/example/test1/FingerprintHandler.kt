package com.example.test1



import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON
import java.util.concurrent.Executor
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity


open class FingerprintHandler(Activity:Context){
    var activity: Activity? = null
    var biometricPrompt: BiometricPrompt? = null
    val executor = MainThreadExecutor()
    fun buildBiometricPrompt(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login")
            .setSubtitle("Login into your account")
            .setDescription("Touch your finger on the finger print sensor to authorise your account.")
            .setNegativeButtonText("Cancel")
            .build()
    }
    fun setNextActivity(nextActivity: Activity){
        this.activity = nextActivity
    }
    var promptInfo = buildBiometricPrompt()
    var callback = object: BiometricPrompt.AuthenticationCallback(){
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            if (errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                biometricPrompt!!.cancelAuthentication()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Toast.makeText(Activity,"Authenticated",Toast.LENGTH_SHORT).show()
            onAuthSuccess()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onAuthFail()
            //Toast.makeText(this, "Application did not recognize the placed finger print. Please try again!", Toast.LENGTH_LONG).show()
        }

        fun onAuthSuccess() {
            Activity.startActivity(Intent(Activity, activity!!::class.java))
            (Activity as AppCompatActivity).finish()

        }

        fun onAuthFail() {

        }
    }
}

package com.example.test1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON
import androidx.biometric.BiometricPrompt

import com.example.test1.FingerprintHandler
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.lex.interactionkit.Response;
import com.amazonaws.mobileconnectors.lex.interactionkit.config.InteractionConfig;
import com.amazonaws.mobileconnectors.lex.interactionkit.ui.InteractiveVoiceView;
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.mobile.auth.core.IdentityHandler
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.config.AWSConfiguration
import kotlinx.android.synthetic.main.activity_login.*







class Login : AppCompatActivity() {
    private var credentialsProvider: AWSCredentialsProvider? = null
    private var awsConfiguration: AWSConfiguration? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var fingerprintHandler = FingerprintHandler(this)
    var lexResponse: String = ""
    companion object {
        val TAG: String = this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AWSMobileClient.getInstance().initialize(this) {
            credentialsProvider = AWSMobileClient.getInstance().credentialsProvider
            awsConfiguration = AWSMobileClient.getInstance().configuration

            IdentityManager.getDefaultIdentityManager().getUserID(object : IdentityHandler {
                override fun onIdentityId(identityId: String?) {
                    Log.d(TAG, "Identity = $identityId")
                    val cachedIdentityId = IdentityManager.getDefaultIdentityManager().cachedUserID
                    // Do something with the identity here
                }

                override fun handleError(exception: Exception?) {
                    Log.e(TAG, "Retrieving identity")
                }

            })
        }.execute()

        voiceInterface.setInteractiveVoiceListener(
            object : InteractiveVoiceView.InteractiveVoiceListener {
                override fun dialogReadyForFulfillment(
                    slots: MutableMap<String, String>?,
                    intent: String?
                ) {

                    Log.d(TAG, "Dialog ready for fulfillment:\n\tIntent: $intent")
                }


                override fun onResponse(response: Response) {
                    Log.d(TAG, "Bot response: ${response.textResponse}")
                    lexResponse = response.textResponse

                }

                override fun onError(responseText: String, e: Exception) {
                    Log.e(TAG, "Error: ${e.message}")
                }
            })
        with (voiceInterface.viewAdapter) {
            credentialsProvider = AWSMobileClient.getInstance().credentialsProvider
            setInteractionConfig(InteractionConfig("BankBot","v_one"))
            awsRegion = applicationContext.getString(R.string.aws_region)
            setCredentialProvider(credentialsProvider)
        }

        loginbutton.setOnClickListener{

            val sharedPref = this?.getSharedPreferences("CREDENTIAL_STORAGE", Context.MODE_PRIVATE)
            var Username = username.text.toString()
            var Password = password.text.toString()
            var storedUsername = sharedPref.getString("USERNAME","")
            var storedPassword = sharedPref.getString("PASSWORD","")
            if(storedUsername!!.isEmpty()){
                Toast.makeText(this,"User not registered",Toast.LENGTH_SHORT).show()
            }else{
                if(Password.equals(storedPassword)){
                    var myIntent = Intent(this, Home::class.java)
                    myIntent.putExtra("username", Username)
                    startActivity(myIntent)
                }
                else{
                    Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
                }

                username.setText("")
                password.setText("")
            }
            username.setText("")
            password.setText("")
        }

        selectFingerPrintButton.setOnClickListener{
            fingerprintHandler.setNextActivity(Home())
            biometricPrompt = BiometricPrompt( this, fingerprintHandler.executor,  fingerprintHandler.callback )

            findViewById<View>(R.id.selectFingerPrintButton).setOnClickListener { view ->
                val promptInfo = fingerprintHandler.buildBiometricPrompt()
                var res = biometricPrompt!!.authenticate(fingerprintHandler.promptInfo)
            }
        }
    }

}

package com.example.test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.mobile.auth.core.IdentityHandler
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.lex.interactionkit.Response
import com.amazonaws.mobileconnectors.lex.interactionkit.config.InteractionConfig
import com.amazonaws.mobileconnectors.lex.interactionkit.ui.InteractiveVoiceView
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.activity_add_account.voiceInterface1
import kotlinx.android.synthetic.main.activity_first_menu.*

class AddAccount : AppCompatActivity() {

    private var credentialsProvider: AWSCredentialsProvider? = null
    private var awsConfiguration: AWSConfiguration? = null
    var lexResponse = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)



        AWSMobileClient.getInstance().initialize(this) {
            credentialsProvider = AWSMobileClient.getInstance().credentialsProvider
            awsConfiguration = AWSMobileClient.getInstance().configuration

            IdentityManager.getDefaultIdentityManager().getUserID(object : IdentityHandler {
                override fun onIdentityId(identityId: String?) {
                    Log.d("TAG", "Identity = $identityId")
                    val cachedIdentityId = IdentityManager.getDefaultIdentityManager().cachedUserID
                    // Do something with the identity here
                }

                override fun handleError(exception: Exception?) {
                    Log.e("TAG", "Retrieving identity")
                }

            })
        }.execute()

        voiceInterface1.setInteractiveVoiceListener(
            object : InteractiveVoiceView.InteractiveVoiceListener {
                override fun dialogReadyForFulfillment(
                    slots: MutableMap<String, String>?,
                    intent: String?
                ) {

                    Log.d("abc", "Dialog ready for fulfillment:\n\tIntent: $intent")
                }


                override fun onResponse(response: Response) {
                    Log.d("TAG", "Bot response: ${response.textResponse}")
                    lexResponse = response.textResponse
                    if(lexResponse.equals("12345"))
                        editText_mobile_no.setText(lexResponse)
                }

                override fun onError(responseText: String, e: Exception) {
                    Log.e("TAG", "Error: ${e.message}")
                }
            })
        with (voiceInterface1.viewAdapter) {
            credentialsProvider = AWSMobileClient.getInstance().credentialsProvider
            setInteractionConfig(InteractionConfig("BankBot","v_one"))
            awsRegion = applicationContext.getString(R.string.aws_region)
            setCredentialProvider(credentialsProvider)
        }








        btn_otp.setOnClickListener{

            progressBar.visibility = View.VISIBLE
            Handler().postDelayed({
                editText_name.setText("Shivam Singh")
                editText_account_no.setText("200012012085")
                progressBar.visibility = View.INVISIBLE
            }, 4000)
        }

        btn_create_pin.setOnClickListener{
            if(editText_account_no.text.toString().isEmpty()){
                Toast.makeText(this,"Empty Fields",Toast.LENGTH_SHORT).show()
            }else {
                var myIntent = Intent(this, CreatePin::class.java)
                startActivity(myIntent)
            }
        }

        btn_cancel.setOnClickListener{
            finish()
        }

    }
}

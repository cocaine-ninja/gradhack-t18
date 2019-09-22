package com.example.test1


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_first_menu.*
import com.amazonaws.regions.Regions
import com.amazonaws.auth.CognitoCachingCredentialsProvider

import android.media.MediaPlayer
import android.util.Log
import java.io.IOException
import android.media.AudioAttributes
import android.os.Handler
import android.os.StrictMode
import android.view.KeyEvent
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
import com.amazonaws.services.polly.AmazonPollyPresigningClient
import com.amazonaws.services.polly.model.OutputFormat
import com.amazonaws.services.polly.model.SynthesizeSpeechPresignRequest
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.concurrent.thread


class FirstMenu : AppCompatActivity() {

    private var credentialsProvider: AWSCredentialsProvider? = null
    private var awsConfiguration: AWSConfiguration? = null
    val buttons = arrayOf(R.id.login_button_first_menu,R.id.signup_button)
    var index: Int = 0;
    var buttonSelected = ""
    var lexResponse: String = ""
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {

                Toast.makeText(this,index.toString(),Toast.LENGTH_SHORT).show()
                findViewById<Button>(buttons[(index+1)%2]).setBackgroundColor(Color.parseColor("#ffffff"))
                findViewById<Button>(buttons[(index)]).setBackgroundColor(0)
                buttonSelected = findViewById<Button>(buttons[index]).text.toString()
                Log.d("ChessApp",buttonSelected )
                index = (index+1) % 2;
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                Toast.makeText(this,index.toString(),Toast.LENGTH_SHORT).show()
//                findViewById<Button>(buttons[index]).setBackgroundColor(Color.parseColor("#ffffff"))
//                findViewById<Button>(buttons[(index+1)%2]).setBackgroundColor(0)
//                buttonSelected = findViewById<Button>(buttons[index]).text.toString()
//                buttonSelected = buttons[index].toString()
                findViewById<Button>(buttons[index]).performClick()
                Log.d("ChessApp",buttonSelected )
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_menu)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)







        AWSMobileClient.getInstance().initialize(this) {
            credentialsProvider = AWSMobileClient.getInstance().credentialsProvider
            awsConfiguration = AWSMobileClient.getInstance().configuration

            IdentityManager.getDefaultIdentityManager().getUserID(object : IdentityHandler {
                override fun onIdentityId(identityId: String?) {
                    Log.d(Login.TAG, "Identity = $identityId")
                    val cachedIdentityId = IdentityManager.getDefaultIdentityManager().cachedUserID
                    // Do something with the identity here
                }

                override fun handleError(exception: Exception?) {
                    Log.e(Login.TAG, "Retrieving identity")
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
                    Log.d(Login.TAG, "Bot response: ${response.textResponse}")
                    lexResponse = response.textResponse
                    if(lexResponse.equals("Login")){
                        findViewById<Button>(buttons[0]).performClick()
                    }else if(lexResponse.equals("Sign up")){
                        findViewById<Button>(buttons[1]).performClick()
                    }

                }

                override fun onError(responseText: String, e: Exception) {
                    Log.e(Login.TAG, "Error: ${e.message}")
                }
            })
        with (voiceInterface1.viewAdapter) {
            credentialsProvider = AWSMobileClient.getInstance().credentialsProvider
            setInteractionConfig(InteractionConfig("BankBot","v_one"))
            awsRegion = applicationContext.getString(R.string.aws_region)
            setCredentialProvider(credentialsProvider)
        }





























        Handler().postDelayed({
            var policy = StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            val credentialsProvider = CognitoCachingCredentialsProvider(
                applicationContext,
                "us-east-1:94319f4e-b9f8-48af-a52c-48ee4ca1348a", // Identity pool ID
                Regions.US_EAST_1 // Region
            )
            val client = AmazonPollyPresigningClient(credentialsProvider)


            var synthesizeSpeechPresignRequest = SynthesizeSpeechPresignRequest()
//            // Set the text to synthesize.
                .withText("Welcome to the coolest bank. You can Login or Sign up.")
//            // Select voice for synthesis.
                .withVoiceId("Raveena") // "Joanna"
//            // Set format to MP3.
                .withOutputFormat(OutputFormat.Mp3)
            val mediaPlayer = MediaPlayer()

            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )


            val presignedSynthesizeSpeechUrl =
                client.getPresignedSynthesizeSpeechUrl(synthesizeSpeechPresignRequest)
            try {
                // Set media player's data source to previously obtained URL.
                mediaPlayer.setDataSource(presignedSynthesizeSpeechUrl.toString())
            } catch (e: IOException) {
                Log.d(

                    "Unable to set data source for the media player! ", e.message
                )
            }

         mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener { mp -> mp.start() }
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }


        }, 4000)



        login_button_first_menu.setOnClickListener {
             var myIntent = Intent(this, Login::class.java)
             startActivity(myIntent)

        }

        signup_button.setOnClickListener {
            var myIntent = Intent(this, SignUp::class.java)
            startActivity(myIntent)
        }
    }

}
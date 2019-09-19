package t18.gradhack.com.main;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechService extends Service implements TextToSpeech.OnInitListener {
    IBinder mBinder = new LocalBinder();
    TextToSpeech tts;
    final Handler handler = new Handler();

    public boolean ttsIsReady = false;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)
            ttsIsReady = true;
        else
            ttsIsReady = false;
    }

    public class LocalBinder extends Binder {
        public TextToSpeechService getService() {
            return TextToSpeechService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.i("TextToSpeechService", "onStartCommand()");

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        return START_STICKY;
    }

    public void speakText(final String text) {
        if (ttsIsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 1000);
        }
    }
}

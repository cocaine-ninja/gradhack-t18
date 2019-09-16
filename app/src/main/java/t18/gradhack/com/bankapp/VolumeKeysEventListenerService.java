package t18.gradhack.com.bankapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class VolumeKeysEventListener extends Service {
    public VolumeKeysEventListener() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

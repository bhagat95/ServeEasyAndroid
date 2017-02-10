package com.example.bhagat.finalyear;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ConsumerBackgroundService extends Service {
    public ConsumerBackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

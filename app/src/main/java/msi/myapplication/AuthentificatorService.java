package msi.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AuthentificatorService extends Service {
    public AuthentificatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        AccountAuthentificator authenticator = new AccountAuthentificator(this);
        return authenticator.getIBinder();
    }

    @Override
    public void onCreate()
    {
        Log.v("StartServiceAtBoot", "StartAtBootService Created");
    }
}

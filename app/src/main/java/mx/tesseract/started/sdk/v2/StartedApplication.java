package mx.tesseract.started.sdk.v2;

import android.app.Application;

import mx.tesseract.sdk.v2.SDK;

/**
 *
 *
 */
public class StartedApplication extends Application
{

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();

        SDK.init(this);

    }
}

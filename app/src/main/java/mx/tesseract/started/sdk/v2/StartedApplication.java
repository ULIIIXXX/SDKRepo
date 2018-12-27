package mx.tesseract.started.sdk.v2;

import android.app.Application;

import mx.tesseract.sdk.v2.SDK;

/**
 *
 * @author Cristian Jaramillo (cristian_gerar@hotmail.com)
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

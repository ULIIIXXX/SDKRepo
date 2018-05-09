package mx.tesseract.started.sdk.v2;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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

package mx.tesseract.started.sdk.v2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import mx.tesseract.started.sdk.v2.config.AppConfig;
import mx.tesseract.started.sdk.v2.utils.ActivityUtils;

/**
 *
 * @author Cristian Jaramillo (cristian_gerar@hotmail.com)
 */
public class SplashScreenActivity extends AppCompatActivity
{
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(SplashScreenActivity.this, AppConfig.getPermissions(), 1);

            return;
        }

        ActivityUtils.startActivityDelay(this, MainActivity.class, 3000L);
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.onStart();
    }
}

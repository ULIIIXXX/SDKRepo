package mx.tesseract.started.sdk.v2.config;

import android.Manifest;

/**
 *
 *
 */
public class AppConfig
{

    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.READ_PHONE_STATE
    };

    /**
     *
     */
    private AppConfig()
    {
        throw new AssertionError();
    }

    /**
     *
     * @return
     */
    public static final String[] getPermissions()
    {
        return PERMISSIONS.clone();
    }

}
package mx.tesseract.started.sdk.v2.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Cristian Jaramillo (cristian)
 */
public final class ActivityUtils
{

    /**
     *
     */
    private ActivityUtils()
    {
        throw new AssertionError();
    }

    /**
     *
     * @param activity
     * @param view
     * @param delay
     * @param bundles
     */
    public static void startActivityDelay(final Activity activity, Class<?> view, long delay, Bundle... bundles)
    {
        final Timer timer = new Timer();
        final Intent intent = buildIntent(activity, view, bundles);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.startActivity(intent);
                activity.finish();
            }
        }, delay);
    }

    /**
     *
     * @param view
     * @param bundles
     */
    public static void startActivity(final Activity activity, Class<?> view, Bundle... bundles)
    {
        activity.startActivity(buildIntent(activity, view, bundles));
        activity.finish();
    }

    /**
     *
     * @param activity
     * @param view
     * @param bundles
     * @return
     */
    private static Intent buildIntent(final Activity activity, Class<?> view, Bundle... bundles)
    {
        final Intent intent = new Intent(activity, view);

        for (Bundle bundle : bundles)
            intent.putExtras(bundle);
        return intent;
    }


    /**
     *
     * @param activity
     * @param view
     */
    public static void requestFocus(Activity activity, View view)
    {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}


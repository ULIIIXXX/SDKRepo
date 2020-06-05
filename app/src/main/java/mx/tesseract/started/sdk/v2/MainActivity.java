package mx.tesseract.started.sdk.v2;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import mx.tesseract.sdk.v2.SDK;
import mx.tesseract.sdk.v2.http.callbacks.ActivationCallback;
import mx.tesseract.sdk.v2.support.domain.Result;

/**
 *
 *
 */
public class MainActivity extends AppCompatActivity
{

    static final String TAG = MainActivity.class.getName();

    static final String TOKEN_NAME = "Tesseract 2305";

    static final String ENROLLMENT_STRING = "NDlmYTY3M2Q2NDgxYjAzZWIzMmJjYTgwNWQ1ZGQyM2E2MWUwZjI1OTJkMTNkZDRmMDc2MGE5OGQzMWY3OGQyOTZlNDZhNDk1OGUzMzFkMWM4Mzk5ODFmNjhjYWJhZmFmMTMxMmM0YWM4ZTg4NTZlZDE4NTMwYjYxZGVhM2MzMGY4NzAyODM0NTRmZjIyOGU5NTZhZTNlNzE3ODMzNjAyOGU5NWNlZDdjNDI5ODZiZjlkNmExNDc0YWJhMWIxYTk0NmUwMjMxMzliNTc5YzFiMjQ4OGUzM2Q3MDA0NGZmYTVhY2M2ZjMzZjQyYThjMTExN2IwZDcwYzkxZDYxMzRhYjZmYWM3MmUwMTY3ZTVmODIzZWNhOTQzMDI2YjVhNTdlMTI4Nzk1MzUyNzIyMjA0MDRhNjYxZDRkMTAxOTA3ODBhMDk4NWI4ZDc5MjJiMzFlZWU2MzA0ODhkMTY3NDY0YmE2YTg2NDdkOTQwNzdkZWU4NzBmZTJhNTBhYTU5MTgwMWNlZWNiMTMzYjkxOGM1Nzg3MWMwNzNhNjY0NjIzZDNiMTVhOTViYjNhYzljZGZiOTJkZjhlODE0YjZkZGRjMGYzNDZjY2QyNWExNzMxODg=";

    static final String ACT_CODE = "43115251";

    static final String PIN = "1234";

    static final String CHALLENGE = "527792";


    /**
     *
     */
    private TextView otpTextView;

    /**
     *
     */
    private TextView timeoutTextView;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        otpTextView = findViewById(R.id.otp_text_view);
        timeoutTextView = findViewById(R.id.timeout_text_view);
    }

    /**
     *
     */
    @Override
    protected void onStart()
    {
        super.onStart();

        Result result;

        if(!SDK.existsToken(TOKEN_NAME))
        {
            result = SDK.createToken(TOKEN_NAME);

            if(result != Result.SUCCESS)
                return;

            info("CREATE TOKEN INFO");
        }

        result = SDK.selectToken(TOKEN_NAME);

        if(result != Result.SUCCESS)
            return;

        info("SELECT TOKEN INFO");

        switch (SDK.getStatus())
        {
            case CREATED:

                result = SDK.enrollmentToken(ENROLLMENT_STRING, ACT_CODE);

                info("ENROLLMENT STRING INFO");

                if(result != Result.SUCCESS) {
                    Toast.makeText(this, result.name(), Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            case ENROLLMENT:

                /**
                 *
                 */
                SDK.activate(new ActivationCallback() {

                    /**
                     *
                     */
                    @Override
                    public void success() {
                        info("ACTIVATE TOKEN INFO");
                    }

                    /**
                     *
                     * @param result
                     */
                    @Override
                    public void failed(Result result) {
                        Toast.makeText(MainActivity.this, result.name(), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case WAITING_PIN:
                result = SDK.setPin(PIN);
                info("TOKEN SET PIN INFO");

                if(result != Result.SUCCESS)
                    return;;

                break;
            case READY:
                result = SDK.auth(PIN);

                info("AUTH TOKEN INFO");

                if(result != Result.SUCCESS)
                    return;

                new CountDownTimer(10 * 60 * 100, 500) {


                        @Override
                        public void onTick(long l) {
                            otpTextView.setText(SDK.generateOTP(CHALLENGE));
                            timeoutTextView.setText(String.format("timeout %02d", (SDK.getTimeInterval()- SDK.getTimeout()) / 1000));
                        }

                        @Override
                        public void onFinish() {
                            Log.i(TAG, "Finish");
                        }

                    }.start();

                break;
            case BLOCKED:

                result = SDK.deleteToken(SDK.getTokenName());

                info("DELETE TOKEN INFO");

                if(result != Result.SUCCESS)
                    return;;

                break;
        }

    }

    /**
     *
     * @param s
     */
    private static void info(String s)
    {
        Log.i(TAG, String.format("+%53s+", "").replace(' ','-'));
        Log.i(TAG, String.format("+ %-51s +", s.toUpperCase()));
        Log.i(TAG, String.format("+%53s+", "").replace(' ','-'));
        format("Token Name", SDK.getTokenName());
        format("Token Status", SDK.getStatus());
        format("Token Type", SDK.getType());
        format("Token Serial", SDK.getSerial());
        format("Token Expires At", SDK.getExpiresAt());
        format("Token Time Interval", SDK.getTimeInterval());
        format("Token Attempts", SDK.getAttempts());
        format("Token Max Attempts", SDK.getMaxAttempts());
        format("Token Timeout", SDK.getTimeout());
        format("Token Is Auth", SDK.isAuth());
        format("Token OTP/Response", SDK.generateOTP(CHALLENGE));
    }

    /**
     *
     * @param field
     * @param value
     */
    private static final void format(String field, Object value)
    {
        Log.i(TAG, String.format("| %-20s | %-28s |", field, value == null ? "EMPTY" : value));
        Log.i(TAG, String.format("+%22s+%30s+", "", "").replace(' ','-'));
    }

}
package mx.tesseract.started.sdk.v2;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import mx.tesseract.sdk.v2.SDK;
import mx.tesseract.sdk.v2.http.callbacks.ActivationCallback;
import mx.tesseract.sdk.v2.support.domain.Result;

/**
 *
 * @author Cristian Jaramillo (cristian_gerar@hotmail.com)
 */
public class MainActivity extends AppCompatActivity
{

    static final String TAG = MainActivity.class.getName();

    static final String TOKEN_NAME = "My Token 3214";

    static final String ENROLLMENT_STRING = "ZjgyOGUwMjE1NGNlZWRhNTg1NDU4ZGFkZWViZjYyZjllZWEzOGFkMGI2NGU5N2E5YjgxNDA4YTQyMWVhYmJkNjhmYmM1MDYyNzhjOThhZDA1MDIzY2VmMjQ0Nzk0M2IyMWFmZjJjODA1ZTk5Y2Y0NTFjYjA0MmEwNWQ2ZGRlMjllZDFmY2QwZGNmY2M5ZGVmZWYwNjhiM2MwNDhlNjdiZTk5OGRlNmRlMjY0YTMzZGNmNzFkZTI5MzA1Yjg5NWY3NjI2ZTUxNTQzZGVjNDBmZmMyYjY3N2Q2ZWYzZTdiNTA1YmExZjc4NzIyY2Y1Nzk1MzBhYjIxZmYzZGE2MTc3YzkwNjViMTU3YmY1MzQ1OWZiZmRhOTk2MzZjODZlNDY2ZmExNGI4MTI0MThiYmNhMTAzYzNjNDk5Mjg0OGVlMTNkZjdhZWYwNjMxN2E5NzdlZWI1MTk1NTE5YWE5ZjIxOTFhMGRmNjQxY2EyNzUyMzgzNDhkY2YyODQwNzE3OTAyOGQ2YzM5OGQwMWRkYzFlYTUxNDk3ZDAwZDQ1NGQ4YWQ2MDM2ODRjZTFkZjgwNGQ1NzE0YmM3M2VkYjg3ZmU0ZmQyMThmYTdlMjQwNzE2MTE=";

    static final String ACT_CODE = "54296617";

    static final String PIN = "4547";

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
        setContentView(mx.tesseract.sdk.v2.R.layout.activity_main);

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

                new CountDownTimer(10 * 60 * 1000, 1000) {

                    /**
                     *
                     * @param l
                     */
                    @Override
                    public void onTick(long l) {
                        otpTextView.setText(SDK.generateOTP(null));
                        timeoutTextView.setText(String.format("timeout %02d", (SDK.getTimeInterval()- SDK.getTimeout()) / 1000));
                    }

                    /**
                     *
                     */
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
        format("Token OTP/Response", SDK.generateOTP(null));
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
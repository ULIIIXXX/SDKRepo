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

    static final String TOKEN_NAME = "My Token 3715";

    static final String ENROLLMENT_STRING = "MDBjMmE0NGI0YjVhODg4YzJjMGI0ZWRjMTMyMzUzOTc2OTZmNjFhYWQ5Y2MxMzlmN2RlNjdjNTQwZmJkMjdhYTZiNjEzMTY3ZWJkMTU0NjIxN2E5ZWYyZGQzNmFkMGNlODE5MGVkMWNiNjQ1YTY1OTljZTgxMDI1MGM4ZTAwOTdiNGFhYzY5ZDk4M2U2ZGE3NTgyZWU5YThmYjhiY2JjNDY4YzkzNjhkNDk1ZTg1NTZlYjM2ZGEyZGIxYmU2YmYwNzJiYmJkYTlmNDU0YjkxOTBjOGRkOTIzYjMzMTQyOWUwNWExZjIyYmQ2OWU2ZTI1N2E0YzAzODJiMDZjY2JmODA5M2NjMjE3YmFkNDdmYWNkMWFlNGMyODIwZDYwMDU4MzkwYmQ0ZjlkN2Y3ZjJhNzYxZjBjYWIwNGY3ZGU4NWVmNWJjZjM1ZmIwY2QwNGQxZmZkNGRiZDY0YmM5Mjk1MzdmOWJjYTgxMjc2NjY5MDgxOTk3YTIyYTQ4MDgyYzc3ZTJiNDg0MzE3MjkwNTA4ZmViZTk0YjMyY2RkZGRhZjFjZDIxNWU0ZmE2MTQxZmMzZGJjYmJlMGJiNGNlNzdjYTBkMWRkNzE1MjJkMGI1ODU=";

    static final String ACT_CODE = "82395828";

    static final String PIN = "4547";

    static final String CHALLENGE = "375737";

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
                        otpTextView.setText(SDK.generateOTP(CHALLENGE));
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
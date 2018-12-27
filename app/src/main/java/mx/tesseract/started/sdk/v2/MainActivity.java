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

    static final String TOKEN_NAME = "My Token 6";

    static final String ENROLLMENT_STRING = "YWFiMWJkMTUzMmE0YTk0YTlkM2I4NDlmMGI1ZGU2ZmI0YjFmNzcyNDNmYjcwZGZlYWNlOGU3NWNjNTAzYjYxYmVlMjkzN2UyNGU1NDZhMmE3ZDRmZTNhYzNhY2NhZmRlMjRjYjY0ZWIzNjA5NjA4MmI0ODZjMTc3Y2Y3OGFjOGQ3ZmEwZjkxYzIzZjdhN2ViN2RiOTEyYzkyMGE0NjdmYjJiODRmMGIyZTA0N2E2NmE3YTM0YjliNTI0YjFlMmFiZjAxZjQ4ZjQ3M2JlYzc5NmE4NGY3NzM1MDBjNjYzMjk5OWVlMTU0NDg0YjA0ODVmMmJiMTQ3Y2JiYjhiZmE3OTQ2NjhmNjVjM2Q0NzI0MDI3YTk3OTcyNmZhNGI4NmJkZjBmMTc1NDdlM2UxMDdlZmIzMmIxMWVkNmU4ZWExZjdjMDc4NmNkZjUwY2NiNGMyODBlNjhlY2U1YTY4MDYyMzU5ZGI5YTZiMDIyM2FiNzVmNDBkNDdmNzZlNzZjZDJkMTk5N2MwMmVlY2NlMzE3YzE1ODEzMmZjZjQ1ZDIzZTI5MWUwODI4NGMzOTc3MDEz";

    static final String ACT_CODE = "90477585";

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
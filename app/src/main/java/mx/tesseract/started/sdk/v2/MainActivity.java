package mx.tesseract.started.sdk.v2;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;

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

    static final String TOKEN_NAME = "Test  2760"; //2305
    static final String ENROLLMENT_STRING = "YmZjNDIxMWNkZWMwNzkxNDc2YmUyMmE0MTllNjFmNjkwZDk4YWQ0NzVjNzVjOTMxZmNmYzg3OGViYzE5Njg5Y2IzZTU3MjYxYzJhYjU3OTNmNWZjMjg0MzcyN2JjNTM1MWY1NGNhNmUyYmRkZGQ0ZWRlNmIyOWZjMWZiNDYxNTk1YWFjMGQ2ODRlYmE1ZjJjMTcwYjEyZDI2ZDRhYjYwMjAwZjhlYWNkNzY2ZjYxYjRhODlkZDFhMzNmM2E3ZDY3NWUyODgwMDdlMjM5NDU3NGI3ZTk1M2YxODBkZmQ1NjRjYTcwNzhkMDAyOThhODIzNGYzNmYzMDgwMWY3MzRmZmZhZjliZjFkNzE3ODMwNmY2MDQ5NWUwMWM1YTQ3Y2M5MjM3YmFhMzdkODExNjQ4ZGY3ODBlZDcwMzM2MTVlMWZlODI2MzkxZDc0N2FjYmQxZTBmZTE1Zjc1MTUyYmI1YjBhYzJiYzdkMzYzNzMyMjA2NmRiNzNjMmRhZWU5YTIyMmUyZTAxMTBkN2EyYjBiZTZkZTkxZDY3OTBiZmI3MmQ5ZDliMjYxYTc5MjdlNDBiMmQ3OWY1MWUwMzY4NzE2NTFjMzYyYjE1OWM4YWYzOTQ=";
    static final String ACT_CODE = "12547365";
    static final String PIN = "7896";

    static final String CHALLENGE = "655009";

    //



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
        Button btn_token = (Button) findViewById(R.id.btn_token);
        Button btn_send_data = (Button) findViewById(R.id.btn_send_data);
        Button btn_pin = (Button) findViewById(R.id.btn_pin);
        Button btn_status = (Button) findViewById(R.id.btn_status);
        Button btn_activate = (Button) findViewById(R.id.btn_activate);
        Button btn_challenge =  (Button) findViewById(R.id.btn_challenge);
        Button btn_exp =  (Button) findViewById(R.id.btn_exp);
        btn_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Esto funciono",Toast.LENGTH_SHORT).show();
                SDK.createToken(TOKEN_NAME);
            }
        });

        btn_send_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.enrollmentToken(ENROLLMENT_STRING,ACT_CODE);
            }
        });

        btn_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.activate(new ActivationCallback() {
                    @Override
                    public void success() {
                        Toast.makeText(MainActivity.this,"Activado, se espera PIN",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failed(Result result) {
                        Toast.makeText(MainActivity.this,"ALgo ha fallado",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.setPin(PIN);
            }
        });

        btn_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.auth(PIN);
               // SDK.generateOTP(CHALLENGE);
                otpTextView.setText("El OTP es:" + " " + SDK.generateOTP(CHALLENGE));
               Toast.makeText(MainActivity.this,"Intentos restantes: " + SDK.getAttempts(),Toast.LENGTH_SHORT).show();
            }
        });

        btn_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Expira en:" + " " + SDK.getExpiresAt(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.selectToken(TOKEN_NAME);
                Toast.makeText(MainActivity.this,SDK.getStatus().toString(),Toast.LENGTH_SHORT).show();
            }
        });


        /*if(!SDK.existsToken(TOKEN_NAME))
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
              /*  SDK.activate(new ActivationCallback() {

                    /**
                     *
                     */
                /*    @Override
                    public void success() {
                        info("ACTIVATE TOKEN INFO");
                    }

                    /**
                     *
                     * @param result
                     */
                  /*  @Override
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
        }*/

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
        format("A ver venga ese serial", SDK.getSerial());
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
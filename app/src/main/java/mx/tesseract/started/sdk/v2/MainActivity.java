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

    static final String TOKEN_NAME = " Test 2870"; //2305
    static final String ENROLLMENT_STRING = "N2MyOTMxZDNhMDllZmZjYjcyYWQ3MDA3NmJlYTdhN2E4NWFkNThjY2JjZTNmYmEzNTFjOGFjOGE4ZGE4ODk1MWZiYjc0MmU5MjZlN2IwZGM5YjBkNzUyY2I5ZmIzZDRjMTRiYTUxYzJkYTU3ZGU1NzBhZmViMzVmOTY0N2ZlYzQzZTViNWI2NjA3M2RmODJiYTVhOThmNzY2MWE3N2YxNTI0YThhMmFhODhhMGQ5ZTc0ZjYzYTQzNDEyNTU1YWNjZjA3NDI2MGI3Mzc2NWI2ODE0YWU1MDcyZTFkOWU2MTU1NjAzODdlMjA0ZDI2M2M1NmIyZGJiOTAxNGUwYzYzMWM1OWM2NjhhZjRlZWQ3NWJiZTAyZjQyYTg5OTE3ZmFhMmUyOTdiOGU3YTNmYjZhZGU2ZGRhOTc5NDRiNTdmYTFlNzU0ZWIyYWQwNDBkNWRmNDQxODc4M2M4MDU1ZGVhMTExN2JhNjNkN2E4ZGNiMzE2NzE0ODM5MTU1ZWFlNGIxY2NmOGRlYzA3OTMzMzEwMzM0NTNlYWY3NzdkNGY2MTY1YzhiZDk2ZDQ2YTViM2VlZTMyMzBjOTdjOGEzNjExZmI3YTkwNzEzNThiZWYxZTY=";
    static final String ACT_CODE = "46856188";
    static final String PIN = "7896";

    static final String CHALLENGE = "377326";

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
        Button btn_delToken = (Button) findViewById(R.id.btn_delToken);
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

       btn_delToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.deleteToken(TOKEN_NAME);
                Toast.makeText(MainActivity.this,"Se ha eliminado el token",Toast.LENGTH_SHORT).show();
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
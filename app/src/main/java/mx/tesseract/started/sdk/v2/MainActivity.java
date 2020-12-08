package mx.tesseract.started.sdk.v2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    static final String TOKEN_NAME = " Test 3775";
    static final String ENROLLMENT_STRING = "MDE2YTk0OTU3ZmE2OWY5ODYzZGJmOTAxMTdlMWM0M2I3ZmYyMWVkNmMxZjA4Y2M4OWZkNjdmNDcwYzM4ZTI2ZDE1MTc2YmMyNGE2NzFmZDZmMTNkNGFjNmFmMjM1NWI2NTNmZjM4ZTY1ZDNlYzM5YzRhYjI2ODIzYTJiY2JmNjlkYWMzNzVkMWE3NWZmYTY0ZjllN2E5YzY2YTIxYzMzNzAxZmNmMjgwODA2MzZkNzQ1YTkwMDkxNTU1NjBiYmI0ZjZhYWVkZWE4NzA0YmJmMmUyM2ZhNTMxOGQ3NTUwODYxNmRkMjk0NDU1MjI5Zjk0YmY2ZGMyNTUzMjljMzQ5OGMzMWIyZGZkM2NlZmVmZDA0MjUwMDA5OGU4YWVjNWEwM2E2M2NhY2ZiYjUyMGViMTA0N2JiMzZkNDEwMzk0YjVlNTY1YjNjZWE3ZmY0YWVlMzVkODg3ZTZkNmIyOTcwMzcxODliYTBkZjA5ZmI5NmJlMDRhYTAyNjMyNzliYWM5NDQ1YzZkNjFhNTk1Y2FjNjkzMzA2YzZiZjU2NTZmOTE2NDQzNGY0MWY3NTBiYTI1ZWZkZTgzYTI1NDlkNzFmMjk3Y2I2NTcxYzNlMmViYmQ=";
    static final String ACT_CODE = "02123390";
    static final String PIN = "7896";

    static final String CHALLENGE = "377326"; //Simula ser un token generado por el tesseract api , no lo usaremos en esta ocasion

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
        Button btn_createToken = findViewById(R.id.btn_createToken);
        Button btn_send_enroll_act_code = findViewById(R.id.btn_send_enroll_act_code);
        Button btn_pin = findViewById(R.id.btn_pin);
        Button btn_selectToken = findViewById(R.id.btn_selectToken);
        Button btn_activate = findViewById(R.id.btn_activate);
        Button btn_OTP = findViewById(R.id.btn_OTP);
        Button btn_status = findViewById(R.id.btn_status);
        Button btn_delToken = findViewById(R.id.btn_delToken);


        //1 Creacion del token dentro del dispositivo movil
        btn_createToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.createToken(TOKEN_NAME); //Paso el nombre como parametro de la funcion
            }
        });

        //2 seleccionamos el token

        btn_selectToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.selectToken(TOKEN_NAME);
            }
        });


        //3 Asignacion de cadena de enrolamiento y codigo de activacion
        btn_send_enroll_act_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.enrollmentToken(ENROLLMENT_STRING,ACT_CODE); // Paso cadena de enrolamiento y codigo de activacion como parametro
            }
        });



        //4 Activamos el Token
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

        // 5 Enviamos el pin de autenticacion
        btn_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.setPin(PIN);
            }
        });

        //6 Consultar token status
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Estado del token:" + " " + SDK.getStatus(), Toast.LENGTH_SHORT).show();
            }
        });


        //7 Generamos el OTP
        btn_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.auth(PIN);
                otpTextView.setText("El OTP es:" + " " + SDK.generateOTP("null"));
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
    /*private static void info(String s)
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
    }*/

    /**
     *
     * @param field
     * @param value
     */
   /* private static final void format(String field, Object value)
    {
        Log.i(TAG, String.format("| %-20s | %-28s |", field, value == null ? "EMPTY" : value));
        Log.i(TAG, String.format("+%22s+%30s+", "", "").replace(' ','-'));
    }*/

}
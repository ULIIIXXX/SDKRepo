package mx.tesseract.started.sdk.v2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import mx.tesseract.sdk.v2.SDK;
import mx.tesseract.sdk.v2.domain.Result;
import mx.tesseract.sdk.v2.domain.actions.ActivationCallback;

public class MainActivity extends AppCompatActivity {

    static final String TAG = MainActivity.class.getName();

    static final String tokenName = "Tesseract v2";

    static final String ENROLLMENT_STRING = "MDI0MWNiNGUzOWNkZDY5MjhmNGU4MDAwMWJjNzBkYjRlOGFkZmQ1YmMxZmI3MjMxNzA0MDk0OTkzMjVmNzc3YWE3Y2M0YTE1YjIwMjNiMWQxMjI3ZjQ0ZTEwZThlNzdmOTMyM2E3ODcxZjNjZmRhODZkN2RhYjI2ODlkYjYxMzdlOTc5NTdlNDU4MmQ1ZGUxM2Q5NWM3NmUzNWI5YzVkZDYzYjlmMzMyODUxM2VmNTIxMWIyMWQzMzQ4YjRhNTM3NmFhYjE0Njk5MDA4NWViNmIyZjFmYmY3ZjI1ZDZkN2U0OTJjOTE5YTFkZTk2N2FmYzJjYjJiZGJiZTg0NDFiOWE4ZjMwOTI1NDQ0ZTY0ZDY0MzkwYzU1YjY4NjQyNmNhNjliNzBkNzMxNTMyY2EzZjI1NThiMTY3Yzc4MDEyOTk0ODAzMjI2OWIxMmU3MmQwNmZkMjkwMzJiMmQ3YTFiNTI3YjE1NDhjYWY2Y2MzYWFmZTY2Yzg1NWFhMDEwM2MzYzczMTA0YzYxM2I0YmVhNTAzMmExNDRkMjk2MDU4ZGFiY2U0NDQ5ZDBlNjkzZWQ4";

    static final String ACT_CODE = "84680617";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        int permissionCheck = ContextCompat.checkSelfPermission(
//                this, Manifest.permission.READ_PHONE_STATE);
//
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            Log.i("Mensaje", "No se tiene permiso.");
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 225);
//        } else {
//            Log.i("Mensaje", "Se tiene permiso!");
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Result result;

        if(SDK.existsToken(tokenName))
        {
            result = SDK.deleteToken(tokenName);

            if(result != Result.SUCCESS)
            {
                Toast.makeText(this, String.format("Error %s", result), Toast.LENGTH_LONG).show();
                return;
            }

        }

        result = SDK.createToken(tokenName);

        if(result != Result.SUCCESS)
        {
            Toast.makeText(this, String.format("Error %s", result), Toast.LENGTH_LONG).show();
            return;
        }

        result = SDK.selectToken(tokenName);

        if(result != Result.SUCCESS)
        {
            Toast.makeText(this, String.format("Error %s", result), Toast.LENGTH_LONG).show();
            return;
        }

        Log.i(TAG, String.format("| %20s | %20s |", "Name",SDK.getTokenName()));
        Log.i(TAG, String.format("| %20s | %20s |", "Type",SDK.getType()));
        Log.i(TAG, String.format("| %20s | %20s |", "Status",SDK.getStatus()));
        Log.i(TAG, String.format("| %20s | %20s |", "Serial",SDK.getSerial()));

        switch (SDK.getStatus())
        {
            case CREATED:
                enrollment();
                break;
            case ENROLLMENT:
                // activate();
                break;
            case WAITING_PIN:
                // setPin();
                break;
            case READY:
                // generateOtp();
                break;
        }

    }

    private void enrollment()
    {
        Result result = SDK.enrollmentToken(ENROLLMENT_STRING, ACT_CODE);

        if(result != Result.SUCCESS)
        {
            Toast.makeText(this, String.format("Error %s", result), Toast.LENGTH_LONG).show();
            return;
        }

        Log.i(TAG, String.format("| %20s | %20s |", "Name",SDK.getTokenName()));
        Log.i(TAG, String.format("| %20s | %20s |", "Type",SDK.getType()));
        Log.i(TAG, String.format("| %20s | %20s |", "Status",SDK.getStatus()));
        Log.i(TAG, String.format("| %20s | %20s |", "Serial",SDK.getSerial()));

        activate();

    }

    /**
     *
     */
    private void activate()
    {
        SDK.activate(new ActivationCallback() {

            /**
             *
             */
            @Override
            public void success() {
                // generateOTP();
            }

            /**
             *
             * @param result
             */
            @Override
            public void failed(Result result) {
                Log.i(TAG, String.format("Error %s", result));
            }

        });
    }

}

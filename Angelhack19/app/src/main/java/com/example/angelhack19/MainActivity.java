package com.example.angelhack19;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private long lastUpdate;
    TextView sensorValue;
    static String BASE_URL="https://gfex1np0cj.execute-api.us-east-1.amazonaws.com/dev/users/create/";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.textView);
        view.setBackgroundColor(Color.GREEN);

        sensorValue=(TextView)findViewById(R.id.senstextView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        findViewById(R.id.parent).setBackgroundColor(Color.GREEN);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    public String globalValue;

    private void getAccelerometer(SensorEvent event) {

        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        sensorValue.setText(globalValue+","+Float.toString(y));


        long actualTime = event.timestamp;

        if (y<=-3 || y>=5) //
        {
            if (actualTime - lastUpdate < 500) {
                return;
            }

            lastUpdate = actualTime;

            Toast.makeText(this, "Detecting Hit", Toast.LENGTH_SHORT)
                    .show();

            new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    Log.i("tag", "This'll run 300 milliseconds later");
                    findViewById(R.id.parent).setBackgroundColor(Color.GREEN);
                }
            },
            1000);

            globalValue=Float.toString(y);


            findViewById(R.id.parent).setBackgroundColor(Color.RED);
            MainActivity.BASE_URL=MainActivity.BASE_URL+"?xy="+x+"&yz="+y+"&zx="+z;
//            MainActivity.BASE_URL="https://gfex1np0cj.execute-api.us-east-1.amazonaws.com/dev/users/create/?xy=-0.09881349&yz=4.0324774&zx=8.965692/";


            com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            };

                VolleyLog.DEBUG = true;
                RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
                String uri_page_one = String.format(MainActivity.BASE_URL);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(uri_page_one, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, errorListener) {

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.IMMEDIATE;
                    }
                };

                queue.add(jsonObjectRequest);
        }




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}

package com.abia.weatherforcast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    TextView longitude, latitude, pressure, weather, wind;
    String lon, lat, pres, weat, win;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instanciating android networking library
        AndroidNetworking.initialize(getApplicationContext());

        // instanciating progressDialog
        dialog = new ProgressDialog(this);

        // instanciating textviews
        longitude = findViewById(R.id.tvLongitude);
        latitude = findViewById(R.id.tvLatitude);
        pressure = findViewById(R.id.tvPressure);
        weather = findViewById(R.id.tvWeather);
        wind = findViewById(R.id.tvwind);

        processNetwork();


    }

    private void processNetwork() {
        dialog.setMessage("Fetching Weather Details");
        dialog.show();

        AndroidNetworking.get("http://api.openweathermap.org/data/2.5/group?id=524901,703448,2643743&units=metric ")
//                .addPathParameter("pageNumber", "0")
                .addHeaders("appid", "b6907d289e10d714a6e88b30761fae22")
                .addQueryParameter("limit", "3")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        dialog.dismiss();

                        Log.d("succ1",response.toString() );
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onError(ANError anError) {

                        dialog.dismiss();

                        Log.d("error1",anError.toString());
//                        Log.d("error1", anError.getErrorBody());
//                        Log.d("error1", anError.getErrorDetail());
                        Toast.makeText(getApplicationContext(), anError.toString(), Toast.LENGTH_LONG).show();


                    }
                });

    }


}


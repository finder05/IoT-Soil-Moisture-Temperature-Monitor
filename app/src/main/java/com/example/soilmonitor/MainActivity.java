package com.example.soilmonitor;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Moisture Views
    TextView tvMoisture, tvMoistureStatus;
    ProgressBar progressMoisture;

    // Temperature Views
    TextView tvTemperature, tvTempStatus;
    ProgressBar progressTemp;

    // Metadata
    TextView tvLastUpdated;

    // Controls
    Button btnStartMonitoring, btnStopMonitoring;

    // Real-time simulation using Handler
    Handler handler = new Handler();
    Runnable sensorRunnable;
    boolean isMonitoring = false;

    // Simulated sensor data generator
    SensorSimulator simulator = new SensorSimulator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Moisture widgets
        tvMoisture       = findViewById(R.id.tvMoisture);
        tvMoistureStatus = findViewById(R.id.tvMoistureStatus);
        progressMoisture = findViewById(R.id.progressMoisture);

        // Temperature widgets
        tvTemperature = findViewById(R.id.tvTemperature);
        tvTempStatus  = findViewById(R.id.tvTempStatus);
        progressTemp  = findViewById(R.id.progressTemp);

        // Metadata
        tvLastUpdated = findViewById(R.id.tvLastUpdated);

        // Buttons
        btnStartMonitoring = findViewById(R.id.btnStartMonitoring);
        btnStopMonitoring  = findViewById(R.id.btnStopMonitoring);

        // Define the repeating sensor poll (every 2 seconds)
        sensorRunnable = new Runnable() {
            @Override
            public void run() {
                if (isMonitoring) {
                    updateSensorUI();
                    handler.postDelayed(this, 2000); // poll every 2 seconds
                }
            }
        };

        btnStartMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMonitoring) {
                    isMonitoring = true;
                    handler.post(sensorRunnable);
                    Toast.makeText(MainActivity.this,
                            "Monitoring started", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStopMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMonitoring = false;
                handler.removeCallbacks(sensorRunnable);
                Toast.makeText(MainActivity.this,
                        "Monitoring stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void updateSensorUI() {
        int moisture = simulator.getMoisture();   // 0–100 %
        int temp     = simulator.getTemperature(); // 0–60 °C

        // Update moisture
        tvMoisture.setText(moisture + " %");
        progressMoisture.setProgress(moisture);
        tvMoistureStatus.setText("Status: " + getMoistureStatus(moisture));

        // Update temperature
        tvTemperature.setText(temp + " °C");
        progressTemp.setProgress(temp);
        tvTempStatus.setText("Status: " + getTempStatus(temp));

        // Timestamp
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                .format(new Date());
        tvLastUpdated.setText("Last Updated: " + time);

        // Alert on critical values
        if (moisture < 20) {
            Toast.makeText(this, "⚠️ Low Moisture! Irrigate now.", Toast.LENGTH_SHORT).show();
        }
        if (temp > 45) {
            Toast.makeText(this, "⚠️ High Temperature! Check crop.", Toast.LENGTH_SHORT).show();
        }
    }

    String getMoistureStatus(int m) {
        if (m < 20)  return "🔴 Very Dry";
        if (m < 40)  return "🟡 Dry";
        if (m < 70)  return "🟢 Optimal";
        return "🔵 Wet";
    }

    String getTempStatus(int t) {
        if (t < 15)  return "🔵 Cold";
        if (t < 30)  return "🟢 Optimal";
        if (t < 45)  return "🟡 Warm";
        return "🔴 Too Hot";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(sensorRunnable);
    }
}

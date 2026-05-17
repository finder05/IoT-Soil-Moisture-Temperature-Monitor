package com.example.soilmonitor;

import java.util.Random;

/**
 * SensorSimulator generates realistic-looking IoT sensor readings.
 * In a real application, replace this with MQTT / HTTP API calls
 * to your actual IoT gateway or Firebase Realtime Database reads.
 */
public class SensorSimulator {

    private final Random random = new Random();
    private int lastMoisture    = 50;
    private int lastTemperature = 28;

    /**
     * Returns simulated soil moisture (0–100 %).
     * Drifts gradually to mimic real sensor behaviour.
     */
    public int getMoisture() {
        int delta = random.nextInt(7) - 3; // -3 to +3
        lastMoisture = Math.max(0, Math.min(100, lastMoisture + delta));
        return lastMoisture;
    }

    /**
     * Returns simulated temperature in Celsius (0–60 °C).
     */
    public int getTemperature() {
        int delta = random.nextInt(5) - 2; // -2 to +2
        lastTemperature = Math.max(0, Math.min(60, lastTemperature + delta));
        return lastTemperature;
    }
}

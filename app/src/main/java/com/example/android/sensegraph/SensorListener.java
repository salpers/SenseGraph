package com.example.android.sensegraph;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

public class SensorListener implements SensorEventListener {

    private RecordActivity monitor;
    private static int id_count = 0;
    private int myId;
    private long timeStamp;

    public SensorListener(RecordActivity monitor){
        this.monitor = monitor;
        myId = id_count++;
        timeStamp = System.currentTimeMillis();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        double s = (System.currentTimeMillis()-timeStamp)/1000.0;
        double value = event.values[0];
        Log.d("SensorChange",Thread.currentThread().getName());
        monitor.updateData(new DataPoint(s,value),myId);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

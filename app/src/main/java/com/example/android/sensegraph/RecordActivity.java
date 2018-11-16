package com.example.android.sensegraph;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private GraphView mGraph;
    private LineGraphSeries<DataPoint>[] mSeries;
    private SQLiteHelper myHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private FloatingActionButton mStopFabButton;
    private int sessionId;
    private int[] chosenSensorTypes;
    private HandlerThread threads[];
    private SensorListener[] sensorListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Intent intent = getIntent();
        chosenSensorTypes = intent.getIntArrayExtra(MainActivity.SENSORMESSAGE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myHelper = new SQLiteHelper(this);
        mSqLiteDatabase = myHelper.getWritableDatabase();
        mGraph = findViewById(R.id.tv_rec_graph);
        mSeries = new LineGraphSeries[chosenSensorTypes.length];
        mStopFabButton = findViewById(R.id.fab_recStop);
        threads = new HandlerThread[chosenSensorTypes.length];
        sensorListeners = new SensorListener[chosenSensorTypes.length];
        setSessionId();
        setUpGraphview();
        setUpFabButton();
        createSensorThreads();
    }

    public void setSessionId() {
        Cursor cs = mSqLiteDatabase.query(SQLiteHelper.tableName, new String[]{SQLiteHelper.cols[3]}, null, null, null, null, SQLiteHelper.cols[3] + " DESC");
        if (cs.getCount() == 0) {
            Log.d("SessionID", "nothing found SID = 1");
            sessionId = 1;
        } else {
            cs.moveToNext();
            Log.d("SessionID", "last session id: " + cs.getInt(0));
            sessionId = cs.getInt(0) + 1;
        }
    }

    private void setUpGraphview() {
        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinX(0);
        mGraph.getViewport().setMaxX(30);

        mGraph.getViewport().setYAxisBoundsManual(true);
        mGraph.getViewport().setMinY(-1000);
        mGraph.getViewport().setMaxY(50000);
        mGraph.getViewport().setScrollable(true);
        mGraph.getViewport().setScalable(true);
        mGraph.getGridLabelRenderer().setNumHorizontalLabels(5);
    }

    private void setUpFabButton() {
        mStopFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
                displayDataFromSQL();
            }
        });

    }

    private void displayDataFromSQL() {
        for (int i = 0; i < mSeries.length; i++) {
            mSeries[i].resetData(getData(i));
        }
    }

    private void createSensorThreads() {
        for (int i = 0; i < chosenSensorTypes.length; i++) {
            Sensor mSensor = mSensorManager.getDefaultSensor(chosenSensorTypes[i]);
            Log.d("SensorInfo:", mSensor == null ? "Sensor null, Type = "+chosenSensorTypes[i]+" "+mSensorManager.getSensorList(Sensor.TYPE_ALL).get(chosenSensorTypes[i]) : "Sensor created = "+mSensor.getType()+" " + mSensor.getName());
            HandlerThread mSensorThread = new HandlerThread("SensorThread " + i, Thread.MAX_PRIORITY);
            mSensorThread.start();
            Handler mSensorHandler = new Handler(mSensorThread.getLooper());
            SensorListener mSensorListener = new SensorListener(this);
            LineGraphSeries<DataPoint> mSeriesEle = new LineGraphSeries<DataPoint>();
            mGraph.addSeries(mSeriesEle);
            mSeries[i] = mSeriesEle;
            threads[i] = mSensorThread;
            sensorListeners[i] = mSensorListener;
            mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL, mSensorHandler);
        }
        Log.d("SensorInfo", "SensorThreadCreation finished");
    }

    private void stopRecording() {
        for (int i = 0; i  < threads.length; i++){
            Log.d("Threadcheck",threads[i]==null?"Thread null":"Thread not null");
            threads[i].quitSafely();
            mSensorManager.unregisterListener(sensorListeners[i]);
        }
    }

    public void updateData(DataPoint dp, int threadId) {
        Log.d("mSeriesLog", mSeries[threadId]==null?"series null "+threadId:"series ok");
        mSeries[threadId].appendData(dp, dp.getX() >= 30, 10000);
        myHelper.insertData(dp.getX(), dp.getY(), chosenSensorTypes[threadId], sessionId);
    }

    private DataPoint[] getData(int threadId){
        Log.d("test","query: "+ threadId + " " + sessionId);
        Cursor cursor = mSqLiteDatabase.query(SQLiteHelper.tableName,SQLiteHelper.cols, "sessionId = "+sessionId+ " and sensorId = "+chosenSensorTypes[threadId],null,null,null,null);
        DataPoint[] dps = new DataPoint[cursor.getCount()];
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            dps[i] = new DataPoint(cursor.getDouble(0), cursor.getDouble(1));
        }
        Log.d("test", Arrays.toString(dps));
        return dps;
    }
}

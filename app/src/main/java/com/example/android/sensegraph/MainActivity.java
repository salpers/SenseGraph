package com.example.android.sensegraph;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements RecAdapter.ListItemClickListener, SelectSensorDialogFragment.NoticeDialogListener {

    private static final int NUM_LIST_ITEMS = 10;
    public static final String SENSORMESSAGE = "Selected Sensors:";

    private RecAdapter mAdapter;
    private RecyclerView mRecsList;
    private Toast mToast;
    private FloatingActionButton fab;
    private SensorManager mSensorManager;
    private List<Sensor> sensorList;
    private SQLiteHelper mHelper;
    private SQLiteDatabase mSqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecsList = (RecyclerView) findViewById(R.id.rv_graphs);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        fab = findViewById(R.id.fab_recStart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSensorSelectDialog();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecsList.setLayoutManager(layoutManager);
        mRecsList.setHasFixedSize(true);
        mAdapter = new RecAdapter(NUM_LIST_ITEMS, this);
        /*mAdapter = new RecAdapter(NUM_LIST_ITEMS, this, getDataPoints());*/
        mRecsList.setAdapter(mAdapter);


    }

    private DataPoint[][][] getDataPoints() {
        int numberOfSessions;
        //get NumberOfSessions
        Cursor cursor = mSqLiteDatabase.query(SQLiteHelper.tableName, SQLiteHelper.cols, null, null, null, null, SQLiteHelper.cols[3] + " DESC");
        if (cursor.getCount() == 0) {
            numberOfSessions = 0;
        } else {
            cursor.moveToNext();
            numberOfSessions = cursor.getInt(3);
        }
        //get each set for each session
    return null;
    }


    private void showSensorSelectDialog() {
        SelectSensorDialogFragment newFragment = new SelectSensorDialogFragment();
        newFragment.setmSensorList(getSensorNamesList());
        newFragment.show(getSupportFragmentManager(), "missiles");
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }*/

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }

        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();
    }


    @Override
    public void onDialogPositiveClick(SelectSensorDialogFragment dialog) {
        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra(SENSORMESSAGE, dialog.getSelectedItems());
        startActivity(intent);

    }

    @Override
    public void onDialogNegativeClick(SelectSensorDialogFragment dialog) {
        //DO NOTHING
    }

    public Sensor[] getSensorList() {
        Sensor[] sl = new Sensor[sensorList.size()];
        for (int i = 0; i < sl.length; i++) {
            sl[i] = sensorList.get(i);
        }
        return sl;
    }

    public String[] getSensorNamesList() {
        String[] sl = new String[sensorList.size()];
        for (int i = 0; i < sl.length; i++) {
            sl[i] = sensorList.get(i).getName();
        }
        return sl;
    }
}

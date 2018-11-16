package com.example.android.sensegraph;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SQLiteHelper extends SQLiteOpenHelper {
    final static public String tableName = "SenseGraphTable";
    final static private String col1 = "ms", col2 = "data", col3="sensorId", col4 = "sessionId";
    final static public String[] cols ={col1,col2,col3,col4};
        /*final static public String col4 = "sensorType";*/
    private Context context;

    public SQLiteHelper(Context context) {
        super(context, "Database", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableString = "create table " + tableName + "(" + col1 + " DOUBLE," + col2 + " DOUBLE," + col3 + " INT,"+col4+" INT);";
        Log.d("SQLITEHELPER", createTableString);
        db.execSQL(createTableString);
        Toast.makeText(context, "Table created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(double x, double y,int sensorId, int sessionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1, x);
        contentValues.put(col2, y);
        contentValues.put(col3, sensorId);
        contentValues.put(col4, sessionId);
        db.insert(tableName,null,contentValues);
        Log.d("DBHelper","inserted Data sensorID="+col3+"sessionId="+col4);
    }
}

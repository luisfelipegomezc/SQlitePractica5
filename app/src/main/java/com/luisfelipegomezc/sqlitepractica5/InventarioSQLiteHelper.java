package com.luisfelipegomezc.sqlitepractica5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FELIPE on 05/05/2016.
 */
public class InventarioSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME="InventarioDB";
    private static final int DATA_VERSION=1;
    String sqlCreate = "CREATE TABLE Inventario (id_peluche INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, cantidad INTEGER, valor INTEGER)";
    public InventarioSQLiteHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Inventario");
        db.execSQL(sqlCreate);
    }
}

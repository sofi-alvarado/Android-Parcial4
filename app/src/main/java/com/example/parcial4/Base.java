package com.example.parcial4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Base extends SQLiteOpenHelper {
    public Base(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;

        query = "CREATE TABLE IF NOT EXISTS Compras(Id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query;
        query = "DROP TABLE IF EXISTS Compras";
        db.execSQL(query);
        onCreate(db);
    }
}

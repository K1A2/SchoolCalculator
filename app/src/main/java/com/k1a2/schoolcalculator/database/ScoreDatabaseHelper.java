package com.k1a2.schoolcalculator.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDatabaseHelper extends SQLiteOpenHelper {

    public ScoreDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String q = DatabaseKey.KEY_TABLE_11;
        final String w = DatabaseKey.KEY_TABLE_12;
        final String e = DatabaseKey.KEY_TABLE_21;
        final String r = DatabaseKey.KEY_TABLE_22;
        final String t = DatabaseKey.KEY_TABLE_31;
        final String y = DatabaseKey.KEY_TABLE_32;
        sqLiteDatabase.execSQL("CREATE TABLE\"" + q +"\" (\"subject\" TEXT NOT NULL, \"grade\" INTEGER NOT NULL, \"point\" INTEGER NOT NULL, \"type\" INTEGER NOT NULL, \"position\" INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE\"" + w +"\" (\"subject\" TEXT NOT NULL, \"grade\" INTEGER NOT NULL, \"point\" INTEGER NOT NULL, \"type\" INTEGER NOT NULL, \"position\" INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE\"" + e +"\" (\"subject\" TEXT NOT NULL, \"grade\" INTEGER NOT NULL, \"point\" INTEGER NOT NULL, \"type\" INTEGER NOT NULL, \"position\" INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE\"" + r +"\" (\"subject\" TEXT NOT NULL, \"grade\" INTEGER NOT NULL, \"point\" INTEGER NOT NULL, \"type\" INTEGER NOT NULL, \"position\" INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE\"" + t +"\" (\"subject\" TEXT NOT NULL, \"grade\" INTEGER NOT NULL, \"point\" INTEGER NOT NULL, \"type\" INTEGER NOT NULL, \"position\" INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE\"" + y +"\" (\"subject\" TEXT NOT NULL, \"grade\" INTEGER NOT NULL, \"point\" INTEGER NOT NULL, \"type\" INTEGER NOT NULL, \"position\" INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE \"Type\" (\"_id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"type\" TEXT NOT NULL);");

        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO \"Type\" VALUES (null, \'국어 과목\')");
        db.execSQL("INSERT INTO \"Type\" VALUES (null, \'수학 과목\')");
        db.execSQL("INSERT INTO \"Type\" VALUES (null, \'과학탐구 과목\')");
        db.execSQL("INSERT INTO \"Type\" VALUES (null, \'사회탐구 과목\')");
        db.execSQL("INSERT INTO \"Type\" VALUES (null, \'기타 과목\')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String table, String subject, int grade, int point, int type) {

    }
}

package com.k1a2.schoolcalculator.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**성적을 저장하고 꺼내오는 데이터베이스
 * 왠만하면 수정금지**/

public class ScoreDatabaseHelper extends SQLiteOpenHelper {

    public ScoreDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //데이터베이스 초기화
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String q = DatabaseKey.KEY_TABLE_11;
        final String w = DatabaseKey.KEY_TABLE_12;
        final String e = DatabaseKey.KEY_TABLE_21;
        final String r = DatabaseKey.KEY_TABLE_22;
        final String t = DatabaseKey.KEY_TABLE_31;
        final String y = DatabaseKey.KEY_TABLE_32;
        sqLiteDatabase.execSQL("CREATE TABLE \'11\' (\'id\' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, subject TEXT NOT NULL, grade INTEGER NOT NULL, point INTEGER NOT NULL, type INTEGER NOT NULL, position INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE \'12\' (\'id\' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, subject TEXT NOT NULL, grade INTEGER NOT NULL, point INTEGER NOT NULL, type INTEGER NOT NULL, position INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE \'21\' (\'id\' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, subject TEXT NOT NULL, grade INTEGER NOT NULL, point INTEGER NOT NULL, type INTEGER NOT NULL, position INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE \'22\' (\'id\' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, subject TEXT NOT NULL, grade INTEGER NOT NULL, point INTEGER NOT NULL, type INTEGER NOT NULL, position INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE \'31\' (\'id\' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, subject TEXT NOT NULL, grade INTEGER NOT NULL, point INTEGER NOT NULL, type INTEGER NOT NULL, position INTEGER NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE \'32\' (\'id\' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, subject TEXT NOT NULL, grade INTEGER NOT NULL, point INTEGER NOT NULL, type INTEGER NOT NULL, position INTEGER NOT NULL);");
//        sqLiteDatabase.execSQL("CREATE TABLE \"Type\" (\"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"type\" TEXT NOT NULL);");
//
//        sqLiteDatabase.execSQL("INSERT INTO \"Type\" VALUES (null, \'국어 과목\')");
//        sqLiteDatabase.execSQL("INSERT INTO \"Type\" VALUES (null, \'수학 과목\')");
//        sqLiteDatabase.execSQL("INSERT INTO \"Type\" VALUES (null, \'과학탐구 과목\')");
//        sqLiteDatabase.execSQL("INSERT INTO \"Type\" VALUES (null, \'사회탐구 과목\')");
//        sqLiteDatabase.execSQL("INSERT INTO \"Type\" VALUES (null, \'기타 과목\')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //데이터 존제여부 검사 함수
    public boolean isExisit(String table, int position) {
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT EXISTS (SELECT * FROM \'" + table + "\' WHERE position = \'" + position + "\') as success", null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return  true;
        } else {
            cursor.close();
            return  false;
        }
    }

    //성적 데이터 추가 함수
    public void insert(String table, String subject, int grade, int point, int type, int position) {
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO \'" + table + "\' VALUES (null, \'" + subject + "\', \'" + grade + "\', \'" + point + "\', \'" + type + "\', \'" + position + "\');");
    }

    //성적 데이터 수정 함수
    public void update(String table, String type, String value, int position) {
        final SQLiteDatabase db = getWritableDatabase();
        if (!type.equals(DatabaseKey.KEY_VALUE_SUBJECT)) {
            if (!type.equals(DatabaseKey.KEY_VALUE_TYPE)) {
                if (value.isEmpty()) {
                    value = "1";
                }
            } else {
                if (value.isEmpty()) {
                    value = "0";
                }
            }
            db.execSQL("UPDATE \'" + table + "\' SET " + type + " = \'" + Integer.parseInt(value) + "\' WHERE position = " + position +";");
        } else {
            db.execSQL("UPDATE \'" + table + "\' SET " + type + " = \'" + value + "\' WHERE position = " + position +";");
        }
    }

    //성적 데이터 삭제하는 함수
    public void delete(String table, int position) {
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM \'" + table + "\' WHERE position=" + position + ";");
        final Cursor cursor = db.rawQuery("SELECT * FROM \'" + table + "\'", null);
        for (int i = 0;i<cursor.getCount();i++) {
            cursor.moveToPosition(i);
            final Cursor cursor1 = db.rawQuery("SELECT * FROM \'" + table + "\' " + "WHERE position = \'" + cursor.getInt(5) + "\';", null);
            cursor1.moveToLast();
            final int id = cursor1.getInt(0);
            db.execSQL("UPDATE \'" + table + "\' SET position = " + i + " WHERE id = " + id + ";");
        }
    }

    //데이터배이스의 정보 가져오는 함수(기억 잘 안남)
    public ArrayList<String[]> getScores(String table) {
        final ArrayList<String[]> values = new ArrayList<>();
        final SQLiteDatabase db = getWritableDatabase();
        try {
            final Cursor cursor = db.rawQuery("SELECT * FROM \'" + table + "\'", null);
            for (int i = 0;i<cursor.getCount();i++) {
                cursor.moveToPosition(i);
                values.add(new String[]{cursor.getString(1), String.valueOf(cursor.getInt(2)), String.valueOf(cursor.getInt(3)), String.valueOf(cursor.getInt(4))});
            }
            return values;
        } catch (Exception e) {
            return null;
        }
    }

    //3년의 들급과 단위 모두 리턴
    public ArrayList<Integer[]> getGP(String table) {
        final ArrayList<Integer[]> values = new ArrayList<>();
        final SQLiteDatabase db = getWritableDatabase();
        try {
            final Cursor cursor = db.rawQuery("SELECT grade, point FROM \'" + table + "\'", null);
            if (cursor.getCount() == 0) {
                return null;
            } else {
                for (int i = 0;i<cursor.getCount();i++) {
                    cursor.moveToPosition(i);
                    values.add(new Integer[]{cursor.getInt(0), cursor.getInt(1)});//0 = grade등급, 1= point단위
                }
                return values;
            }
        } catch (Exception e) {
            return null;
        }
    }

    //한 과목 계열의 각 학기당 성적 리턴
    public ArrayList<Float> getTP(int type) {
        final ArrayList<Float> values = new ArrayList<>();
        final SQLiteDatabase db = getWritableDatabase();
        String[] table = new String[] {String.valueOf(11), String.valueOf(12), String.valueOf(21) , String.valueOf(22), String.valueOf(31), String.valueOf(32)};
        try {
            for (int i = 0;i < 6;i++) {
                final Cursor cursor = db.rawQuery("SELECT * FROM \'" + table[i] + "\' WHERE type = " + type, null);
                float pA = 0;
                float gA = 0;
                for (int s = 0;s<cursor.getCount();s++) {
                    cursor.moveToPosition(s);
                    int g = cursor.getInt(2);
                    int p = cursor.getInt(3);

                    gA += p*g;
                    pA += p;
                }
                values.add((float) (Math.round(gA/pA)));
            }
            return values;
        } catch (Exception e) {
            return null;
        }
    }

    //한 괴목계열을 총합산 성적 리턴
    public float getTPA(int type) {
        final SQLiteDatabase db = getWritableDatabase();
        String[] table = new String[] {String.valueOf(11), String.valueOf(12), String.valueOf(21) , String.valueOf(22), String.valueOf(31), String.valueOf(32)};
        try {
            float pA = 0;
            float gA = 0;
            for (int i = 0;i < 6;i++) {
                final Cursor cursor = db.rawQuery("SELECT * FROM \'" + table[i] + "\' WHERE type = " + type, null);
                for (int s = 0;s<cursor.getCount();s++) {
                    cursor.moveToPosition(s);
                    int g = cursor.getInt(2);
                    int p = cursor.getInt(3);

                    gA += p*g;
                    pA += p;
                }
            }
            return (float) (Math.round(gA/pA));
        } catch (Exception e) {
            return Float.NaN;
        }
    }
}

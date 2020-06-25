package com.k1a2.schoolcalculator.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;
import java.util.ArrayList;

/**성적을 저장하고 꺼내오는 데이터베이스
 * 왠만하면 수정금지**/

public class TestScoreDatabaseHelper extends SQLiteOpenHelper {

    private Context context = null;

    public TestScoreDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    //데이터베이스 초기화
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public String createTable(String name) {
        final SQLiteDatabase db = getWritableDatabase();
        int r = seekTable(name);
        if (r == 0) {
            db.execSQL("CREATE TABLE \"" + name + "\" ('id' INTEGER PRIMARY KEY AUTOINCREMENT, \"isIn\" INTEGER NOT NULL, \"subject\" INTEGER NOT NULL, \"origin\" INTEGER NOT NULL, \"grade\" INTEGER NOT NULL, \"position\" INTEGER NOT NULL, \"percent\" TEXT NOT NULL);");
            for (int i = 0;i < 7;i++) {
                db.execSQL("INSERT INTO '" + name + "' VALUES(NULL,0,0,0,0,1,0.0);");
            }
            return "succes";
        } else {
            return "exist";
        }
    }

    public int seekTable(String name) {
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + name + "';", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public ArrayList<Object[]> getDatas(String name) {
        final SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM '" + name + "'", null);
        cursor.moveToFirst();

        ArrayList<Object[]> result = new ArrayList<>();
        for (int i = 0;i < cursor.getCount();i++) {
            int isIn = cursor.getInt(1);
            int subject = cursor.getInt(2);
            int origin = cursor.getInt(3);
            int grade = cursor.getInt(4);
            int position = cursor.getInt(5);
            String percent = cursor.getString(6);

            Object[] re = new Object[] {isIn, subject, origin, grade, position, percent};
            result.add(re);
            cursor.moveToNext();
        }

        return result;
    }

    public void updateIsIn(String name, boolean isIn, int id) {
        final SQLiteDatabase db = getWritableDatabase();
        int kk = 0;
        if (isIn) {
            kk = 1;
        } else {
            kk = 0;
        }
        db.execSQL("UPDATE \'" + name + "\' SET 'isIn' = " + kk + " WHERE id = " + (id + 1) +";");
    }

    public void updateSubject(String name, int position, int id) {
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE \'" + name + "\' SET 'subject' = " + position + " WHERE id = " + (id + 1) +";");
    }

    public void updateOrginalGrade(String name, int origin, int id) {
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE \'" + name + "\' SET 'origin' = " + origin + " WHERE id = " + (id + 1) +";");
    }

    public void updatePercen(String name, float percent, int id) {
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE \'" + name + "\' SET 'percent' = " + Float.toString(percent) + " WHERE id = " + (id + 1) +";");
    }

    public void updateGrade(String name, int grade, int id) {
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE \'" + name + "\' SET 'grade' = " + grade + " WHERE id = " + (id + 1) +";");
    }

    public void updatePosition(String name, int grade, int id) {
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE \'" + name + "\' SET 'position' = " + grade + " WHERE id = " + (id + 1) +";");
    }

    public void deleteTable(String name) {
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE '" + name + "'");
    }

    public String[][] getListDatas(int type, ArrayList<Integer> b) {
        final SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' AND name!='sqlite_sequence'  order by name", null);
        cursor.moveToFirst();

        String t = "";
        switch (type) {
            case 0:
                t = "origin";
                break;
            case 1:
                t = "grade";
                break;
            case 2:
                t = "position";
                break;
            case 3:
                t = "percent";
                break;
        }

        ArrayList<String> n = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        for (int i = 0;i < cursor.getCount();i++) {
            String name = cursor.getString(0);
            n.add(name);
            try {
                JSONObject jsonObject1 = new JSONObject();
                for (int id:b) {
                    Cursor cursor1 = db.rawQuery("SELECT isIn," + t + " FROM '" + name + "' WHERE id=" + id, null);
                    cursor1.moveToFirst();
                    int isIn = cursor1.getInt(0);
                    if (isIn == 1) {
                        if (type == 3) {
                            String sd = cursor1.getString(1);
                            jsonObject1.put(String.valueOf(id), Float.parseFloat(sd));
                            Float.parseFloat(sd);
                        } else {
                            jsonObject1.put(String.valueOf(id), cursor1.getInt(1));
                        }
                    } else {
                        jsonObject1.put(String.valueOf(id), -1);
                    }
                }
                jsonObject.put(name, jsonObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }

        String[] ns = new String[n.size()];
        for (int i = 0;i < ns.length;i++) {
            ns[i] = n.get(i);
        }
        return new String[][] {{jsonObject.toString()}, ns};
    }
}

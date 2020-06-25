package com.k1a2.schoolcalculator.database;

/**데이터 베이스 키 모아둔 클래스**/

public interface DatabaseKey {
    public String KEY_DB_NAME = "Score.db";
    public String KEY_TABLE_11 = "11";
    public String KEY_TABLE_12 = "12";
    public String KEY_TABLE_21 = "21";
    public String KEY_TABLE_22 = "22";
    public String KEY_TABLE_31 = "31";
    public String KEY_TABLE_32 = "32";

    public String KEY_VALUE_SUBJECT = "subject";
    public String KEY_VALUE_GRADE = "grade";
    public String KEY_VALUE_POINT = "point";
    public String KEY_VALUE_TYPE = "type";
    public String KEY_VALUE_POSITION = "position";

    public Integer KEY_DB_TYPE_K = 0;
    public Integer KEY_DB_TYPE_E = 2;
    public Integer KEY_DB_TYPE_M= 1;
    public Integer KEY_DB_TYPE_R = 5;
    public Integer KEY_DB_TYPE_SC = 4;
    public Integer KEY_DB_TYPE_S = 3;


    public String KEY_DB_NAME_TEST = "test.db";
}

package com.k1a2.schoolcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
import com.k1a2.schoolcalculator.sharedpreference.PreferenceKey;

import java.util.ArrayList;

/**등급 계산 클래스
 * 왠만하면 수정금지**/

public class CalculateGrade {

    private float[] f11 = null;
    private float[] f12 = null;
    private float[] f21 = null;
    private float[] f22 = null;
    private float[] f31 = null;
    private float[] f32 = null;

    private float result11 = 0;
    private float result12 = 0;
    private float result21 = 0;
    private float result22 = 0;
    private float result31 = 0;
    private float result32 = 0;
    private float result1 = 0;
    private float result2 = 0;
    private float result3 = 0;
    private float resultAll = 0;

    private ScoreDatabaseHelper scoreDatabaseHelper = null;
    private SharedPreferences preferences_rate = null;

    public CalculateGrade(Context context) {
        scoreDatabaseHelper = new ScoreDatabaseHelper(context, DatabaseKey.KEY_DB_NAME, null, 1);
        preferences_rate = PreferenceManager.getDefaultSharedPreferences(context);

        f11 = getGrade(11);
        f12 = getGrade(12);
        f21 = getGrade(21);
        f22 = getGrade(22);
        f31 = getGrade(31);
        f32 = getGrade(32);

        int r1 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 0);
        int r2 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 0);
        int r3 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 0);

        if (r1 == 0||r2 == 0||r3 == 0) {
            r1 = 1;
            r2 = 1;
            r3 = 1;
        }

        //텍스트뷰에 함수 값 연결
        if (f11 == null) {
            result11 = 0;
        } else {
            result11 = (float) (Math.round(f11[0]/f11[1]*100)/100.0);
        }
        if (f12 == null) {
            result12 = 0;
        } else {
            result12 = (float) (Math.round(f12[0]/f12[1]*100)/100.0);
        }
        if (f21 == null) {
            result21 = 0;
        } else {
            result21 = (float) (Math.round(f21[0]/f21[1]*100)/100.0);
        }
        if (f22 == null) {
            result22 = 0;
        } else {
            result22 = (float) (Math.round(f22[0]/f22[1]*100)/100.0);
        }
        if (f31 == null) {
            result31 = 0;
        } else {
            result31 = (float) (Math.round(f31[0]/f31[1]*100)/100.0);
        }
        if (f32 == null) {
            result32 = 0;
        } else {
            result32 = (float) (Math.round(f32[0]/f32[1]*100)/100.0);
        }
        if (f11 !=  null&&f12 != null) {
            result1 = (float) (Math.round((f11[0] + f12[0])/(f11[1] + f12[1])*100)/100.0);
        } else if (f11 ==  null&&f12 != null) {
            result1 = (float) (Math.round(f12[0]/f12[1]*100)/100.0);
        } else if (f11 !=  null&&f12 == null) {
            result1 = (float) (Math.round(f11[0]/f11[1]*100)/100.0);
        } else {
            result1 = 0;
        }
        if (f21 !=  null&&f22 != null) {
            result2 = (float) (Math.round((f21[0] + f22[0])/(f21[1] + f22[1])*100)/100.0);
        } else if (f21 ==  null&&f22 != null) {
            result2 = (float) (Math.round(f22[0]/f22[1]*100)/100.0);
        } else if (f21 !=  null&&f22 == null) {
            result2 = (float) (Math.round(f21[0]/f21[1]*100)/100.0);
        } else {
            result2 = 0;
        }
        if (f31 !=  null&&f32 != null) {
            result3 = (float) (Math.round((f31[0] + f32[0])/(f31[1] + f32[1])*100)/100.0);
        } else if (f31 ==  null&&f32 != null) {
            result3 = (float) (Math.round(f32[0]/f32[1]*100)/100.0);
        } else if (f31 !=  null&&f32 == null) {
            result3 = (float) (Math.round(f31[0]/f31[1]*100)/100.0);
        } else {
            result3 = 0;
        }
        if (f11 == null&&f12 == null&&f21 == null&&f22 == null&&f31 == null&&f32 == null) {
            resultAll = 0;
        } else {
            float ap1 = 0;
            float ag1 = 0;
            float ap2 = 0;
            float ag2 = 0;
            float ap3 = 0;
            float ag3 = 0;

            if (f11 != null) {
                ag1 += f11[0];
                ap1 += f11[1];
            }
            if (f12 != null) {
                ag1 += f12[0];
                ap1 += f12[1];
            }
            if (f21 != null) {
                ag2 += f21[0];
                ap2 += f21[1];
            }
            if (f22 != null) {
                ag2 += f22[0];
                ap2 += f22[1];
            }
            if (f31 != null) {
                ag3 += f31[0];
                ap3 += f31[1];
            }
            if (f32 != null) {
                ag3 += f32[0];
                ap3 += f32[1];
            }

            if (r1 == 1&&r2 == 1&&r3 == 1) {
                float rr = 0;
                float a1 = ag1/ap1;
                float a2 = ag2/ap2;
                float a3 = ag3/ap3;
                if (a1 != 0) {
                    if (!Float.isNaN(a1)) {
                        rr += a1;
                    }
                }
                if (a2 != 0) {
                    if (!Float.isNaN(a2)) {
                        rr += a2;
                    }
                }
                if (a3 != 0) {
                    if (!Float.isNaN(a3)) {
                        rr += a3;
                    }
                }
                resultAll = (float) (Math.round(rr/3*100)/100.0);
            } else {
                final float rA = r1 + r2 + r3;
                float rr = 0;
                if (ag1 != 0&&ap1 != 0) {
                    rr += ag1/ap1*r1/rA;
                }
                if (ag2 != 0&&ap2 != 0) {
                    rr += ag2/ap2*r2/rA;
                }
                if (ag3 != 0&&ap3 != 0) {
                    rr += ag3/ap3*r3/rA;
                }
                resultAll = (float) (Math.round(rr*100)/100.0);
            }
        }
    }

    public float getResult11() {
        return result11;
    }

    public float getResult12() {
        return result12;
    }

    public float getResult21() {
        return  result21;
    }

    public float getResult22() {
        return result22;
    }

    public float getResult31() {
        return result31;
    }

    public float getResult32() {
        return result32;
    }

    public float getResult1() {
        return result1;
    }

    public float getResult2() {
        return result2;
    }

    public float getResult3() {
        return result3;
    }

    public float getResultAll() {
        return resultAll;
    }

    //학기별 등급 산출 함수
    private float[] getGrade(int type) {
        float sumgrade = 0; // 등급, 단위수 곱한 후 합산
        float sumpoint = 0;

        switch (type) {
            case 11: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_11);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 12: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_12);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 21: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_21);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 22: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_22);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 31: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_31);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 32: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_32);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            default: {
                return null;
            }
        }
    }
}

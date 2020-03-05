package com.k1a2.schoolcalculator.activity;

/**주요 액티비티 키를 나열한 클래스
 * 수정 금지**/

public interface ActivityKey {
    public String KEY_ACTIVITY_MODE = "AM";
    String KEY_ACTIVITY_ACCOUNT_MODE = "ACM";

    public String KEY_ACTIVITY_SCORE_ALL = "AA";
    public String KEY_ACTIVITY_SCORE_FIRST = "FA";
    public String KEY_ACTIVITY_SCORE_SECOND = "SA";
    public String KEY_ACTIVITY_SCORE_THIRD = "TA";

    int KEY_ACTIVITY_ACCOUNT_DELETE = 2000;
    int KEY_ACTIVITY_ACCOUNT_PASSWORD = 4000;

    int LOGIN_RSULT_NEED_VARIFY = 1000;
    int LOGIN_RESULT_SUCCESS = 200;
    int LOGIN_RESULT_FAIL = 300;
    int LOGIN_RESULT_CANCELED = 400;
}

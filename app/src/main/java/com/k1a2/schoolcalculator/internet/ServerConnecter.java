package com.k1a2.schoolcalculator.internet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;

import com.k1a2.schoolcalculator.activity.MainActivity;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class ServerConnecter {

    public class CheckUser extends AsyncTask<Void, String, String> {

        private ProgressDialog progressDialog;
        private Context context;
        private String receiveMsg;
        private String uid;
        private int code;

        public CheckUser(Context context, String uid, int code) {
            this.context = context;
            this.uid = uid;
            this.code = code;
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("저장 정보 확인중..");
            progressDialog.setMessage("이전에 저장한 데이터가 있는지 조회중입니다.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String str;
                //URL url = new URL("http://10.0.2.2:8080/ScoreCalcolator/TestResponse.jsp");
                URL url = new URL("http://ec2-13-125-247-41.ap-northeast-2.compute.amazonaws.com:8080/ScoreCalcolator/CheckDB.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(uid);
                wr.flush();
//                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//                sendMsg = "id=njk"+"&pw=q";
//                osw.write(sendMsg);
//                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    return receiveMsg;
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                    return "err";
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return "timerr";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "err";
            } catch (IOException e) {
                e.printStackTrace();
                return "err";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            ((MainActivity)context).isUserExsist(s, code);
        }
    }

    public class SaveDB extends AsyncTask<Void, String, String> {

        private ProgressDialog progressDialog;
        private String uid;
        private Context context;
        private String receiveMsg;
        private ScoreDatabaseHelper scoreDatabaseHelper = null;
        private String ex;

        public SaveDB(Context context, String uid, String exist) {
            this.context = context;
            this.uid = uid;
            ex = exist;
            scoreDatabaseHelper = new ScoreDatabaseHelper(context, DatabaseKey.KEY_DB_NAME, null, 1);
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("서버에 저장중..");
            progressDialog.setMessage("서버에 성적 정보를 저장중입니다.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                JSONObject json = new JSONObject();
                json.put("uid", uid);
                json.put("isEx", ex);
                for (int i = 0; i < 3; i++) {
                    for (int a = 0; a < 2; a++) {
                        final ArrayList<String[]> values = scoreDatabaseHelper.getScores(String.valueOf(i + 1) + String.valueOf(a + 1));
                        //JSONArray jsonArray = new JSONArray();
                        JSONArray jsonArray1 = new JSONArray();
                        for (String[] s : values) {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("subject", s[0]);
                            jsonObject1.put("rank", s[1]);
                            jsonObject1.put("point", s[2]);
                            jsonObject1.put("type", s[3]);
                            jsonArray1.put(jsonObject1);
                        }
                        json.put(String.valueOf(i + 1) + String.valueOf(a + 1), jsonArray1);
                    }
                }

                String str;
                //URL url = new URL("http://10.0.2.2:8080/ScoreCalcolator/TestResponse.jsp");
                URL url = new URL("http://ec2-13-125-247-41.ap-northeast-2.compute.amazonaws.com:8080/ScoreCalcolator/SaveDB.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(json.toString());
                wr.flush();
//                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//                sendMsg = "id=njk"+"&pw=q";
//                osw.write(sendMsg);
//                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    return receiveMsg;
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                    return "err";
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return "timerr";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "err";
            } catch (IOException e) {
                e.printStackTrace();
                return "err";
            } catch (JSONException e) {
                e.printStackTrace();
                return "err";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            ((MainActivity)context).isSaveSuccess(s);
            //JSONObject j = new JSONObject(s);
        }
    }

    public class LoadDB extends AsyncTask<Void, String, String> {

        private ProgressDialog progressDialog;
        private String uid;
        private Context context;
        private String receiveMsg;
        private ScoreDatabaseHelper scoreDatabaseHelper = null;

        public LoadDB(Context context, String uid) {
            this.context = context;
            this.uid = uid;
            scoreDatabaseHelper = new ScoreDatabaseHelper(context, DatabaseKey.KEY_DB_NAME, null, 1);
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("서버에서 가져오는중..");
            progressDialog.setMessage("서버에 성적 정보를 가져오는중입니다.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                JSONObject json = new JSONObject();
                json.put("uid", uid);

                String str;
                //URL url = new URL("http://10.0.2.2:8080/ScoreCalcolator/TestResponse.jsp");
                URL url = new URL("http://ec2-13-125-247-41.ap-northeast-2.compute.amazonaws.com:8080/ScoreCalcolator/LoadDB.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(json.toString());
                wr.flush();
//                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//                sendMsg = "id=njk"+"&pw=q";
//                osw.write(sendMsg);
//                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                    JSONObject jsonObject = new JSONObject(receiveMsg);
                    String result = jsonObject.getString("res");
                    switch (result) {
                        case "suc": {
                            for (int i = 0;i < 3;i++) {
                                for (int a = 0;a < 2;a++) {
                                    final String name = String.valueOf(i + 1)+String.valueOf(a + 1);
                                    scoreDatabaseHelper.deleteAll(name);
                                    JSONArray array = jsonObject.getJSONArray(name);
                                    for ( int q = 0; q < array.length(); q++ ) {
                                        JSONObject datas = (JSONObject)array.get(q);
                                        String subject = datas.getString ("subject");
                                        String grade = datas.getString ("rank");
                                        String point = datas.getString ("point");
                                        String type = datas.getString ("type");
                                        scoreDatabaseHelper.insert(name, subject, Integer.parseInt(grade), Integer.parseInt(point), Integer.parseInt(type), q);
                                    }
                                }
                            }

                            return "suc";
                        }
                        case "dberror":
                        case "sererror": {
                            return result;
                        }
                        default: {
                            return "resoponerror";
                        }
                    }
                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                    return "err";
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return "timerr";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "err";
            } catch (IOException e) {
                e.printStackTrace();
                return "err";
            } catch (JSONException e) {
                e.printStackTrace();
                return "err";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            ((MainActivity)context).isLoadSuccess(s);
            //JSONObject j = new JSONObject(s);
        }
    }
}

package com.k1a2.schoolcalculator.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.utils.Utils;
import com.k1a2.schoolcalculator.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**스플래시 액티비티
 * 이건 설명 생략. 수정 금지**/

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //안드 5.0 이상일때 권한검사
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //저장소 읽기/쓰기 권한 요청
            final int permisionRequest = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            final int permisionRequest2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permisionRequest == PackageManager.PERMISSION_DENIED || permisionRequest2 == PackageManager.PERMISSION_DENIED)
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    final AlertDialog.Builder permissioCheck = new AlertDialog.Builder(SplashActivity.this);
                    permissioCheck.setTitle("저장소 권한이 필요합니다")
                            .setMessage("성적을 저장하기 위한 저장소 읽기 쓰기 권한이 필요합니다")
                            .setCancelable(false)
                            .setPositiveButton("동의", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ActivityCompat.requestPermissions(SplashActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                                    }
                                }
                            })
                        .setNegativeButton("거부", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(SplashActivity.this, "성적을 저장할수 없어서 앱을 이용하실 수 없습니다", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                        .create()
                            .show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
            else {
                startMain();
            }
        } else {
            startMain();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMain();
                } else {
                    Toast.makeText(this, "성적을 저장할수 없어서 앱을 이용하실 수 없습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    private void startMain() {
        final Handler handeler = new  Handler();
        handeler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1500);
    }
}

package com.k1a2.schoolcalculator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.k1a2.schoolcalculator.R;

public class AccountActivity extends AppCompatActivity {

//    private SignInButton signInButton = null;
    private EditText edit_email = null;
    private EditText edit_pass = null;
    private Button btn_login = null;
    private Button btn_signup =  null;
    private LinearLayout layout_login = null;
    private LinearLayout layout_signup = null;
    private RelativeLayout layout_varify = null;
    private Button btn_back = null;
    private EditText edit_up_email = null;
    private EditText edit_up_pass = null;
    private EditText edit_up_passcheck = null;
    private Button btn_sginup_new = null;
    private TextView text_title = null;

    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900;

    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    private Intent intent = null;
    private int mode = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        signInButton = findViewById(R.id.login_google);
        edit_email =  findViewById(R.id.login_email);
        edit_pass = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.login_btn_login);
        btn_signup = findViewById(R.id.login_btn_signup);
        layout_login = findViewById(R.id.login_layout_signin);
        layout_signup = findViewById(R.id.login_layout_signup);
        layout_varify = findViewById(R.id.login_lay_verify);
        btn_back = findViewById(R.id.signup_btn_back);
        edit_up_email = findViewById(R.id.signup_email);
        edit_up_pass = findViewById(R.id.signup_password);
        edit_up_passcheck = findViewById(R.id.signup_password_check);
        btn_sginup_new = findViewById(R.id.signup_btn_signup);

        layout_login.setVisibility(View.VISIBLE);
        layout_signup.setVisibility(View.GONE);
        layout_varify.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        intent = getIntent();
        mode = intent.getIntExtra(ActivityKey.KEY_ACTIVITY_ACCOUNT_MODE, 0);
        if (mode != 0) {
            switch (mode) {
                case ActivityKey.KEY_ACTIVITY_ACCOUNT_PASSWORD: {
                    text_title = findViewById(R.id.login_text_title);

                    text_title.setText("비밀번호 변경");
                    edit_up_email.setVisibility(View.GONE);
                    edit_up_pass.setHint("변경할 비밀번호");
                    edit_up_passcheck.setHint("변경할 비밀번호 확인");
                    btn_back.setVisibility(View.GONE);
                    btn_sginup_new.setText("변경하기");
                    layout_login.setVisibility(View.GONE);
                    layout_signup.setVisibility(View.VISIBLE);
                    layout_varify.setVisibility(View.GONE);
                    break;
                }
                case ActivityKey.KEY_ACTIVITY_ACCOUNT_DELETE: {
                    break;
                }
            }
        } else {
            // Google 로그인을 앱에 통합
            // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
//            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
//                    .requestEmail()
//                    .build();

//            googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

//            signInButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent signInIntent = googleSignInClient.getSignInIntent();
//                    startActivityForResult(signInIntent, RC_SIGN_IN);
//                }
//            });

            //로그인 버튼
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = edit_email.getText().toString();
                    String password = edit_pass.getText().toString();
                    if (email.isEmpty()||password.isEmpty()) {
                        Toast.makeText(AccountActivity.this, "모든 칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(AccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    setResult(ActivityKey.LOGIN_RESULT_SUCCESS);
                                    finish();
                                } else {
                                    setResult(ActivityKey.LOGIN_RESULT_FAIL);
                                    finish();
                                }
                            }
                        });
                    }
                }
            });

            //회원가입 버튼
            btn_sginup_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = edit_up_email.getText().toString();
                    String pass1 = edit_up_pass.getText().toString();
                    String pass2 = edit_up_passcheck.getText().toString();
                    if (email.isEmpty()) {
                        Toast.makeText(AccountActivity.this, "모든 칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pass1.isEmpty()||pass2.isEmpty()) {
                            Toast.makeText(AccountActivity.this, "모든 칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (pass1.equals(pass2)) {
                                firebaseAuth.createUserWithEmailAndPassword(email, pass1)
                                        .addOnCompleteListener(AccountActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    firebaseAuth.getCurrentUser().sendEmailVerification();
                                                    layout_varify.setVisibility(View.VISIBLE);
                                                    layout_signup.setVisibility(View.INVISIBLE);
                                                    firebaseAuth.signOut();
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    setResult(ActivityKey.LOGIN_RESULT_FAIL);
                                                    finish();
                                                }
                                            }
                                        });

                            } else {//TODO: 이메일 인증
                                Toast.makeText(AccountActivity.this, "비밀번호가 같게 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

            btn_signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout_login.setVisibility(View.GONE);
                    layout_signup.setVisibility(View.VISIBLE);
                    layout_varify.setVisibility(View.GONE);
                }
            });

            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout_login.setVisibility(View.VISIBLE);
                    layout_signup.setVisibility(View.GONE);
                    layout_varify.setVisibility(View.GONE);
                }
            });
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 구글로그인 버튼 응답
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(AccountActivity.this, e.getMessage() + "\n\n" + e.getStatusCode(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증합니다.
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        //intent.putExtra("result", "some value");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            setResult(ActivityKey.LOGIN_RESULT_SUCCESS);
                            finish();
                        } else {
                            setResult(ActivityKey.LOGIN_RESULT_FAIL);
                            finish();
                            // 로그인 실패
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (mode == 0) {
            if (layout_varify.getVisibility() == View.VISIBLE) {
                setResult(ActivityKey.LOGIN_RESULT_SUCCESS);
                finish();
            } else {
                if (layout_signup.getVisibility() == View.VISIBLE) {
                    layout_login.setVisibility(View.VISIBLE);
                    layout_signup.setVisibility(View.GONE);
                    layout_varify.setVisibility(View.GONE);
                } else if (layout_login.getVisibility() == View.VISIBLE) {
                    setResult(ActivityKey.LOGIN_RESULT_CANCELED);
                    finish();
                }
            }
        } else {
            finish();
        }
    }
}

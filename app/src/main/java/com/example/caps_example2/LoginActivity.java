package com.example.caps_example2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSigninClient;
    private int RC_SIGN_IN = 10;

    private EditText et_email, et_password, et_password_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
        // Configure Google Sign In, api값과 요청할 값이 저장되어 있음
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSigninClient = GoogleSignIn.getClient(this, gso);


        findViewById(R.id.email_login_button).setOnClickListener(onClickListener);
        findViewById(R.id.google_login_button).setOnClickListener(onClickListener);


        et_email = findViewById(R.id.email_edittext);
        et_password = findViewById(R.id.password_edittext);
        et_password_check = findViewById(R.id.password_check_edittext);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.email_login_button:
                    createAccount();
                    break;

                case R.id.google_login_button:
                    signIn();
                    break;

                // 구글 로그인 성공 -> 구글에서는 토큰을 넘겨준다.
                // 토큰에는 유저 아이디와 이름이 암호화 (JsonWebToken) 되어있음
                // 그 토큰을 firebase에 서버로 넘겨줘 firebase는 유저의 계정을 만들게된다.
            }
        }
    };

    // start signin
    private void signIn() {
        Intent signInIntent = mGoogleSigninClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                // updateUI(null);
                // [END_EXCLUDE]
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential: success");
                            Snackbar.make(findViewById(R.id.layout_login), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential: failure", task.getException());
                            Toast.makeText(LoginActivity.this,"Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String passwordCheck = et_password_check.getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {

                // createUserWithEmailAndPassword : 비밀번호 기반의 계정을 생성
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "회원가입에 성공하셨습니다.");
                                    Toast.makeText(LoginActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) {
                                        //Log.w(TAG, "비밀번호가 일치하지 않습니다.", task.getException());
                                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            }
                        });
            } else {
                Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
    //private void updateUI(FirebaseUser currentUser) {}

}


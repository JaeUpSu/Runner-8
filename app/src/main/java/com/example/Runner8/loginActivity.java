package com.example.Runner8;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.SingleTon.LoginSingleTon;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class loginActivity  extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 123;

    //
    boolean user_data_check = false;
    boolean auto_check = false;

    private EditText mEmailText, mPasswordText;
    private Button logBtn;
    private TextView regBtn;
    private SignInButton googleBtn;
    private CheckBox auto_login;

    private LinearLayout laybtn_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        // Button
        logBtn = (Button) findViewById(R.id.btn_login);
        regBtn = findViewById(R.id.txt_btn_join);
        laybtn_google = findViewById(R.id.laybtn_google);
        auto_login = findViewById(R.id.auto_login);

        // EditText
        mEmailText = (EditText) findViewById(R.id.id_inputedit);
        mPasswordText = (EditText) findViewById(R.id.password_inputedit);

        // FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //
        updateUI(user);

        // Google Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // 로그인
        logBtn.setOnClickListener(view -> {

            String email = mEmailText.getText().toString().trim();
            String pwd = mPasswordText.getText().toString().trim();

            try {
                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {

                                Map<String, Object> map = new HashMap<>();
                                map.put("auto_check", auto_check);
                                db.collection("Users").document(mAuth.getCurrentUser().getUid()).update(map);

                                LoginSingleTon.getInstance().setLogin_finish_check(true);

                                Intent intent = new Intent(loginActivity.this, SplashActivity.class);
                                startActivity(intent);
                                // finish();

                            } else {
                                Toast.makeText(loginActivity.this, "로그인 실패", Toast.LENGTH_LONG).show();
                            }
                        });
            }catch (Exception e){
                Toast.makeText(loginActivity.this,"이메일을 입력해주세요!!", Toast.LENGTH_LONG).show();
            }
        });
        // 회원가입
        regBtn.setOnClickListener(v -> {
            Intent intent = new Intent(loginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // 구글 로그인
        laybtn_google.setOnClickListener(v -> {
            signIn();
        });

        auto_login.setOnCheckedChangeListener((buttonView, isChecked) -> {
            auto_check = isChecked;

        });


        /*
        if(user != null) {
            db.collection("Users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        auto_check = Boolean.valueOf(document.get("auto_check").toString());
                        Log.i("auto addOnCompleteListener", auto_check + "");
                        auto_login.setChecked(auto_check);
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);

                        Log.i("auto", auto_check + "");
                        if(!LoginSingleTon.getInstance().isRegister_check()){
                            if(auto_check) {
                                startActivity(intent);
                                Toast.makeText(this, user.getEmail() + " 님 자동로그인 되셨습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

         */

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    // 자동 로그인
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        user = mAuth.getCurrentUser();
        Intent intent = new Intent(loginActivity.this, MainActivity.class);

        /*
        Log.i("auto", auto_check + "");
        if(!LoginSingleTon.getInstance().isRegister_check()){
            if(auto_check) {
                startActivity(intent);
                Toast.makeText(this, user.getEmail() + " 님 자동로그인 되셨습니다.", Toast.LENGTH_SHORT).show();
            }


        }

         */

        if(user != null){
            Log.i("user", user + "");


            /*
            db.collection("Users").document(user.getUid())
                    .collection("Profile").document("diet_profile")
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        boolean check = Boolean.valueOf(document.get("data_check").toString());
                        if(check){
                            Intent intent = new Intent(loginActivity.this, MainActivity.class);
                            intent.putExtra("NoneDataUser", false);
                            // startActivity(intent);
                            Toast.makeText(this, user.getEmail() + " 님 자동로그인 되셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.i("Login","Need Login");
                        }
                    });

             */
        }
        else{
            Log.i("Login","Need Login");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        intent.putExtra("providerId", R.string.default_web_client_id);
                        startActivity(intent);
                        updateUI(user);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Log.i("User", "Exist");
        }else{
            Log.i("User", "Not Exist");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }
}

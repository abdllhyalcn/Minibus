package com.example.minibus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText inputEmail, inputPassword;
    private MaterialButton btnLogin, btnForgot;
    private SwitchMaterial rememberMe;
    private ProgressBar progressBar;

    public static String  PREFS_NAME="authentication";
    public static String PREF_USERNAME="username";
    public static String PREF_PASSWORD="password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

       if (auth.getCurrentUser() == null) {

           // auth.signOut();
            startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
            finish();
        }

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        inputEmail =  findViewById(R.id.username_edit_text);
        inputPassword =  findViewById(R.id.password_edit_text);
        btnLogin =  findViewById(R.id.login_button);
        btnForgot =  findViewById(R.id.forgot_password_button);
        rememberMe= findViewById(R.id.remember_me_switch);
        progressBar =  findViewById(R.id.progressBar);

    }

    @Override
    protected void onStart() {
        super.onStart();

        getUser();



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString()+"@minibusdomain.com";
                final String password = inputPassword.getText().toString();

                if (email.length() == 18) {
                    inputEmail.setError(getString(R.string.minimum_email));
                    return;
                } else if (password.length() < 6) {
                    inputPassword.setError(getString(R.string.minimum_password));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                signIn(email,password);
            }
        });


        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:05395553062");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
    }

    private void signIn(final String email,final String password){
        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        } else {
                            if(rememberMe.isChecked()){
                                rememberMe(email,password);
                            }else{
                                deleteUser();
                            }
                            Toast.makeText(getApplicationContext(), auth.getUid(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void getUser(){
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);

        if (username != null || password != null) {
            //directly show logout form
            inputEmail.setText(username);
            inputPassword.setText(password);
            rememberMe.setChecked(true);
        }
    }

    private void rememberMe(String user, String password){
        user= user.substring(0 , user.indexOf('@'));
        //save username and password in SharedPreferences
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString(PREF_USERNAME,user)
                .putString(PREF_PASSWORD,password)
                .apply();
    }

    private void deleteUser(){
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        pref.edit().clear().apply();
    }
}

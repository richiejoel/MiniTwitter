package com.heavy.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.heavy.minitwitter.R;
import com.heavy.minitwitter.common.Constants;
import com.heavy.minitwitter.common.SharedPreferencesManager;
import com.heavy.minitwitter.retrofit.MiniTwitterClient;
import com.heavy.minitwitter.retrofit.MiniTwitterService;
import com.heavy.minitwitter.retrofit.request.RequestLogin;
import com.heavy.minitwitter.retrofit.response.ResponseLogin;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView txtCreateAccount;
    EditText edtLoginEmail, edtLoginPassword;

    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        findViews();
        events();

        retrofitInit();

    }

    private void findViews(){
        btnLogin = findViewById(R.id.btnLogin);
        txtCreateAccount = findViewById(R.id.txtCreateAccount);
        edtLoginEmail = findViewById(R.id.loginEmail);
        edtLoginPassword = findViewById(R.id.loginPassword);
    }

    private void events(){
        btnLogin.setOnClickListener(this);
        txtCreateAccount.setOnClickListener(this);
    }

    private void retrofitInit(){
        this.miniTwitterClient = MiniTwitterClient.getInstance();
        this.miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                mGoToLogin();
                break;
            case R.id.txtCreateAccount:
                mGoToSignUp();
                break;
        }
    }

    private void mGoToLogin(){
        String email = edtLoginEmail.getText().toString();
        String password = edtLoginPassword.getText().toString();

        if(email.isEmpty()){
            edtLoginEmail.setError("Email is required");
        }
        if(password.isEmpty()){
            edtLoginPassword.setError("Password is required");
        }

        if(!email.isEmpty() && !password.isEmpty()){
            RequestLogin requestLogin = new RequestLogin(email, password);

            Call<ResponseLogin> call = miniTwitterService.mDoLogin(requestLogin);
            call.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();

                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_DATE_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constants.PREF_ACTIVE, response.body().getActive());

                        Intent goDashboard = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(goDashboard);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email or Password incorrect",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void mGoToSignUp(){
        Intent goSignup = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(goSignup);
    }
}

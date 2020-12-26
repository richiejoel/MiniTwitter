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
import com.heavy.minitwitter.retrofit.MiniTwitterClient;
import com.heavy.minitwitter.retrofit.MiniTwitterService;
import com.heavy.minitwitter.retrofit.request.RequestSignUp;
import com.heavy.minitwitter.retrofit.response.ResponseSignUp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp;
    TextView txtGoLogin;
    EditText edtSignupUsername, edtSignupEmail, edtSignupPassword;

    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        UI();
        events();
        retrofitInit();

    }

    private void UI(){
        btnSignUp = findViewById(R.id.btnSignUp);
        txtGoLogin = findViewById(R.id.txtLoginGo);
        edtSignupUsername = findViewById(R.id.signupUsername);
        edtSignupEmail = findViewById(R.id.signupEmail);
        edtSignupPassword = findViewById(R.id.signupPassword);
    }

    private void events(){
        btnSignUp.setOnClickListener(this);
        txtGoLogin.setOnClickListener(this);
    }

    private void retrofitInit(){
        this.miniTwitterClient = MiniTwitterClient.getInstance();
        this.miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                mGoToSignUp();
                break;
            case R.id.txtLoginGo:
                mGoToLogin();
                break;
        }

    }

    private void mGoToSignUp(){
        String username = edtSignupUsername.getText().toString();
        String email = edtSignupEmail.getText().toString();
        String password = edtSignupPassword.getText().toString();

        if(username.isEmpty()){
            edtSignupUsername.setError("Username is required");
        }
        if(email.isEmpty()){
            edtSignupEmail.setError("Email is required");
        }
        if(password.length() < 4){
            edtSignupPassword.setError("The password should have mimimun 4 characters");
        }
        if(password.isEmpty()){
            edtSignupPassword.setError("Password is required");
        }


        if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length() > 3 ){
            RequestSignUp requestSignUp = new RequestSignUp(username, email, password, "UDEMYANDROID");

            Call<ResponseSignUp> call = miniTwitterService.mDoSignUp(requestSignUp);
            call.enqueue(new Callback<ResponseSignUp>() {
                @Override
                public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Create Account Successfull", Toast.LENGTH_SHORT).show();
                        Intent goLogin = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(goLogin);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email or Password incorrect",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Trouble connection. Try again please.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void mGoToLogin(){
        Intent goLogin = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(goLogin);
        finish();
    }


}

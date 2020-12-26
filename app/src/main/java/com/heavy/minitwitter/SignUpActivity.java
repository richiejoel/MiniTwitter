package com.heavy.minitwitter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp;
    TextView txtGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        btnSignUp = findViewById(R.id.btnSignUp);
        txtGoLogin = findViewById(R.id.txtLoginGo);

        btnSignUp.setOnClickListener(this);
        txtGoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                break;
            case R.id.txtLoginGo:
                mGoToLogin();
                break;
        }

    }

    private void mGoToLogin(){
        Intent goLogin = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(goLogin);
    }


}

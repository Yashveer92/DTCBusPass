package com.example.dtcbuspass.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dtcbuspass.HomePage;
import com.example.dtcbuspass.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button login;
    TextView sighUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.login_to_app);
        sighUp=findViewById(R.id.btv_signup);


        sighUp.setOnClickListener(this);

        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.login_to_app:

                Intent login=new Intent(LoginActivity.this, HomePage.class);
                startActivity(login);
                finish();
                break;
            case R.id.btv_signup:
                Intent signup=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(signup);
                finish();
                break;

        }

    }
}

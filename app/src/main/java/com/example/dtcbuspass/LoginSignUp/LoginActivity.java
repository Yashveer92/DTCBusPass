package com.example.dtcbuspass.LoginSignUp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dtcbuspass.HomePage;
import com.example.dtcbuspass.ProfileActivity;
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

        final TextView tv_changePassword    = findViewById(R.id.forgot_password);


        tv_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

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




    public void changePassword() {

        Context context = LoginActivity.this;
        final AlertDialog alertDialog;

        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.dialog_change_password,null);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(context,android.
                    R.style.Theme_DeviceDefault_Light_Dialog_MinWidth).create();
        } else {
            alertDialog = new AlertDialog.Builder(context).create();
        }

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.setView(v);

        final EditText et_current_pass,et_new_pass, et_confirm_pass;
        final TextView password_error;
        TextView done,cancel;


        et_current_pass = v.findViewById(R.id.current_password);
        et_new_pass     = v.findViewById(R.id.new_password);
        et_confirm_pass = v.findViewById(R.id.confirm_password);


        password_error = v.findViewById(R.id.tv_password_error);

        done   = v.findViewById(R.id.done);
        cancel = v.findViewById(R.id.cancel);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentPass,newPass,confirmPass;

                currentPass = et_current_pass.getText().toString().trim();
                newPass     = et_new_pass.getText().toString().trim();
                confirmPass = et_confirm_pass.getText().toString().trim();


                if(currentPass.equals("") || newPass.equals("") ||
                        confirmPass.equals("")){
                    password_error.setVisibility(View.VISIBLE);
                    password_error.setText("All field are required");
                    return;
                }

                //if old password is wrong
              /*  if(!currentPass.equals(mLoginSession.getUserDetailsFromSharedPreference()
                        .get(KEY_PASSWORD))){
                    password_error.setVisibility(View.VISIBLE);
                    password_error.setText("*Current password is wrong");
                    et_new_pass.setText("");
                    et_confirm_pass.setText("");

                    return;
                }*/

                //if new password is shorter than six character
                if(newPass.length()<6){
                    password_error.setVisibility(View.VISIBLE);
                    password_error.setText("*New password must be of at least six characters");
                    et_confirm_pass.setText("");
                    return;
                }

                if(!newPass.equals(confirmPass)){
                    password_error.setVisibility(View.VISIBLE);
                    password_error.setText("*New password and confirm password do not match");
                    et_confirm_pass.setText("");
                    return;
                }

                password_error.setVisibility(View.GONE);

                //Toast.makeText(EditProfile.this,"new Pass "+newPass,Toast.LENGTH_SHORT).show();

                //et_password.setText(newPass);
/*
                sendNewPasswordToDataBase(newPass);
*/
                alertDialog.dismiss();

            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }
}

package com.example.dtcbuspass.LoginSignUp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.dtcbuspass.HomePage;
import com.example.dtcbuspass.ProfileActivity;
import com.example.dtcbuspass.R;
import com.example.dtcbuspass.network.ApiService;
import com.example.dtcbuspass.network.RetrofitBuilder;
import com.example.dtcbuspass.network.TokenManager;
import com.example.dtcbuspass.network.Utils;
import com.example.dtcbuspass.network.entities.AccessToken;
import com.example.dtcbuspass.network.entities.ApiError;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView sighUp;

    @BindView(R.id.edt_email_login)

    TextInputLayout userEmail;

    @BindView(R.id.edt_pass_login)

    TextInputLayout userPass;

    ApiService service;


    TokenManager tokenManager;

    AwesomeValidation validator;
    Call<AccessToken> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sighUp=findViewById(R.id.btv_signup);


        ButterKnife.bind(this);

        service = RetrofitBuilder.createService(ApiService.class);

        tokenManager = TokenManager.getInstance(getSharedPreferences("preferences", MODE_PRIVATE));

        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);

        setupRules();

        sighUp.setOnClickListener(this);


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

            case R.id.btv_signup:
                Intent signup=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(signup);
                //finish();
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


    @OnClick(R.id.login_to_app)

    void login() {

        String email = userEmail.getEditText().getText().toString();

        String password = userPass.getEditText().getText().toString();

        userEmail.setError(null);
        userPass.setError(null);

        validator.clear();

        if (validator.validate()) {

            call = service.login(email, password);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {




                    if (response.isSuccessful()) {

                        tokenManager.saveToken(response.body());

                        Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(LoginActivity.this, HomePage.class));
                        finish();

                    } else {

                        if (response.code() == 422) {

                            handleErrors(response.errorBody());
                        }

                        if (response.code() == 401) {
                            ApiError apiError = Utils.convertErrors(response.errorBody());

                            Toast.makeText(LoginActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {

                }
            });

        }
    }




    private void handleErrors(ResponseBody response)
    {


        ApiError apiError = Utils.convertErrors(response);

        for(Map.Entry<String, List<String>> error : apiError.getError().entrySet())
        {


            if(error.getKey().equals("username"))
            {
                userEmail.setError(error.getValue().get(0));
            }

            if(error.getKey().equals("password"))
            {
                userPass.setError(error.getValue().get(0));
            }


        }

    }

    public  void  setupRules()
    {

       /* validator.addValidation(this, R.id.edt_email_login, Patterns.EMAIL_ADDRESS, R.string.err_email);

        validator.addValidation(this, R.id.edt_pass_login, "[a-zA-Z0-9]{6,}", R.string.err_password);
*/


    }

    protected void onDestroy()
    {

        super.onDestroy();

        if (call==null)
        {

            call.cancel();
            call = null;
        }
    }

}
package com.example.dtcbuspass.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
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

public class SignUpActivity extends AppCompatActivity {

    TextView haveAccount;


    private static final String TAG = "RegisterActiviy";

    @BindView(R.id.edt_name)
    TextInputLayout usrName;

    @BindView(R.id.edt_email)
    TextInputLayout userEmail;

   /* @BindView(R.id.edt_phone)
    TextInputLayout userPhone;*/

    @BindView(R.id.edt_pass)
    TextInputLayout usrPass;


    ApiService service;
    Call<AccessToken> call;

    TokenManager tokenManager;
    AwesomeValidation validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        ButterKnife.bind(this);

        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("preferences",MODE_PRIVATE));


        validator =  new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);


        setupRules();

        haveAccount=findViewById(R.id.btv_already_account);
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @OnClick(R.id.sign_up)

    void register()

    {

        String name = usrName.getEditText().getText().toString();
        String email = userEmail.getEditText().getText().toString();
       // String phone = userPhone.getEditText().getText().toString();
        String password = usrPass.getEditText().getText().toString();

        usrName.setError(null);
        userEmail.setError(null);
       // userPhone.setError(null);
        usrPass.setError(null);

        validator.clear();

        if(validator.validate()) {

            call = service.register(name, email,password);

            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {


                    Log.w(TAG, "onResponse: " + response);


                    if (response.code()==201) {

                        Toast.makeText(SignUpActivity.this, "Successfully created user", Toast.LENGTH_LONG).show();

                        tokenManager.saveToken(response.body());



                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();


                    } else {


                        handleErrors(response.errorBody());


                    }

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {

                    Log.w(TAG, "onFailure: " + t.getMessage());

                }
            });

        }

    }


    private void handleErrors(ResponseBody response)
    {


        ApiError apiError = Utils.convertErrors(response);

        for(Map.Entry<String, List<String>> error : apiError.getError().entrySet())
        {

            if(error.getKey().equals("name"))
            {
                usrName.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("email"))
            {
                userEmail.setError(error.getValue().get(0));
            }

        /*    if(error.getKey().equals("phone"))
            {
                userPhone.setError(error.getValue().get(0));
            }*/
            if(error.getKey().equals("password"))
            {
                usrPass.setError(error.getValue().get(0));
            }


        }

    }

    public  void  setupRules()
    {
/*


        validator.addValidation(this, R.id.edt_name,  RegexTemplate.NOT_EMPTY, R.string.err_name);
        validator.addValidation(this, R.id.edt_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
       // validator.addValidation(this, R.id.edt_phone,"[0-9]{10,}", R.string.err_phone );

        validator.addValidation(this, R.id.edt_pass, "[a-zA-Z0-9]{6,}", R.string.err_password);

*/



    }

    protected void onDestroy()
    {
        super.onDestroy();

        if(call != null)
            call.cancel();
        call = null;


    }
}

package com.example.dtcbuspass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dtcbuspass.LoginSignUp.LoginActivity;
import com.example.dtcbuspass.LoginSignUp.SignUpActivity;
import com.example.dtcbuspass.SelectPass.GeneralPassActivity;
import com.example.dtcbuspass.SelectPass.StudentPassActivity;
import com.example.dtcbuspass.network.ApiService;
import com.example.dtcbuspass.network.FileUtils;
import com.example.dtcbuspass.network.RetrofitBuilder;
import com.example.dtcbuspass.network.entities.AccessToken;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Feedback_and_Suggestions extends AppCompatActivity {

    ImageView back_arrow;

    private ProgressBar mProgressBar;






    @BindView(R.id.feedback_name)
    TextInputLayout Name;

    @BindView(R.id.feedback_email)
    TextInputLayout Email;

    @BindView(R.id.feeedback_description)
    TextInputLayout Description;



    ApiService service;

    Call<AccessToken> call;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_and__suggestions);


        ButterKnife.bind(this);


        service = RetrofitBuilder.createService(ApiService.class);


        mProgressBar = findViewById(R.id.feedback_progressBar);





        back_arrow=findViewById(R.id.buy_pas_back_btn);




        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();

            }
        });

    }


    @OnClick(R.id.submit_feedback)

    void feedbackSuggestion() {



        String name = Name.getEditText().getText().toString();
        String email = Email.getEditText().getText().toString();
        String description = Description.getEditText().getText().toString();









        call = service.feedbackSuggestion(name, email,description);




        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {




                if (response.code()==201) {


                    mProgressBar.setVisibility(View.VISIBLE);

                    startActivity(new Intent(Feedback_and_Suggestions.this, SuccesfulSubmittedActivity.class));

                    finish();


                }

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

                mProgressBar.setVisibility(View.GONE);



            }
        });

    }



    protected void onDestroy()
    {
        super.onDestroy();

        if(call != null)
            call.cancel();
        call = null;



    }



}

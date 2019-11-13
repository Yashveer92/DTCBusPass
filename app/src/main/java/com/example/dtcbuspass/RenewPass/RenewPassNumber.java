package com.example.dtcbuspass.RenewPass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dtcbuspass.R;

public class RenewPassNumber extends AppCompatActivity {

    Button submit;
    ImageView back_arrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_pass_number);

        submit=findViewById(R.id.submit_btn);
        back_arrow=findViewById(R.id.buy_pas_back_btn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(RenewPassNumber.this,RenewBussPass.class));
            }
        });




        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });
    }
}

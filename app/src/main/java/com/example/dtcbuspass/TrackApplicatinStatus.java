package com.example.dtcbuspass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TrackApplicatinStatus extends AppCompatActivity {

    Button track_btn;
    ImageView back_arrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_applicatin_status);

        track_btn=findViewById(R.id.track_now_btn);
        back_arrow=findViewById(R.id.buy_pas_back_btn);

        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TrackApplicatinStatus.this,Application_status_details.class));

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

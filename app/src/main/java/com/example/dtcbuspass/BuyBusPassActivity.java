package com.example.dtcbuspass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.dtcbuspass.SelectPass.DisabledPassActivity;
import com.example.dtcbuspass.SelectPass.GeneralPassActivity;
import com.example.dtcbuspass.SelectPass.SeniorCitizenActivity;
import com.example.dtcbuspass.SelectPass.StudentPassActivity;

import java.text.DateFormat;
import java.util.Calendar;

public class BuyBusPassActivity extends AppCompatActivity  implements PassDurationDialog.SingleChoiceListner {

    private TextView tvChangePassDuration;
    private static final String TAG = "BuyBusPassActivity";
    ImageView back_arrow;
    RadioGroup radioGroup;
    private TextView mDisplayDate,changeDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_bus_pass);
        tvChangePassDuration=findViewById(R.id.change_pass_duration_tv);
        changeDate =  findViewById(R.id.change_date_tv);
        mDisplayDate=findViewById(R.id.display_date_tv);

        radioGroup=findViewById(R.id.radioGroup);
        back_arrow=findViewById(R.id.buy_pas_back_btn);


        datePickerFunction();
        radioGroupItemsClickListner();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });



        tvChangePassDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment singleChoiceDialog=new PassDurationDialog();
                singleChoiceDialog.setCancelable(false);
                singleChoiceDialog.show(getSupportFragmentManager(),"Single Choice Dialog");

            }
        });

    }



    @Override
    public void onPositiveButtonClicked(String[] list, int position) {

       TextView tvSelectPassDuration=findViewById(R.id.selct_pass_duration);


        tvSelectPassDuration.setText(list[position]);


    }

    @Override
    public void onNegativeButtonClicked() {

    }


    private void datePickerFunction() {




         //Show current date

        Calendar calendar=Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance().format(calendar.getTime());
        mDisplayDate.setText(currentDate);



        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuyBusPassActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

    }


    private void radioGroupItemsClickListner() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {


                if (checkedId==R.id.general_radio_btn)
                {

                    startActivity(new Intent(BuyBusPassActivity.this, GeneralPassActivity.class));


                }

                else if(checkedId==R.id.student_radio_btn)
                {
                    startActivity(new Intent(BuyBusPassActivity.this, StudentPassActivity.class));


                }
                else if(checkedId==R.id.disable_radio_btn)
                {
                    startActivity(new Intent(BuyBusPassActivity.this, DisabledPassActivity.class));


                }

                else if(checkedId==R.id.senr_citizen_radio_btn)
                {
                    startActivity(new Intent(BuyBusPassActivity.this, SeniorCitizenActivity.class));


                }


            }
        });
    }



}

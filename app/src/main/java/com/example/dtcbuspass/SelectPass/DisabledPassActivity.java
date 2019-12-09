package com.example.dtcbuspass.SelectPass;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dtcbuspass.BuyBusPassActivity;
import com.example.dtcbuspass.PassDurationDialog;
import com.example.dtcbuspass.R;
import com.example.dtcbuspass.SuccesfulSubmittedActivity;
import com.example.dtcbuspass.network.ApiService;
import com.example.dtcbuspass.network.FileUtils;
import com.example.dtcbuspass.network.RetrofitBuilder;
import com.example.dtcbuspass.network.entities.AccessToken;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisabledPassActivity extends AppCompatActivity implements View.OnClickListener {

    File mPhotoFile;
    private Button btn,procced;

    private ProgressBar mProgressBar;


    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static final int REQUEST_GALLERY_IMAGE = 5;
    TextView date_of_birth;
    String dob,genderSelected;

    Bitmap bitmap;
    private String  buttonId;
    TextView remove_photograph_tv, remove_aadhar_tv,remove_certificate_tv,certficate_tv,photograph_tv,addharcard_tv;
    ImageView photograph, aadharCard,certificate, add_photograph_btn, add_Aadhar_btn,add_certificate_btn;


    private Uri photoURI = null;

    private  Uri photo,adhar,certft;


    ImageView back_arrow;


    int flag=0,flag2=0;
    String[] gender = { "Select Gender","Male", "Female"};
    Spinner spin;


    @BindView(R.id.first_name_disabled_pass)
    TextInputLayout firstName;

    @BindView(R.id.last_name_disabled_pass)
    TextInputLayout lastName;

    @BindView(R.id.phone_disabled_pass)
    TextInputLayout userPhone;


    ApiService service;

    Call<AccessToken> call;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disabled_pass);

        ButterKnife.bind(this);

        mProgressBar = findViewById(R.id.progressBar_proceed_disabled_pass);


        service = RetrofitBuilder.createService(ApiService.class);


        photograph =findViewById(R.id.photograph_img_vu_disabled_pass);
        aadharCard =findViewById(R.id.addharCard_img_vu_disabled_pass);
        certificate=findViewById(R.id.certificate_img_vu_disabled_pass);
        add_photograph_btn =findViewById(R.id.add_photograph_disabled_pass_btn);
        add_certificate_btn=findViewById(R.id.add_disbled_certificate_btn);
        // addharcard_tv=findViewById(R.id.addharcard_tv);
        add_Aadhar_btn =findViewById(R.id.add_addhar_disabled_pass_btn);

        remove_photograph_tv =findViewById(R.id.remove_photograph_disabled_pass);
        remove_aadhar_tv =findViewById(R.id.remove_aadhar_disabled_pass);
        remove_certificate_tv=findViewById(R.id.remove_disabled_certificate);
        date_of_birth=findViewById(R.id.dob_disabled_pass);
        addharcard_tv=findViewById(R.id.addharcard_disabled_pass_tv);
        photograph_tv=findViewById(R.id.photograph_disabled_pass_tv);
        certficate_tv=findViewById(R.id.disablitity_certificate_tv);



        spin =  findViewById(R.id.choose_gender_disabled_pass);
        remove_aadhar_tv.setOnClickListener(this);
        remove_photograph_tv.setOnClickListener(this);
        remove_certificate_tv.setOnClickListener(this);

        procced=findViewById(R.id.proceed_disabled_pass);

        procced.setOnClickListener(this);

        back_arrow=findViewById(R.id.disabled_pass_back_btn);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                genderSelected = parent.getItemAtPosition(position).toString();

                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#5a5a5a"));
                ((TextView) parent.getChildAt(0)).setTextSize(17);

                // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DisabledPassActivity.this,
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

                dob = year + "/" + month + "/" + day;
                date_of_birth.setText(dob);
            }
        };
    }








    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName(),
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Save a file: path for use with ACTION_VIEW intents
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        String currentPhotoPath = image.getAbsolutePath();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && data != null) {
            byte[] bytes = data.getByteArrayExtra("image");
            Bitmap bmp = null;
            if (bytes != null) {
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            }
        }





        if (requestCode == REQUEST_GALLERY_IMAGE)
        {
            if (data != null && resultCode== RESULT_OK)
            {


                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                cursor.moveToFirst();
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                if(bitmap != null && !bitmap.isRecycled())
                {
                    bitmap = null;
                }

                bitmap = BitmapFactory.decodeFile(filePath);


                setImage(bitmap);




            }
            else
            {
                Log.d("Status:", "Action Not Completed");
            }
        }



    }

    private void setImage(Bitmap bitmap) {




        if(flag==1)
        {
            photograph.setImageBitmap(bitmap);
            add_photograph_btn.setVisibility(View.GONE);
            remove_photograph_tv.setVisibility(View.VISIBLE);

            photo = getImageUri(this, bitmap);



            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(0);
                photograph_tv.setLayoutParams(params);
            }
            photograph.getLayoutParams().height = 400;

            photograph.getLayoutParams().width = 650;

            photograph.setScaleType(ImageView.ScaleType.FIT_XY);

        }
        else if (flag==2)
        {

            aadharCard.setImageBitmap(bitmap);
            add_Aadhar_btn.setVisibility(View.GONE);
            remove_aadhar_tv.setVisibility(View.VISIBLE);
            addharcard_tv=findViewById(R.id.addharcard_disabled_pass_tv);

            adhar = getImageUri(this, bitmap);


            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(0);
                addharcard_tv.setLayoutParams(params);
            }

            aadharCard.getLayoutParams().height = 400;

            aadharCard.getLayoutParams().width = 650;

            aadharCard.setScaleType(ImageView.ScaleType.FIT_XY);


        }

        else if (flag==3)
        {

            certificate.setImageBitmap(bitmap);
            add_certificate_btn.setVisibility(View.GONE);
            remove_certificate_tv.setVisibility(View.VISIBLE);
            certficate_tv=findViewById(R.id.disablitity_certificate_tv);

            certft = getImageUri(this, bitmap);


            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(0);
                certficate_tv.setLayoutParams(params);
            }

            certificate.getLayoutParams().height = 400;

            certificate.getLayoutParams().width = 650;

            certificate.setScaleType(ImageView.ScaleType.FIT_XY);


        }







    }

    private Uri getImageUri(Context inContext, Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitmap, "photograph", null);
        return Uri.parse(path);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onAddReportButton(View view) {
        String buttonId = getResources().getResourceEntryName(view.getId());



        final CharSequence[] items={"Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DisabledPassActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (items[i].equals("Gallery")) {

                    switch(view.getId())
                    {

                        case R.id.add_photograph_disabled_pass_btn:
                            flag=1;
                            String camera1="photograph";
                            chooseGalleryPhoto(camera1,buttonId);
                            break;
                        case R.id.add_addhar_disabled_pass_btn:
                            flag=2;
                            String camera2="aadharCard";
                            chooseGalleryPhoto(camera2,buttonId);
                            break;
                        case R.id.add_disbled_certificate_btn:
                            flag=3;
                            String camera3="certificate";
                            chooseGalleryPhoto(camera3,buttonId);
                            break;
                    }



                }

                else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }

            }
        });

        builder.show();






    }







    private void chooseGalleryPhoto(String str1, String str2) {

        buttonId = str2;




        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);






    }








    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onAddReportButton(String buttonId) {
        this.buttonId = buttonId;
        if (!buttonId.endsWith("z")) {
            btn = findViewById(getResources().getIdentifier(buttonId, "id", getApplicationContext().getPackageName()));

            btn.setTextColor(getColor(android.R.color.black));
        } else {
            buttonId = buttonId.substring(0, buttonId.length()-1);
            int final_index = Integer.parseInt(buttonId);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onAdapterInteraction(String buttonId) {
        onAddReportButton(buttonId);

    }



    private void reDefinedWidthHeight() {




        if(flag2==1)
        {

            photograph.getLayoutParams().height = 0;

            photograph.getLayoutParams().width = 0;
            add_photograph_btn.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(20);
                photograph_tv.setLayoutParams(params);
            }


        }

        else if(flag2==2)
        {


            aadharCard.getLayoutParams().height = 0;

            aadharCard.getLayoutParams().width = 0;
            add_Aadhar_btn.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(20);
                addharcard_tv.setLayoutParams(params);
            }

        }

        else if(flag2==3)
        {


            certificate.getLayoutParams().height = 0;

            certificate.getLayoutParams().width = 0;
            add_certificate_btn.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(20);
                certficate_tv.setLayoutParams(params);
            }

        }


    }


    @Override
    public void onClick(View view) {


        switch (view.getId())
        {
            case R.id.remove_photograph_disabled_pass:
                flag2=1;
                photograph.setImageBitmap(null);
                remove_photograph_tv.setVisibility(View.GONE);
                break;

            case R.id.remove_aadhar_disabled_pass:
                flag2=2;
                aadharCard.setImageBitmap(null);
                remove_aadhar_tv.setVisibility(View.GONE);
                break;

            case R.id.remove_disabled_certificate:
                flag2=3;
                certificate.setImageBitmap(null);
                remove_certificate_tv.setVisibility(View.GONE);
                break;

            case R.id.proceed_disabled_pass:

                RegDisabledPass(photo,adhar,certft);
                break;



        }

        reDefinedWidthHeight();

    }

    private void RegDisabledPass(Uri photo, Uri adhar, Uri certft) {


        String Fname = firstName.getEditText().getText().toString();
        String Lname = lastName.getEditText().getText().toString();
        String mobile = userPhone.getEditText().getText().toString();
        // String email = userEmail.getEditText().getText().toString();



        RequestBody first_name = RequestBody.create(MultipartBody.FORM, Fname);

        RequestBody last_name = RequestBody.create(MultipartBody.FORM, Lname);

        RequestBody phone = RequestBody.create(MultipartBody.FORM, mobile);

        RequestBody date_of_birth = RequestBody.create(MultipartBody.FORM, dob);


        RequestBody gender = RequestBody.create(MultipartBody.FORM, genderSelected);







        File orginalFile1 = FileUtils.getFile(this, photo);
        File orginalFile2 = FileUtils.getFile(this, adhar);
        File orginalFile3 = FileUtils.getFile(this, certft);

        RequestBody photograph = RequestBody.create(

                MediaType.parse(getContentResolver().getType(photo)),
                orginalFile1);

        RequestBody adharCard = RequestBody.create(

                MediaType.parse(getContentResolver().getType(adhar)),
                orginalFile2);


        RequestBody certificate = RequestBody.create(

                MediaType.parse(getContentResolver().getType(certft)),
                orginalFile3);





        MultipartBody.Part file1 = MultipartBody.Part.createFormData("photograph", orginalFile1.getName(), photograph);

        MultipartBody.Part file2 = MultipartBody.Part.createFormData("adhar_card", orginalFile2.getName(), adharCard);

        MultipartBody.Part file3 = MultipartBody.Part.createFormData("certificate", orginalFile3.getName(), certificate);




        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://192.168.43.27/PassApi/public/api/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiService client = retrofit.create(ApiService.class);

        Call<ResponseBody> call = client.RegDisabledPass( first_name,last_name,phone, date_of_birth ,gender,file1,file2,file3);

        mProgressBar.setVisibility(View.VISIBLE);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                if (response.code()==201) {


                    startActivity(new Intent(DisabledPassActivity.this, SuccesfulSubmittedActivity.class));

                    finish();

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
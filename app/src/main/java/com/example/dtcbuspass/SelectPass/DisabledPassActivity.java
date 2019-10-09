package com.example.dtcbuspass.SelectPass;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dtcbuspass.BuyBusPassActivity;
import com.example.dtcbuspass.PassDurationDialog;
import com.example.dtcbuspass.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DisabledPassActivity extends AppCompatActivity implements View.OnClickListener {

    File mPhotoFile;
    private Button btn;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static final int REQUEST_GALLERY_IMAGE = 5;
    TextView date_of_birth;
    Bitmap bitmap;
    private String  buttonId;
    TextView remove_img_tv1,remove_img_tv2,addharcard_tv;
    ImageView doc1,doc2,add_doc_btn1,add_doc_btn2;

    private Uri photoURI = null;


    int flag=0,flag2=0;
    String[] gender = { "Select Gender","Male", "Female"};
    Spinner spin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disabled_pass);



        doc1=findViewById(R.id.doc_img1);
        doc2=findViewById(R.id.doc_img2);
        add_doc_btn1=findViewById(R.id.add_doc1_img);
        addharcard_tv=findViewById(R.id.addharcard_tv);
        add_doc_btn2=findViewById(R.id.add_doc2_img);

        remove_img_tv1=findViewById(R.id.remove1);
        remove_img_tv2=findViewById(R.id.remove2);
        date_of_birth=findViewById(R.id.dob);



        remove_img_tv2.setOnClickListener(this);
        remove_img_tv1.setOnClickListener(this);

        spin =  findViewById(R.id.choose_gender);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                String item = parent.getItemAtPosition(position).toString();

                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#B8B5B5"));
                ((TextView) parent.getChildAt(0)).setTextSize(17);

                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


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

                String date = day + "/" + month + "/" + year;
                date_of_birth.setText(date);
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
            doc1.setImageBitmap(bitmap);
            add_doc_btn1.setVisibility(View.GONE);
            remove_img_tv1.setVisibility(View.VISIBLE);
            addharcard_tv=findViewById(R.id.addharcard_tv);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(0);
                addharcard_tv.setLayoutParams(params);
            }
            doc1.getLayoutParams().height = 400;

            doc1.getLayoutParams().width = 650;

            doc1.setScaleType(ImageView.ScaleType.FIT_XY);

        }
        else if (flag==2)
        {

            doc2.setImageBitmap(bitmap);
            add_doc_btn2.setVisibility(View.GONE);
            remove_img_tv2.setVisibility(View.VISIBLE);
            addharcard_tv=findViewById(R.id.addharcard_tv2);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(0);
                addharcard_tv.setLayoutParams(params);
            }

            doc2.getLayoutParams().height = 400;

            doc2.getLayoutParams().width = 650;

            doc2.setScaleType(ImageView.ScaleType.FIT_XY);


        }







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

                        case R.id.add_doc1_img:
                            flag=1;
                            String camera1="add_doc1_img";
                            chooseGalleryPhoto(camera1,buttonId);
                            break;
                        case R.id.add_doc2_img:
                            flag=2;
                            String camera2="add_doc2_img";
                            chooseGalleryPhoto(camera2,buttonId);
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

            doc1.getLayoutParams().height = 0;

            doc1.getLayoutParams().width = 0;
            add_doc_btn1.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(20);
                addharcard_tv.setLayoutParams(params);
            }


        }

        else if(flag2==2)
        {


            doc2.getLayoutParams().height = 0;

            doc2.getLayoutParams().width = 0;
            add_doc_btn2.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(20);
                addharcard_tv.setLayoutParams(params);
            }

        }


    }


    public void onClick(View view) {


        switch (view.getId())
        {
            case R.id.remove1:
                flag2=1;
                doc1.setImageBitmap(null);
                remove_img_tv1.setVisibility(View.GONE);
                break;

            case R.id.remove2:
                flag2=2;
                doc2.setImageBitmap(null);
                remove_img_tv2.setVisibility(View.GONE);
                break;



        }

        reDefinedWidthHeight();

    }
}

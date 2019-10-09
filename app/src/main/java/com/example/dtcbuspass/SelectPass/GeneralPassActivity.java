package com.example.dtcbuspass.SelectPass;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dtcbuspass.BuyBusPassActivity;
import com.example.dtcbuspass.EditProfileActivity;
import com.example.dtcbuspass.ImagePickerActivity;
import com.example.dtcbuspass.R;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GeneralPassActivity extends AppCompatActivity implements AddCustomerDocumentAdapter.OnAdapterClickListener, View.OnClickListener {


    RecyclerView.Adapter imageDocumentAdapter;
    RecyclerView imageDocumentView;
    File mPhotoFile;
    private Button btn,addDetails;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String bookingId, buttonId;
    static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_GALLERY_IMAGE = 5;
    TextView date_of_birth;
    Bitmap bitmap;
    TextView remove_img_tv1,remove_img_tv2,addharcard_tv;
    ImageView doc1,doc2,add_doc_btn1,add_doc_btn2;

    private int rv_index = 0;
    private Uri photoURI = null;
    private Map<String, Integer> dataIndex;
    private List<AddCustomerFDocumentData> reportDocument;

    int flag=0,flag2=0;
    String[] gender = { "Select Gender","Male", "Female"};
    Spinner spin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_pass);



        doc1=findViewById(R.id.doc_img1);
        doc2=findViewById(R.id.doc_img2);
        add_doc_btn1=findViewById(R.id.add_doc1_img);
        addharcard_tv=findViewById(R.id.addharcard_tv);
        add_doc_btn2=findViewById(R.id.add_doc2_img);

        remove_img_tv1=findViewById(R.id.remove1);
        remove_img_tv2=findViewById(R.id.remove2);
        date_of_birth=findViewById(R.id.dob);
        dataIndex = new HashMap<>();
        reportDocument = new ArrayList<>();
        //  reportDocument = initDocumentData();

        bookingId = getIntent().getStringExtra("bookingId");



        imageDocumentView = findViewById(R.id.rv_imagedocument);
        imageDocumentView.setHasFixedSize(false);
        imageDocumentView.setLayoutManager(new LinearLayoutManager(imageDocumentView.getContext(), LinearLayoutManager.HORIZONTAL, false));


        imageDocumentAdapter = new AddCustomerDocumentAdapter(reportDocument, imageDocumentView.getContext());
        imageDocumentView.setAdapter(imageDocumentAdapter);


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
                        GeneralPassActivity.this,
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



    public byte[] getBytes(int i) throws IOException {

        InputStream iStream =   getContentResolver().openInputStream(reportDocument.get(i).getPhotoUri());
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        if (iStream != null) {
            while ((len = iStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } else
            Log.e("iStream: ", "null");
        assert iStream != null;
        iStream.close();
        return byteBuffer.toByteArray();
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
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
           /* int dIndex = dataIndex.get(buttonId);
            reportDocument.set(dIndex, new AddCustomerFDocumentData(buttonId, photoURI, "1"));
            imageDocumentAdapter.notifyItemChanged(dIndex);
            imageDocumentView.scrollToPosition(dIndex);*/
            //  btn.setBackgroundResource(R.drawable.customer_reprt_bt02);
            //  btn.setTextColor(getColor(android.R.color.white));

        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            Log.e("no image", "no image was captured!");
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            reportDocument.add(rv_index, new AddCustomerFDocumentData(buttonId, photoURI, "3"));
            imageDocumentAdapter.notifyItemInserted(rv_index++);
            imageDocumentView.scrollToPosition(rv_index - 1);

        } else if (requestCode == 3) {
            Log.e("no image", "no image was captured!");
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



/*
                ScrollView scroll_view=findViewById(R.id.sv);
                scroll_view.fullScroll(ScrollView.FOCUS_UP);*/



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
/*
    private String getPathFromURI(Uri contentUri) {


        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;

    }*/


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onAddReportButton(View view) {
       String buttonId = getResources().getResourceEntryName(view.getId());



            final CharSequence[] items={"Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(GeneralPassActivity.this);
            builder.setTitle("Add Image");

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (items[i].equals("Camera")) {

                        switch(view.getId())
                        {

                            case R.id.add_doc1_img:

                                String camera1="add_doc1_img";
                               // takePhoto(camera1,buttonId);
                                break;

                        /*    case R.id.bt_rvcamera2:
                                String camera2="bt_rvcamera2";
                                takePhoto(camera2,buttonId);
                                break;*/
                        }

                       // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       // startActivityForResult(intent, REQUEST_TAKE_PHOTO);

                    } else if (items[i].equals("Gallery")) {


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

/*
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);*/

                    }

                    else if (items[i].equals("Cancel")) {
                        dialogInterface.dismiss();
                    }

                }
            });

            builder.show();






    }





    private void takePhoto(String str1,String str2) {

        buttonId=str2;

        if ((!buttonId.equals(str1))) {
           // btn = findViewById(view.getId());
            if (reportDocument.get(dataIndex.get(buttonId)).getBtnstate().equals("0")) {
                if (ContextCompat.checkSelfPermission(GeneralPassActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GeneralPassActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO);
                    return;
                }
                dispatchTakePictureIntent(REQUEST_TAKE_PHOTO);
            } else if (reportDocument.get(dataIndex.get(buttonId)).getBtnstate().equals("1")) {
                reportDocument.set(dataIndex.get(buttonId), new AddCustomerFDocumentData(buttonId, null, "0"));
                imageDocumentAdapter.notifyItemChanged(dataIndex.get(buttonId));
                // btn.setBackgroundResource(R.drawable.customer_rprt_bt01);
              //  btn.setTextColor(getColor(android.R.color.black));
            }
        } else {
            if (ContextCompat.checkSelfPermission(GeneralPassActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GeneralPassActivity.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO);
                return;
            }
            dispatchTakePictureIntent(3);
        }


    }


       private void chooseGalleryPhoto(String str1, String str2) {

           buttonId = str2;

/*
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);*/

           if ((!buttonId.equals(str1))) {
               // btn = findViewById(view.getId());
               if (reportDocument.get(dataIndex.get(buttonId)).getBtnstate().equals("0")) {
                   Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   intent.setType("image/*");
                   startActivityForResult(intent, REQUEST_GALLERY_IMAGE);

               } else if (reportDocument.get(dataIndex.get(buttonId)).getBtnstate().equals("1")) {
                   reportDocument.set(dataIndex.get(buttonId), new AddCustomerFDocumentData(buttonId, null, "0"));
                   imageDocumentAdapter.notifyItemChanged(dataIndex.get(buttonId));
                   // btn.setBackgroundResource(R.drawable.customer_rprt_bt01);
                   //  btn.setTextColor(getColor(android.R.color.black));
               }
           }

           else {


               Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               intent.setType("image/*");
               startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
           }





       }








        @RequiresApi(api = Build.VERSION_CODES.M)
    public void onAddReportButton(String buttonId) {
        this.buttonId = buttonId;
        if (!buttonId.endsWith("z")) {
            btn = findViewById(getResources().getIdentifier(buttonId, "id", getApplicationContext().getPackageName()));

            reportDocument.set(dataIndex.get(buttonId), new AddCustomerFDocumentData(buttonId, null, "0"));
            imageDocumentAdapter.notifyItemChanged(dataIndex.get(buttonId));
            //btn.setBackgroundResource(R.drawable.customer_rprt_bt01);
           btn.setTextColor(getColor(android.R.color.black));
        } else {
            buttonId = buttonId.substring(0, buttonId.length()-1);
            int final_index = Integer.parseInt(buttonId);
            reportDocument.remove(final_index);
            imageDocumentView.getRecycledViewPool().clear();
            imageDocumentAdapter.notifyItemRemoved(final_index);

            // use below code only in case of crash due to position issue
//            imageDocumentAdapter.notifyDataSetChanged();

            rv_index--;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
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


    @Override
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

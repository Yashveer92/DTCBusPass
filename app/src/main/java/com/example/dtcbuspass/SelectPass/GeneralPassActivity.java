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
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dtcbuspass.EditProfileActivity;
import com.example.dtcbuspass.ImagePickerActivity;
import com.example.dtcbuspass.R;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GeneralPassActivity extends AppCompatActivity implements AddCustomerDocumentAdapter.OnAdapterClickListener {


    RecyclerView.Adapter imageDocumentAdapter;
    RecyclerView imageDocumentView;
    File mPhotoFile;
    private Button btn,addDetails;


    private String bookingId, buttonId;
    static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_GALLERY_IMAGE = 5;

    Bitmap bitmap;

    private int rv_index = 0;
    private Uri photoURI = null;
    private Map<String, Integer> dataIndex;
    private List<AddCustomerFDocumentData> reportDocument;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_pass);




        dataIndex = new HashMap<>();
        reportDocument = new ArrayList<>();
        //  reportDocument = initDocumentData();

        bookingId = getIntent().getStringExtra("bookingId");


        imageDocumentView = findViewById(R.id.rv_imagedocument);
        imageDocumentView.setHasFixedSize(false);
        imageDocumentView.setLayoutManager(new LinearLayoutManager(imageDocumentView.getContext(), LinearLayoutManager.HORIZONTAL, false));


        imageDocumentAdapter = new AddCustomerDocumentAdapter(reportDocument, imageDocumentView.getContext());
        imageDocumentView.setAdapter(imageDocumentAdapter);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                ImageView showImage=findViewById(R.id.yas);

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


               // showImage.setBackgroundResource(0);
                showImage.setImageBitmap(bitmap);

                ImageView iv = (ImageView) findViewById(R.id.yas);
                iv.getLayoutParams().height = 300;

                iv.getLayoutParams().width = 500;

                iv.setScaleType(ImageView.ScaleType.FIT_XY);
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



            final CharSequence[] items={"Camera","Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(GeneralPassActivity.this);
            builder.setTitle("Add Image");

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (items[i].equals("Camera")) {

                        switch(view.getId())
                        {

                            case R.id.bt_rvcamera:

                                String camera1="bt_rvcamera";
                                takePhoto(camera1,buttonId);
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

                            case R.id.bt_rvcamera:

                                String camera1="bt_rvcamera";
                                chooseGalleryPhoto(camera1,buttonId);
                                break;
/*
                            case R.id.bt_rvcamera2:
                                String camera2="bt_rvcamera2";
                                chooseGalleryPhoto(camera2,buttonId);
                                break;*/
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






}

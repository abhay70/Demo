package com.example.demo.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.Database.ChatDBHelper;
import com.example.demo.Database.ChatDBUtility;
import com.example.demo.Model.DataRecords;
import com.example.demo.R;
import com.google.common.collect.Iterables;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddDataActivity extends AppCompatActivity {

    TextView add_image;
    EditText et_name,et_age,et_phone,et_email;
    ImageView imageView;
    Button save;

    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };


    private static int RESULT_LOAD_IMG = 1;
    protected static final int REQUEST_CAMERA = 2;

    Dialog  dialog;

    ChatDBHelper chatDBHelper;
    ChatDBUtility chatDBUtility;

    DataRecords lastElement;
    String  capture_dt = "", dateString,imageEncoded;
    File file;



    ArrayList<DataRecords> dataRecords=new ArrayList<DataRecords>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        chatDBUtility = new ChatDBUtility();
        chatDBHelper = chatDBUtility.CreateChatDB(AddDataActivity.this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initializeView();
        initializeListener();
        setData();

    }

    private void setData() {

        dataRecords=chatDBUtility.GetDataList(chatDBHelper);
        lastElement = Iterables.getLast(dataRecords, null);


    }

    private void initializeListener() {

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddDataActivity.this,HomepageActivity.class);
                startActivity(intent);
                AddDataActivity.this.finish();
            }
        });


        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!et_email.getText().toString().equals(""))
                {
                    try {
                        chatDBUtility.UpdateEmail(chatDBHelper,et_email.getText().toString(),lastElement.getId());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_age.getText().toString().equals(""))
                {
                    try {
                        chatDBUtility.UpdateAge(chatDBHelper, Integer.parseInt(et_email.getText().toString()),lastElement.getId());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_phone.getText().toString().equals(""))
                {
                    try {
                        chatDBUtility.UpdatePhone(chatDBHelper, Integer.parseInt(et_phone.getText().toString()),lastElement.getId());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_name.getText().toString().equals(""))
                {
                    try {
                        chatDBUtility.UpdateName(chatDBHelper, et_name.getText().toString(),lastElement.getId());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    private void addImage() {

       dialog = new Dialog(AddDataActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //tell the Dialog to use the dialog.xml as it's layout description

        dialog.setContentView(R.layout.gallery_camera_popup);
        dialog.setCancelable(true);

        RelativeLayout galleryLayout = (RelativeLayout) dialog.findViewById(R.id.gallery_layout);
        RelativeLayout cameraLayout = (RelativeLayout) dialog.findViewById(R.id.camera_layout);


        galleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPermissions())
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);

                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

                }else
                {
                    Toast.makeText(AddDataActivity.this, "Please provide permission to proceed", Toast.LENGTH_SHORT).show();
                }


                dialog.dismiss();
            }
        });

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(checkPermissions())
            {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment
                        .getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, REQUEST_CAMERA);
            }else
            {
                Toast.makeText(AddDataActivity.this, "Please provide permission to proceed", Toast.LENGTH_SHORT).show();
            }
                dialog.dismiss();


            }
        });


        dialog.show();



    }

    private void initializeView() {


        save=(Button)findViewById(R.id.save);
        add_image=(TextView)findViewById(R.id.add_image);
        et_email=(EditText) findViewById(R.id.et_email);
        et_age=(EditText) findViewById(R.id.et_age);
        et_phone=(EditText) findViewById(R.id.et_phone);
        et_name=(EditText) findViewById(R.id.image);
        imageView=(ImageView)findViewById(R.id.imageView);

        DataRecords dataRecords1=new DataRecords();
        chatDBUtility.AddToDataListDB(chatDBHelper,dataRecords1);


    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(AddDataActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AddDataActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK
                    && null != data) {


                String[] filePathColumn = {MediaStore.Images.Media.DATA};


                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                dateString = sdf.format(date);


                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    // Get the cursor

                    String selectedImage = getPath(AddDataActivity.this, mImageUri);

                    String[] proj = {MediaStore.Images.Media.DATA};
                    String result = null;

                    CursorLoader cursorLoader = new CursorLoader(
                            AddDataActivity.this,
                            mImageUri, proj, null, null, null);
                    Cursor cursor = cursorLoader.loadInBackground();

                    if (cursor != null) {
                        int column_index =
                                cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        imageEncoded = cursor.getString(column_index);
                    }



                    sdf = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                    capture_dt = sdf.format(new Date());

                    System.out.println("current_dt :" + capture_dt);

                    chatDBUtility.UpdateImageUrl(chatDBHelper,selectedImage,lastElement.getId());
                    Picasso.get().load(new File(selectedImage)).fit().error(getResources().getDrawable(R.drawable.ic_error_outline_black_24dp)).into(imageView);



                    try {
                        // images.setLat(String.valueOf(DataCaptureActivity.oldLocation.getLatitude()));
                        // images.setLng(String.valueOf(DataCaptureActivity.oldLocation.getLongitude()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }







                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = AddDataActivity.this.getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();
                            String selectedImage = getPath(AddDataActivity.this, uri);

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                            sdf = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                            capture_dt = sdf.format(new Date());


                            Picasso.get().load(new File(selectedImage)).fit().error(getResources().getDrawable(R.drawable.ic_error_outline_black_24dp)).into(imageView);
                            chatDBUtility.UpdateImageUrl(chatDBHelper,selectedImage,lastElement.getId());

                            cursor.close();

                        }



                    }
                }


            }

            else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

                File f1 = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f1.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f1 = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f1.getAbsolutePath(),
                            btmapOptions);


                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            +  "default";
                    //f.delete();
                    OutputStream fOut = null;
                    file = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp" + String.valueOf(System
                            .currentTimeMillis()) + " .jpg");


                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                capture_dt = sdf.format(new Date());


                chatDBUtility.UpdateImageUrl(chatDBHelper,file.getPath(),lastElement.getId());
                Picasso.get().load(new File(file.getPath())).fit().error(getResources().getDrawable(R.drawable.ic_error_outline_black_24dp)).into(imageView);






            }
            else {
                Toast.makeText(AddDataActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(AddDataActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(et_name.getText().toString().equals(""))
        {
            chatDBUtility.DeleteFromDataAccTOID(chatDBHelper,lastElement.getId());

        }
        try {

            Toast.makeText(this, "Please Enter Name to save data", Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        this.finish();
    }
}

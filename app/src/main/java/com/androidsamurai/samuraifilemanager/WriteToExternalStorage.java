package com.androidsamurai.samuraifilemanager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToExternalStorage extends AppCompatActivity {
    private Boolean externalStorageAvailable;
    private static final String LOG_TAG_EXTERNAL_STORAGE = "EXTERNAL_STORAGE";
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_to_external_storage);

        Toolbar toolbar = findViewById(R.id.write_toolbar);
        setSupportActionBar(toolbar);

        setTitle("Write to File");

        final EditText textContents = findViewById(R.id.text_file_contents);
        final EditText textFileName = findViewById(R.id.text_file_name);

        // Save email to a public external storage folder.
        Button savePublicExternalStorageButton = findViewById(R.id.external_storage_button_save_public);
        savePublicExternalStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (isExternalStorageMounted()) {
                        final String fileName = textFileName.getText().toString() + ".txt";

                        // Save file to documents
                        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();

                        File newFile = new File(dirPath, fileName);

                        FileWriter fileWriter = new FileWriter(newFile);

                        fileWriter.write(textContents.getText().toString());

                        fileWriter.close();

                        Toast.makeText(getApplicationContext(), "SUCCESS. . File Path: " + newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    Log.e(LOG_TAG_EXTERNAL_STORAGE, ex.getMessage(), ex);

                    Toast.makeText(getApplicationContext(), "FAIL. Error message: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Save email to a app private external storage folder.
        Button savePrivateExternalStorageButton = (Button) findViewById(R.id.external_storage_button_save_private);
        savePrivateExternalStorageButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                try {
                    if (isExternalStorageMounted()) {
                        final String fileName = textFileName.getText().toString() + ".txt";

                        // Check whether this app has write external storage permission or not.
                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(WriteToExternalStorage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(WriteToExternalStorage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        } else {

                            // Save email_private.txt file to /storage/emulated/0/Android/data/com.dev2qa.example/files folder
//                    String privateDirPath = getPrivateExternalStorageBaseDir(getApplicationContext(), null);
                            File privateDirPath = getPrivateFolderStorageDir(getApplicationContext(), null);

                            File newFile = new File(privateDirPath, fileName);

                            File temp = getTempFile(getApplicationContext(), "Text.txt");
                            FileWriter fileWriter = new FileWriter(temp);

                            fileWriter.write(textContents.getText().toString());

//                            fileWriter.flush();

                            fileWriter.close();

                            Toast.makeText(getApplicationContext(), "SUCCESS. . File Path: " + newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception ex) {
                    Log.e(LOG_TAG_EXTERNAL_STORAGE, ex.getMessage(), ex);

                    Toast.makeText(getApplicationContext(), "FAIL. Error message: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Save email to a app private cached external storage folder.
        Button savePrivateCachedExternalStorageButton = (Button) findViewById(R.id.external_storage_button_save_private_cache);
        savePrivateCachedExternalStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (isExternalStorageMounted()) {
                        final String fileName = textFileName.getText().toString() + ".txt";

                        // Check whether this app has write external storage permission or not.
                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(WriteToExternalStorage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(WriteToExternalStorage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        } else {

                            // Save email_private_cache.txt file to /storage/emulated/0/Android/data/com.dev2qa.example/cache folder
                            String privateDirPath = getPrivateCacheExternalStorageBaseDir(getApplicationContext());

                            File newFile = new File(privateDirPath, fileName);

                            FileWriter fileWriter = new FileWriter(getTempFile(getApplicationContext(), fileName));

                            fileWriter.write(textContents.getText().toString());

//                            fileWriter.flush();

                            fileWriter.close();

                            Toast.makeText(getApplicationContext(), "SUCCESS. File Path: " + newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception ex) {
                    Log.e(LOG_TAG_EXTERNAL_STORAGE, ex.getMessage(), ex);

                    Toast.makeText(getApplicationContext(), "FAIL. " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // Check whether the external storage is mounted or not.
    public static boolean isExternalStorageMounted() {

        String dirState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(dirState)) {
            return true;
        } else {
            return false;
        }
    }

    // Check whether the external storage is read only or not.
    public static boolean isExternalStorageReadOnly() {

        String dirState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(dirState)) {
            return true;
        } else {
            return false;
        }
    }

    // Get private external storage base directory.
    public static String getPrivateExternalStorageBaseDir(Context context, String dirType) {
        String ret = "";
        if (isExternalStorageMounted()) {
            File file = context.getExternalFilesDir(dirType);
            ret = file.getAbsolutePath();
        }
        return ret;
    }

    public File getPublicDocumentsStorageDir(String folderName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), folderName);

        return file;
    }


    public File getPrivateFolderStorageDir(Context context, String folderName) {
        // Get the directory for the app's private albumName (optional) directory.
        File file = context.getExternalFilesDir(folderName);

        return file;
    }

    // Get private cache external storage base directory.
    public static String getPrivateCacheExternalStorageBaseDir(Context context) {
        String ret = "";
        if (isExternalStorageMounted()) {
            File file = context.getExternalCacheDir();
            ret = file.getAbsolutePath();
        }
        return ret;
    }

    private File getTempFile(Context context, String url) {
        File file = null;
        try {
//            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(url, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }

    // Get public external storage base directory.
    public static String getPublicExternalStorageBaseDir() {
        String ret = "";
        if (isExternalStorageMounted()) {
            File file = Environment.getExternalStorageDirectory();
            ret = file.getAbsolutePath();
        }
        return ret;
    }

    // Get public external storage base directory.
    public static String getPublicExternalStorageBaseDir(String dirType) {
        String ret = "";
        if (isExternalStorageMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(dirType);
            ret = file.getAbsolutePath();
        }
        return ret;
    }

    // Get external storage disk space, return MB
    public static long getExternalStorageSpace() {
        long ret = 0;
        if (isExternalStorageMounted()) {
            StatFs fileState = new StatFs(getPublicExternalStorageBaseDir());

            // Get total block count.
            long count = fileState.getBlockCountLong();

            // Get each block size.
            long size = fileState.getBlockSizeLong();

            // Calculate total space size
            ret = count * size / 1024 / 1024;
        }
        return ret;
    }

    // Get external storage left free disk space, return MB
    public static long getExternalStorageLeftSpace() {
        long ret = 0;
        if (isExternalStorageMounted()) {
            StatFs fileState = new StatFs(getPublicExternalStorageBaseDir());

            // Get free block count.
            long count = fileState.getFreeBlocksLong();

            // Get each block size.
            long size = fileState.getBlockSizeLong();

            // Calculate free space size
            ret = count * size / 1024 / 1024;
        }
        return ret;
    }

    // Get external storage available disk space, return MB
    public static long getExternalStorageAvailableSpace() {
        long ret = 0;
        if (isExternalStorageMounted()) {
            StatFs fileState = new StatFs(getPublicExternalStorageBaseDir());

            // Get available block count.
            long count = fileState.getAvailableBlocksLong();

            // Get each block size.
            long size = fileState.getBlockSizeLong();

            // Calculate available space size
            ret = count * size / 1024 / 1024;
        }
        return ret;
    }
}

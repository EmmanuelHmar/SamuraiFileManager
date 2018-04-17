package com.androidsamurai.samuraifilemanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_CODE = 12345;
    private AbsListView filesView;
    private FileAdapter filesAdapter;
    private File currentFolder;
    private File defaultFolder;
    private static final String TAG = "MainActivity";

    private Animation fadeOut;
    private AnimationSet animationIn;

    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        filesView = findViewById(R.id.files_view);

//        Check to see if read/write permission is granted to app
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
//            Permission not granted
            Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
//            Request the app for permissions
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CODE);
        }

        setDefaultPath();
        currentFolder = new File(defaultFolder.getAbsolutePath());
        filesAdapter = new FileAdapter(currentFolder, currentFolder.getAbsolutePath()
                .equals(defaultFolder.getAbsolutePath()), MainActivity.this);

        setActionMode();

        setAnimations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDefaultPath();
        refreshFiles();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsetActionMode();
    }

    // Result of the permission request call back
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
//                If request is cancelled, the result arrays are empty
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Read/Write permission has been granted", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Read/Write permission has NOT been granted", Toast.LENGTH_SHORT).show();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    // Overflow menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, WriteToExternalStorage.class);
                Log.i(TAG, "Directing to Settings activity..");
                startActivity(intent);
                return true;
            case R.id.action_settings:
//                Intent intent = new Intent(MainActivity.this, com.androidsamurai.samuraifilemanager.SettingsActivity.class);
                intent = new Intent(MainActivity.this, WriteToExternalStorage.class);
                Log.i(TAG, "Directing to Settings activity..");
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                refreshFiles();
                Toast.makeText(this, "Files refreshed..", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Activity initialization

    /**
     * Inits default path from shared preferences
     */
    private void setDefaultPath() {
        String internalStoragePath = getObbDir().getParentFile().getParentFile().getParentFile()
                .getAbsolutePath();

        defaultFolder = new File(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString(
                getResources().getString(R.string.default_folder_key), internalStoragePath));
    }

    /**
     * Inits animations of files ListView/GridView during refreshing
     */
    private void setAnimations() {
        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(50);
        fadeOut.setFillAfter(true);

        Animation scaleIn = new ScaleAnimation(0.95f, 1f, 0.95f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleIn.setStartOffset(50);
        scaleIn.setDuration(150);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setStartOffset(50);
        fadeIn.setDuration(100);
        fadeIn.setFillAfter(true);

        animationIn = new AnimationSet(true);
        animationIn.addAnimation(scaleIn);
        animationIn.addAnimation(fadeIn);
    }

    /**
     * Inits on item clicks from files ListView/GridView, inits Action mode
     */
    private void setActionMode() {
        filesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode != null)
                    listItemSelect(position);
                else {
                    File item = (File) filesAdapter.getItem(position);
                    if (item == null)
                        return;
                    if (item.isDirectory())
                        setCurrentFolder(new File(currentFolder.getAbsolutePath() + File.separator +
                                item.getName()));
                    else {
                        if (Build.VERSION.SDK_INT >= 24) {
                            try {
                                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                m.invoke(null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Uri uri = Uri.fromFile(item);
//                        Uri uri = FileProvider.getUriForFile(MainActivity.this,
//                                getBaseContext().getApplicationContext().getPackageName() + ".my.package.name.provider",
//                                item);
                        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, mime);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Log.i(TAG, "Opening file: " + item.getPath());
                        startActivity(intent);
                    }
                }
            }
        });
        filesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listItemSelect(position);
                return true;
            }
        });
    }

    // Files list changing

    /**
     * Refreshes files ListView/GridView
     */
    void refreshFiles() {
        unsetActionMode();
        new Runnable() {
            @Override
            public void run() {
                filesView.startAnimation(fadeOut);

                filesAdapter = new FileAdapter(currentFolder, currentFolder.getAbsolutePath()
                        .equals(defaultFolder.getAbsolutePath()), MainActivity.this);
                filesView.setAdapter(filesAdapter);

                filesView.startAnimation(animationIn);
            }
        }.run();
        Log.i(TAG, "Files refreshed");
    }

    /**
     * Sets current folder to entered
     *
     * @param folder folder to be current
     */
    void setCurrentFolder(File folder) {
        this.currentFolder = folder;
        Log.i(TAG, "Current folder: " + folder.getAbsolutePath());
        refreshFiles();
    }

    /**
     * Sets deafult folder as current
     */
    void setDefaultFolder() {
        this.currentFolder = new File(defaultFolder.getAbsolutePath());
        Log.i(TAG, "Current folder: " + defaultFolder.getAbsolutePath());
        refreshFiles();
    }


    // Methods for Action mode (CAB)

    /**
     * Called when Action mode is on or is to be set on
     *
     * @param position clicked file position in list
     */
    private void listItemSelect(int position) {
        filesAdapter.itemSelect(position);
        boolean hasCheckedItems = filesAdapter.getSelectedCount() > 0;
        if (hasCheckedItems && actionMode == null)
            actionMode = startSupportActionMode(new com.androidsamurai.samuraifilemanager.ActionModeCallback(this, filesAdapter,
                    currentFolder.getAbsolutePath()));
        else if (!hasCheckedItems && actionMode != null)
            actionMode.finish();

        if (actionMode != null)
            actionMode.setTitle(filesAdapter.getSelectedCount() + " selected items");
    }

    /**
     * Disbales Action mode
     */
    void unsetActionMode() {
        filesAdapter.removeSelection();
        if (actionMode != null)
            actionMode = null;
    }

    /**
     * Returns Action mode object
     *
     * @return action mode
     */
    ActionMode getActionMode() {
        return actionMode;
    }
}

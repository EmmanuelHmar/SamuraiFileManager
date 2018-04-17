//package com.androidsamurai.samuraifilemanager;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.StrictMode;
//import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.view.ActionMode;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.ScaleAnimation;
//import android.webkit.MimeTypeMap;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import java.io.File;
//import java.lang.reflect.Method;
//import java.util.List;
//
//public class Main extends AppCompatActivity {
//    private AbsListView filesView;
//    private String root = Environment.getExternalStorageDirectory().getPath();
//    private List<String> item, path, files, filesPath;
//    private File[] filesArray;
//    private String curDir;
//    private FileAdapter filesAdapter;
//    private ListView rootList;
//    private File currentFolder, defaultFolder;
//    private static final String TAG = "MainActivity";
//    private ActionMode actionMode;
//    private Animation fadeOut;
//    private AnimationSet animationIn;
//
//    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        filesView = findViewById(R.id.files_view);
//
////        Check to see if read/write permission is granted to app
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
////            Permission not granted
//            Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
//
////            Request the app for permissions
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_CODE);
//        }
//
////        System.out.println(isExternalStorageReadable());
////        System.out.println("Next: " + isExternalStorageWritable());
////        String filename = "samurai";
////
////        File directory = getBaseContext().getFilesDir();
////        File file = new File(this.getFilesDir(), filename);
//
//        setDefaultPath();
//        currentFolder = new File(defaultFolder.getAbsolutePath());
//        filesAdapter = new FileAdapter(currentFolder, currentFolder.getAbsolutePath()
//                .equals(defaultFolder.getAbsolutePath()), Main.this);
//
////        rootList = findViewById(R.id.files_view);
//
//        setActionMode();
//
//        setAnimations();
//    }
//
//    /**
//     * Inits animations of files ListView/GridView during refreshing
//     */
//    private void setAnimations() {
//        fadeOut = new AlphaAnimation(1, 0);
//        fadeOut.setDuration(50);
//        fadeOut.setFillAfter(true);
//
//        Animation scaleIn = new ScaleAnimation(0.95f, 1f, 0.95f, 1f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleIn.setStartOffset(50);
//        scaleIn.setDuration(150);
//        Animation fadeIn = new AlphaAnimation(0, 1);
//        fadeIn.setStartOffset(50);
//        fadeIn.setDuration(100);
//        fadeIn.setFillAfter(true);
//
//        animationIn = new AnimationSet(true);
//        animationIn.addAnimation(scaleIn);
//        animationIn.addAnimation(fadeIn);
//    }
//
//    public boolean isExternalStorageWritable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean isExternalStorageReadable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
//            return true;
//        }
//        return false;
//    }
//
//    // Result of the permission request call back
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CODE: {
////                If request is cancelled, the result arrays are empty
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    Toast.makeText(this, "Read/Write permission has been granted", Toast.LENGTH_SHORT).show();
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(this, "Read/Write permission has NOT been granted", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//            // other 'case' lines to check for other
//            // permissions this app might request.
//        }
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    private void setDefaultPath() {
//        String internalStoragePath = getObbDir().getParentFile().getParentFile()
//                .getParentFile().getAbsolutePath();
//
//        defaultFolder = new File(PreferenceManager.getDefaultSharedPreferences(this)
//                .getString(getResources().getString(R.string.default_folder), internalStoragePath));
//    }
//
//    private void setActionMode() {
//        filesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (actionMode != null)
//                    listItemSelect(position);
//                else {
//                    File item = (File) filesAdapter.getItem(position);
//                    if (item == null)
//                        return;
//                    if (item.isDirectory())
//                        setCurrentFolder(new File(currentFolder.getAbsolutePath() + File.separator +
//                                item.getName()));
//                    else {
//                        if (Build.VERSION.SDK_INT >= 24) {
//                            try {
//                                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
//                                m.invoke(null);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Uri uri = Uri.fromFile(item);
////                        Uri uri = FileProvider.getUriForFile(MainActivity.this,
////                                getBaseContext().getApplicationContext().getPackageName() + ".my.package.name.provider",
////                                item);
//                        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
//                                MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setDataAndType(uri, mime);
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        Log.i(TAG, "Opening file: " + item.getPath());
//                        startActivity(intent);
//                    }
//                }
//            }
//        });
//        filesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                listItemSelect(position);
//                return true;
//            }
//        });
//    }
//
////    public void getDirFromRoot(String rootPath) {
////        item = new ArrayList<>();
////        Boolean isRoot = true;
////        path = new ArrayList<>();
////        files = new ArrayList<>();
////        filesPath = new ArrayList<>();
////        File m_file = new File(rootPath);
////        File[] filesArray = m_file.listFiles();
////
////        if (!rootPath.equals(root)) {
////            item.add("../");
////            path.add(m_file.getParent());
////            isRoot = false;
////        }
////
////        curDir = rootPath;
////
//////        sorting file list in alpha order
////        Arrays.sort(filesArray);
////
////        for (int i=0; i<filesArray.length;i++) {
////            File file = filesArray[1];
////            if (file.isDirectory()) {
////                item.add(file.getName());
////                item.add(file.getPath());
////            } else {
////                files.add(file.getName());
////                filesPath.add(file.getPath());
////            }
////        }
////
//////        Instead of enhanced for loop
////        item.addAll(files);
////
////        path.addAll(filesPath);
////
////        filesAdapter = new FileAdapter(this, item, path, isRoot);
////
////        rootList.setAdapter(filesAdapter);
////        rootList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
////                File isFile = new File(path.get(position));
////                if (isFile.isDirectory()) {
////                    getDirFromRoot(isFile.toString());
////                } else {
////                    Toast.makeText(MainActivity.this, "This is file", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////
////    }
//
//    void setDefaultFolder() {
//        this.currentFolder = new File(defaultFolder.getAbsolutePath());
//        Log.i(TAG, "Current folder: " + defaultFolder.getAbsolutePath());
////        refreshFiles();
//    }
//
//    void setCurrentFolder(File folder) {
//        this.currentFolder = folder;
//        Log.i(TAG, "Current folder: " + folder.getAbsolutePath());
////        refreshFiles();
//    }
//
//    // Methods for Action mode (CAB)
//
//    /**
//     * Called when Action mode is on or is to be set on
//     *
//     * @param position clicked file position in list
//     */
//    private void listItemSelect(int position) {
//        filesAdapter.itemSelect(position);
//        boolean hasCheckedItems = filesAdapter.getSelectedCount() > 0;
//        if (hasCheckedItems && actionMode == null)
//            actionMode = startSupportActionMode(new com.androidsamurai.samuraifilemanager
//.ActionModeCallback(this, filesAdapter,
//                    currentFolder.getAbsolutePath()));
//        else if (!hasCheckedItems && actionMode != null)
//            actionMode.finish();
//
//        if (actionMode != null)
//            actionMode.setTitle(filesAdapter.getSelectedCount() + " selected items");
//    }
//
//    // Files list changing
//
//    /**
//     * Refreshes files ListView/GridView
//     */
//    void refreshFiles() {
//        unsetActionMode();
//        new Runnable() {
//            @Override
//            public void run() {
//                filesView.startAnimation(fadeOut);
//
//                filesAdapter = new FileAdapter(currentFolder, currentFolder.getAbsolutePath()
//                        .equals(defaultFolder.getAbsolutePath()), MainActivity.this);
//                filesView.setAdapter(filesAdapter);
//
//                filesView.startAnimation(animationIn);
//            }
//        }.run();
//        Log.i(TAG, "Files refreshed");
//    }
//
//    /**
//     * Disbales Action mode
//     */
//    void unsetActionMode() {
//        filesAdapter.removeSelection();
//        if (actionMode != null)
//            actionMode = null;
//    }
//
//    /**
//     * Returns Action mode object
//     *
//     * @return action mode
//     */
//    ActionMode getActionMode() {
//        return actionMode;
//    }
//}

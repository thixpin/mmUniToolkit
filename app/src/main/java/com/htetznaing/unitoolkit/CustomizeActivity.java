package com.htetznaing.unitoolkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.htetznaing.unitoolkit.Utils.AIOmmTool;
import com.htetznaing.unitoolkit.Utils.Toolkit;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import mabbas007.tagsedittext.TagsEditText;

public class CustomizeActivity extends AppCompatActivity {
    TextView textView;
    TagsEditText tagsEditText;
    final int FILE_CODE = 1;
    ArrayList<String> selected_files = new ArrayList<>();
    Switch changeFolderName;
    ProgressDialog progressDialog;
    LinearLayout rootLayout;
    public static CustomizeActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        instance = this;
        if (getActionBar()!=null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        tagsEditText = findViewById(R.id.tagsEditText);
        textView = findViewById(R.id.tv_paths);
        changeFolderName = findViewById(R.id.changeFolderName);
        rootLayout = findViewById(R.id.rootLayout);
        String checkText = textView.getText().toString();
        if (checkText.length()>1){
            textView.setText("");
        }

        for (String s:getStorage()){
            textView.append(s+"\n");
        }
    }

    public void chooseFileOrFolder(View view) {
        // This always works
        Intent i = new Intent(this, FilePickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE_AND_DIR);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(i, FILE_CODE);
    }

    private ArrayList<String> getStorage() {
        ArrayList<String> path = new ArrayList<>();
        String [] a = Toolkit.getExternalStorageDirectories(getApplicationContext());
        if (a!=null && a.length>0){
            for (String p:a){
                if (p!=null && !p.isEmpty()){
                    path.add(p);
                }
            }
        }
        path.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            selected_files.clear();
            // Use the provided utility method to parse the result
            List<Uri> selected = Utils.getSelectedFilesFromResult(intent);
            String checkText = textView.getText().toString();
            if (checkText != null && checkText.length() > 1) {
                textView.setText("");
            }

            for (Uri uri : selected) {
                File file = Utils.getFileForUri(uri);
                if (file!=null) {
                    selected_files.add(file.toString());
                    textView.append(file.toString() + "\n");
                }
            }
        }
    }

    public void changeNow(View view) {
        if (selected_files==null || selected_files.size()<1){
            selected_files = getStorage();
        }
        alert();
    }

    private void alert(){
        String title = "သတိပေးချက်";
        String message = "ရွေးချယ်ထားသောဖိုင်များအားလုံးကို\nယူနီကုဒ်အမည်ဖြင့်ပြောင်းလဲပေးမည်။";

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("ဆက်လုပ်ပါ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkPermissions()){
                            next();
                        }
                    }
                })
                .setNegativeButton("မလုပ်ပါ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    private void next(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("သတိပြုရန်!!")
                .setMessage("သင်ပြောင်းမည့်အရာသည် အားလုံးဇော်ဂျီဖြစ်နေရင်\n" +
                        "* မဖြစ်မနေပြောင်းမည် * ကိုရွေးချယ်ပါ။\n" +
                        "ဇော်ဂျီနှင့်ယူနီကုဒ်ရောထွေးနေပါက * အလိုအလျောက် * ကိုရွေးချယ်ပါ။\n" +
                        "အဘယ်ကြောင့်ဆိုသော် ယူနီကုဒ်ဖြင့်ရေးထားသည့်စာများပါဝင်နေပါက\n" +
                        "ယူနီကုဒ်သို့မဖြစ်မနေပြောင်းသောအခါ စာများလုံးဝလွဲသွားမည်ဖြစ်သည်။")
                .setPositiveButton("အလိုအလျောက်", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeLOL(selected_files,false);
                    }
                })
                .setNegativeButton("မဖြစ်မနေပြောင်းမည်", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeLOL(selected_files,true);
                    }
                })
                .setNeutralButton("မလုပ်တော့ပါ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }


    private void changeLOL(final ArrayList<String> path, final boolean force){
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toolkit.resetPathsAndMimeType();
                progressDialog.setMessage("Converting...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                String extension = tagsEditText.getText().toString();
                    for (String p:path){
                        System.out.println("Change Folder: "+changeFolderName.isChecked());
                        if (changeFolderName.isChecked()){
                            File oldFile = new File(p);
                            if (oldFile.isDirectory()){
                                File newFile = new File(oldFile.getParentFile()+"/"+ AIOmmTool.getUnicode(oldFile.getName(),force));
                                if (oldFile.renameTo(newFile)){
                                    p = newFile.toString();
                                }
                            }
                        }
                        Toolkit.changeFileNameCustomExtension(new File(p),extension,changeFolderName.isChecked(),force);
                    }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                selected_files.clear();
                textView.setText("");
                progressDialog.dismiss();
                done(false);
            }
        }.execute();
    }

    public void done(final boolean done){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (done){
                    Snackbar.make(rootLayout, "ပြီးပါပြီ", Snackbar.LENGTH_LONG)
                            .setAction("ဟုတ်ပြီ", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(instance)
                            .setTitle("အားလုံးပြောင်းပြီးပါပြီ။")
                            .setMessage("အချို့ Player နှင့် Gallery များတွင်ပြောင်းထားတာမြင်နိုင်ရင်\n" +
                                    "Media ဖိုင်များ Update လုပ်ပေးရန်လိုအပ်ပါသည်။\n" +
                                    "သို့မဟုတ်ဖုန်းကို Restart(ပိတ်ပြီးပြန်ဖွင့်) လုပ်၍လည်းရပါသည်။\n" +
                                    "အကောင်းဆုံးကတော့ဖုန်းကို Restart လုပ်လိုက်ပါ။\n" +
                                    "သို့မဟုတ် အောက်ပါ Normal Update နဲ့လုပ်လိုက်ပါ။\n" +
                                    "\n" +
                                    "Force Update ကတော့\n" +
                                    "ပြောင်းခဲ့သမျှဖိုင်များအားလုံးကို Media အမည် Scan ပေးသွားမည်ဖြစ်သည်။\n" +
                                    "ထိုကြောင့်တချို့ Hidden ဖိုင်များလည်း Media အဖြစ် Update လုပ်ပေးသောကြောင့်\n" +
                                    "မလိုအပ်လျှင်မသုံးပါနှင့်!\n")
                            .setPositiveButton("Normal Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toolkit.scanMediaNormal(instance);
                                }
                            })
                            .setNegativeButton("Force Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toolkit.updateMEDIA(instance);
                                }
                            })
                            .setNeutralButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                }
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100) {
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                next();
            } else {
                checkPermissions();
                Toast.makeText(this, "You need to allow this permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        final List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}

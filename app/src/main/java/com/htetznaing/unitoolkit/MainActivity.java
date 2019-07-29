package com.htetznaing.unitoolkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.material.snackbar.Snackbar;
import com.htetznaing.app_updater.AppUpdater;
import com.htetznaing.unitoolkit.Ads.Banner;
import com.htetznaing.unitoolkit.Ads.Interstitial;
import com.htetznaing.unitoolkit.Utils.CheckInternet;
import com.htetznaing.unitoolkit.Utils.Toolkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    int WHAT=0;
    final int ALL=1,VIDEO=2,AUDIO=3,IMAGE=4,CONTACTS=5;
    LinearLayout rootLayout;
    SharedPreferences sharedPreferences;
    String zgORuni = null;
    public static MainActivity instance;

    LinearLayout adLayout;
    Interstitial interstitial;

    //Update
    AppUpdater appUpdater;
    CheckInternet checkInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        rootLayout = findViewById(R.id.rootLayout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        appUpdater = new AppUpdater(this,"https://myappupdateserver.blogspot.com/2019/07/mmunicodetookit.html");
        checkInternet = new CheckInternet(this);
        adLayout = findViewById(R.id.adLayout);
        new Banner(this,adLayout, AdSize.SMART_BANNER);
        interstitial = new Interstitial(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDefault();
    }

    public void changeAll(View view) {
        WHAT = ALL;
        alert(ALL);
    }

    private boolean checkPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_contact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int write_contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);

        final List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (read_contact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (write_contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }

        return true;
    }

    private void changeAll() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toolkit.resetPathsAndMimeType();
                progressDialog.setMessage("အားလုံး...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<String > path = getStorage();
                for (String p:path){
                    Toolkit.changeFileNameToUnicode(new File(p));
                }
                Toolkit.changeContacts(MainActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                done(false);
            }
        }.execute();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100) {
            if (grantResults.length > 0) {
                boolean how = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        how = false;
                        Toast.makeText(this, "You need to allow this permission!", Toast.LENGTH_SHORT).show();
                        checkPermissions();
                        break;
                    }
                }

                System.out.println(how+" : "+WHAT);
                if (how){
                    switch (WHAT){
                        case ALL:changeAll();break;
                        case AUDIO:changeAudio();break;
                        case VIDEO:changeVideo();break;
                        case IMAGE:changeImage();break;
                        case CONTACTS:changeContacts();break;
                    }
                }
            } else {
                checkPermissions();
                Toast.makeText(this, "You need to allow this permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void changeAudio(View view) {
        WHAT = AUDIO;
        alert(AUDIO);
    }

    private void changeAudio() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toolkit.resetPathsAndMimeType();
                progressDialog.setMessage("အသံဖိုင်များ...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<String > path = getStorage();
                for (String p:path){
                    Toolkit.audioFileNameToUnicode(new File(p));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                done(false);
            }
        }.execute();
    }

    public void changeVideo(View view) {
        WHAT = VIDEO;
        alert(VIDEO);
    }

    private void changeVideo() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toolkit.resetPathsAndMimeType();
                progressDialog.setMessage("ဗီဒီယိုဖိုင်များ...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<String > path = getStorage();
                for (String p:path){
                    Toolkit.videoFileNameToUnicode(new File(p));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                done(false);
            }
        }.execute();
    }

    public void changeImage(View view) {
        WHAT = IMAGE;
        alert(IMAGE);
    }

    private void changeImage() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toolkit.resetPathsAndMimeType();
                progressDialog.setMessage("ဓာတ်ပုံများ...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<String > path = getStorage();
                for (String p:path){
                    Toolkit.imageFileNameToUnicode(new File(p));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                done(false);
            }
        }.execute();
    }

    public void changeContacts(View view) {
        WHAT = CONTACTS;
        alert(CONTACTS);
    }

    private void changeContacts(){
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("အဆက်အသွယ်များ...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Toolkit.changeContacts(MainActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                done(true);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("အသိပေးချက်")
                        .setMessage(Html.fromHtml("<p>သင့်ဖုန်းအတွင်းရှိဇော်ဂျီဖြင့်ရေးသားထားသမျှကို<br />ယူနီကုဒ်သို့ပြောင်းရွေ့ရန်ကူညီပေးနိုင်သော<br />Application ဖြစ်ပါသည်။</p>\n" +
                                "<p><span style=\"color: #ff0000;\"><strong>အားလုံး</strong></span><br />သင့်ဖုန်းအတွင်းရှိသမျှ ဇော်ဂျီနဲ့ရေးထားတာတွေကို<br />အကုန်ယူနီကုဒ်နာမည်နဲ့ပြောင်းပေးသွားမှာပါ။<br />သီချင်း၊ ဗီဒီယို၊ ဓာတ်ပုံ၊ ဖုန်းအဆက်အသွယ်နှင့်<br />အခြားဖုန်းအတွင်းရှိဖိုင်အားလုံးကိုတစ်ချက်တည်းနဲ့ပြောင်းပေးသွားမှာပါ။</p>\n" +
                                "<p><span style=\"color: #ff0000;\"><strong>အသံဖိုင်များ</strong></span><br />အသံဖိုင် (Mp3, m4a, wav, etc..) အားလုံးကို<br />ယူနီကုဒ်နာမည်နဲ့ပြောင်းပေးသွားမှာပါ။<br />အဆိုတော်၊ သီချင်း၊ အယ်လ်ဘမ်အမည်အားလုံးကိုပြောင်းပေးမှာပါ။</p>\n" +
                                "<p><strong><span style=\"color: #ff0000;\">ဗီဒီယိုဖိုင်များ</span></strong><br />Video ဖိုင်အားလုံး (Mp4, 3pg, etc..) အားလုံးကို<br />ယူနီကုဒ်နာမည်နဲ့ပြောင်းပေးသွားမှာပါ။</p>\n" +
                                "<p><span style=\"color: #ff0000;\"><strong>ဓာတ်ပုံများ</strong></span><br />ဓာတ်ပုံ (jpg, png, etc...) အားလုံးကို<br />ယူနီကုဒ်နာမည်နဲ့ပြောင်းပေးသွားမှာပါ။</p>\n" +
                                "<p><strong><span style=\"color: #ff0000;\">ဖုန်းအဆက်အသွယ်များ</span></strong><br />သင့်ဖုန်း Contacts အတွင်းရှိ<br />ဇော်ဂျီဖြင့်ရေးသားထားသောအမည်များအားလုံးကို<br />ယူနီကုဒ်ဖြင့်ပြောင်းပေးသွားမည်။</p>\n" +
                                "<p><strong><span style=\"color: #ff0000;\">Bonus</span></strong><br />အပိုအနေနဲ့ယူနီကုဒ်ထည့်သွင်းရန်လိုအပ်သော<br />zFont - Custom Font Installer နဲ့<br />စာရိုက်ရန်လိုအပ်သော Keyboard တို့လိုလည်းလင့်ခ်ချိတ်ပေးထားပါတယ်။<br />zFont ကတော့စိတ်ကြိုက်ဖောင့်အလှမျိုးစုံကို<br />ဖုန်းတော်တော်များများမှာ အလွယ်တကူပြောင်းပေးနိုင်ပါတယ်။<br />ကီးဘုတ်ကတော့ကိုယ်ကြိုက်တာအသုံးပြုနိုင်အောင်စုပေးထားတာပါ။</p>\n" +
                                "<p>ခွန်ထက်နိုင်<br />(ဇူလိုင်လ ၂၈ ရက်၊ ၂၀၁၉)</p>"))
                        .setPositiveButton("ဟုတ်ပြီ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();break;
            case R.id.settings:
                startActivity(new Intent(this,SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void alert(final int WHATz){
        String title = "သတိပေးချက်";
        String message = "";

        switch (WHATz){
            case ALL:message="ဖိုင်";break;
            case AUDIO:message="အသံဖိုင်";break;
            case VIDEO:message="ဗီဒီယိုဖိုင်";break;
            case IMAGE:message="ဓာတ်ပုံဖိုင်";break;
            case CONTACTS:message="အဆက်အသွယ်";break;
        }

        if (zgORuni==null) {
            message += "များအားလုံးကို\nယူနီကုဒ်အမည်ဖြင့်ပြောင်းလဲပေးမည်။";
        }else if (zgORuni.equalsIgnoreCase("zawgyi")){
            message += "များအားလုံးကို\nဇော်ဂျီအမည်ဖြင့်ပြောင်းလဲပေးမည်။";
        }else if (zgORuni.equalsIgnoreCase("unicode")){
            message += "များအားလုံးကို\nယူနီကုဒ်အမည်ဖြင့်ပြောင်းလဲပေးမည်။";
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("ဆက်လုပ်ပါ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkPermissions()){
                            switch (WHATz){
                                case ALL:changeAll();break;
                                case AUDIO:changeAudio();break;
                                case VIDEO:changeVideo();break;
                                case IMAGE:changeImage();break;
                                case CONTACTS:changeContacts();break;
                            }
                        }
                    }
                })
                .setNegativeButton("မလုပ်ပါ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        interstitial.show();
                    }
                });
        builder.show();
    }


    public void done(final boolean contacts){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (contacts) {
                    interstitial.show();
                    Snackbar.make(rootLayout, "ပြီးပါပြီ", Snackbar.LENGTH_LONG)
                            .setAction("ဟုတ်ပြီ", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    interstitial.show();
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
                                    interstitial.show();
                                }
                            });
                    builder.show();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public void frozen(View view) {
        openPlayStore("ninja.thiha.frozenkeyboard2");
    }

    public void m(View view) {
        openPlayStore("com.myatminsoe.mkeyboardlite");
    }

    public void mua(View view) {
        openPlayStore("com.sanlin.mkeyboard");
    }

    public void tt(View view) {
        openPlayStore("com.myopenware.ttkeyboard.latin");
    }

    public void bagan(View view) {
        openPlayStore("com.bit.androsmart.kbinapp");
    }

    public void manic(View view) {
        openPlayStore("com.lmkhant.android.manickeyboard");
    }

    public void k(View view) {
        openPlayStore("mm.kst.keyboard.myanmar");
    }

    public void zfont(View view) {
        openPlayStore("com.mgngoe.zfont");
    }


    private void openPlayStore(String appPackageName){
        try {
            startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)),100);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)),100);
        }
    }

    private void openFb(String userId){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse("fb://profile/"+userId));
            startActivityForResult(intent,100);
        } catch (Exception e) {
            intent.setData(Uri.parse("https://m.facebook.com/"+userId));
            startActivityForResult(intent,100);
        }
    }

    public void dev(View view) {
        openFb("100030031876000");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            interstitial.show();
        }
    }

    public void customize(View view) {
        startActivity(new Intent(this,CustomizeActivity.class));
    }


    private void setDefault(){
        zgORuni = sharedPreferences.getString(Constants.CONVERT_TO_KEY,null);
        boolean change_hidden = sharedPreferences.getBoolean(Constants.CHANGE_HIDDEN_KEY,false);

        if (zgORuni==null) {
            sharedPreferences.edit().putString(Constants.CONVERT_TO_KEY, "unicode").apply();
        }
        Constants.setCHANGING(zgORuni);
        Constants.setChangeHidden(change_hidden);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("အသိပေးချက်")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("ထွက်တော့မှာလား ?")
                .setPositiveButton("ဟုတ်", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("မထွက်သေးပါ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        interstitial.show();
                    }
                });
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkInternet.isInternetOn()){
            appUpdater.check(false);
        }
    }
}

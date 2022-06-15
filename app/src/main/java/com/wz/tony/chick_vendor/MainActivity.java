package com.wz.tony.chick_vendor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wz.tony.chick_vendor.Conn.AnalysisJson;
import com.wz.tony.chick_vendor.Conn.ConnServ;
import com.wz.tony.chick_vendor.DataInfo.BarcodeData;
import com.wz.tony.chick_vendor.DataInfo.ConvertData;
import com.wz.tony.chick_vendor.DataInfo.DailyRecordData;
import com.wz.tony.chick_vendor.DataInfo.FragmentData;
import com.wz.tony.chick_vendor.DataInfo.RecordData;
import com.wz.tony.chick_vendor.Fragment.CheckFragment;
import com.wz.tony.chick_vendor.DataInfo.AppData;
import com.wz.tony.chick_vendor.DataInfo.LoginData;
import com.wz.tony.chick_vendor.Public.Manager;
import com.wz.tony.chick_vendor.Ui.RefreshScroll.PullToRefreshLayout;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.wz.tony.chick_vendor.R.*;


//public class MainActivity extends Activity {
public class MainActivity extends AppCompatActivity {

    //alert
    public Handler handler;

    //Login ui
    //Button loginBtn;
    ImageView loginBtn;
    EditText username;
    EditText password;

    final int EXCHANE = 100;
    final int RECOARD = 101;


    int viewState =  EXCHANE;
    private IntentIntegrator scanIntegrator;



    //  ==== 登入 ====
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        handler = new Handler(this.getMainLooper());
        setContentView(layout.login);

        //hideButtonUi();

        loginBtn = (ImageView) findViewById(id.loginBtn);
        username = (EditText)findViewById(id.username);
        password = (EditText)findViewById(id.password);

        TextView urlSettingBtn = (TextView)findViewById(id.urlSettingTv);


        urlSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e( "tony", "urlSettingBtn" );

                showSettingSetting();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tony", "click");

                String userStr = username.getText().toString().trim();
                String passStr = password.getText().toString().trim();

                if(userStr.isEmpty() || passStr.isEmpty()){
                    Manager.getInst().myToast(getResources().getString(string.login_errors));
                    return;
                }

                LoginData loginData= new LoginData( userStr, passStr);
                String loginUrl = AppData.URl_LOGIN;

                setConnState(loginUrl);
                new ConnServ(loginUrl, loginData).start();
                //connServPost(AppData.URl_LOGIN, loginData);
                alert( getResources().getString(string.http_conn_login));
            }
        });

        SharedPreferences setting = getSharedPreferences("login", MODE_PRIVATE);
        username.setText(setting.getString("username", ""));
        password.setText(setting.getString("password", ""));

        Manager.getInst().setContext(this);
        //showSingleExchange();

    }



    // Main ui
    //Button statBarcoade;
    ImageView statBarcoade;
    Button okBtn;
    Button refrashBtn;


    CheckFragment checkFm;

    TextView vendorName;

    TextView barcode;
    TextView convertFood;

    //ScrollView foodScroll;
    PullToRefreshLayout foodScroll;
    LinearLayout foodInfoList;

    //setting menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    public void setConvertFood(String message){
        Log.e("tony", "setConvertFood =" + message);
        convertFood.setText(message);
    }

    public void setBarcodeLb(String message){
        barcode.setText(message);
    }


    //private void setBarcode
    private void clearTextVeiw(){
        //barcode.setText();
        setBarcodeLb(getResources().getString(string.QR_start));
        setConvertFood("");
    }

    // ===== 掃碼 =====
    //Main ui ------
    public void changeMain(){
        //setContentView(layout.activity_main);
        //setContentView(layout.activity_test);
        setContentView(layout.activity_main2);
        viewState =  EXCHANE;

        String voderUrl = AppData.URl_INFO;
        setConnState(voderUrl);
        new ConnServ(voderUrl).start();
        alert( getResources().getString(string.http_conn_data));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        vendorName = (TextView) findViewById(id.vendorName);

        foodScroll = (PullToRefreshLayout) findViewById(R.id.foodScroll);
        //foodScroll =(ScrollView) findViewById(id.foodScroll );
        foodInfoList = (LinearLayout) findViewById(id.foodInfoList);
        statBarcoade = (ImageView) findViewById(id.startBtn);
        okBtn = (Button) findViewById(id.okBtn);
        refrashBtn = (Button) findViewById( id.refrash );
        barcode = (TextView) findViewById(id.barcode);
        convertFood = (TextView) findViewById(id.convertFood);


        //set Title ---
        vendorName.setText(AppData.VENDORNAME);

        okBtn.setOnClickListener(clickBtn);
        statBarcoade.setOnClickListener(clickBtn);
        refrashBtn.setOnClickListener(clickBtn);

        navigationView.setNavigationItemSelectedListener(clickMenu);
        navigationView.setCheckedItem(id.m_exchange);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        AppData.itemImgCash = new HashMap<String, Bitmap>();
        Log.e( "tony", "Cash = " );
    }


    TextView dateTv, countTv;
    // ===== 兌換紀錄 =====
    public void changeRecoard(){
        Log.e( "tony", "changeRecoard");
        setContentView(layout.activity_recoard);
        viewState =  RECOARD;

        String recordUrl = AppData.URl_RECORD;
        setConnState(recordUrl);
        new ConnServ(recordUrl).start();


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        foodScroll = (PullToRefreshLayout) findViewById(R.id.foodScroll);
        foodInfoList = (LinearLayout) findViewById(id.foodInfoList);

        LinearLayout dailyRecordLy = (LinearLayout) this.findViewById(id.dailyRecordLy);
        dateTv = (TextView)this.findViewById(id.dateTv);
        countTv = (TextView)this.findViewById(id.countTv);
        vendorName = (TextView)this.findViewById(id.vendorName);



        vendorName.setText(AppData.VENDORNAME + " 兌換紀錄");
        dailyRecordLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyRecord();
            }
        });

        navigationView.setNavigationItemSelectedListener(clickMenu);
        navigationView.setCheckedItem(id.m_recoard);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //updateRecoardInfoList();
    }




    //側邊選單
    NavigationView.OnNavigationItemSelectedListener clickMenu = new NavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            drawerLayout.closeDrawer(GravityCompat.START);// 點選時收起選單
            int id = item.getItemId();// 取得選項id


            Log.e( "tony", "clicked id= "+ id );


            // 依照id判斷點了哪個項目並做相應事件
            if (id == R.id.m_refresh) {
                refrashAction();
                return true;


            }else if( id == R.id.m_exchange ){
                //兌換商品
                changeMain();
                return true;



            }else if( id == R.id.m_recoard ){
                //兌換紀錄
                changeRecoard();
                return true;


            }else if( id == R.id.m_logout ){
                logoutApp();
                return true;
            }

            return false;
        }
    };



    View.OnClickListener clickBtn = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Log.e("tony", "cccccccccccccc =" + view.getId());

            switch (view.getId()){
                case id.startBtn:
                    statBarcoadeAction();
                    break;

                case id.okBtn:
                    if(AppData.getTransKey() == null){
                        Manager.getInst().myToast(getResources().getString(string.convert_error));
                        return;
                    }
                   // convertFoodAction();
                    break;

                case id.refrash:
                    refrashAction();
                    break;
            }
        }
    };



    // click action ---------------------
    // 確定有 QR 直接兌換~


    public void convertFoodAction(){
        ConvertData convertData = new ConvertData( AppData.getToken(), AppData.getTransKey() );
        String convertUrl = AppData.URl_COVERT;
        setConnState(convertUrl);
        new ConnServ(convertUrl, convertData).start();
    }

    private void statBarcoadeAction(){
        Log.e( "tony" , " barcoard ") ;
        clearTextVeiw();
        scanIntegrator = new IntentIntegrator(MainActivity.this);
        scanIntegrator.setPrompt("請掃描");
        scanIntegrator.setTimeout(300000);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.initiateScan();
    }

    //刷新
    public void refrashAction(){
        String stateUrl = "";

        if( viewState == EXCHANE ){
            stateUrl = AppData.URl_INFO;
            setConnState(stateUrl);
            new ConnServ(stateUrl).start();

        }else if( viewState == RECOARD ){
            stateUrl = AppData.URl_RECORD;
            setConnState(stateUrl);
            new ConnServ(stateUrl).start();
        }

    }
    // click action end ---



    //CallBack function -------
    //resquest response data -------------------------------
    public void reqCallBack(int postCode, String resData) throws JSONException {
        String toastMsg = "";
        if(postCode != 200){
            toastMsg = getResources().getString(string.post_other_errors);
            Manager.getInst().myToast(toastMsg);
            return;
        }

        AnalysisJson analysisJson = new AnalysisJson(resData);

        Log.e( "tony", " ccccc resData =" + resData );
        if(AppData.getConnState() == AppData.URl_LOGIN){
            analysisJson.loginData();
            // save login data
            String userStr = username.getText().toString().trim();
            String passStr = password.getText().toString().trim();

            SharedPreferences setting = getSharedPreferences("login", MODE_PRIVATE);
            setting.edit().putString("username", userStr) .commit();
            setting.edit().putString("password", passStr) .commit();

        }else if(AppData.getConnState() == AppData.URl_INFO){
            // 刷新 列表
            analysisJson.voderData();
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateFoodInfoList();
                    foodScroll.finishRefresh(); //scroll 更新結束~
                }
            });

        }else if(AppData.getConnState() == AppData.URl_RECORD){
            analysisJson.recordData();
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateRecoardInfoList();
                    foodScroll.finishRefresh(); //scroll 更新結束~
                }
            });

            String dailyUrl = AppData.URl_DAILYRECORD;
            setConnState(dailyUrl);
            new ConnServ(dailyUrl).start();

        }else if(AppData.getConnState() == AppData.URl_DAILYRECORD){
            Log.e( "tony",   "URl_DAILYRECORD");
            final Map<String, Object> map = analysisJson.dailyRecordData();

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{

                        dateTv.setText( (String) map.get("date") );
                        countTv.setText( (Integer) map.get("total") +"" );

                    }catch ( Exception ex){

                        dateTv.setText( "");
                        countTv.setText( "--error--");

                    }

                }
            });



        }else if(AppData.getConnState() == AppData.URl_BARCODE){
            analysisJson.barcodeData();


        }else if( AppData.getConnState() == AppData.URl_COVERT ){
            // 兌換成功

            analysisJson.covertData();
            refrashAction(); //更新 雞排 數量
            //換ui線程  顯示成功toast
            //toastSuccess();
        }

        Manager.getInst().cancelAlertDialog();
    }
    //resquest end ---



    //掃QR後返回資料
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult( requestCode, resultCode, intent );
        if (scanningResult != null) {
            if(scanningResult.getContents() != null) {

                String scanContent = scanningResult.getContents();
                if (!scanContent.equals("")){

                    String QR = scanContent.toString();

                    if( QR.indexOf( "/") > -1 ||
                        QR.indexOf( ":") > -1 ||
                        QR.indexOf( ".") > -1 ||
                        QR.indexOf( ",") > -1
                            ){
                        //Toast.makeText( getApplicationContext(),getResources().getString(string.QR_errorData), Toast.LENGTH_LONG).show();
                        toastErrors( getResources().getString(string.QR_errorData) );

                    }else{
                        //barcode.setText(String.format(getResources().getString(string.main_barcode), QR));
                        String strMsg = String.format(getResources().getString(string.main_barcode), QR);
                        setBarcodeLb(strMsg);

                        //POST Server
                        BarcodeData barcodeData= new BarcodeData( AppData.getToken(), QR);
                        String barcodeUrl = AppData.URl_BARCODE;
                        setConnState(barcodeUrl);
                        new ConnServ(barcodeUrl, barcodeData).start();
                        Log.e("tony", "scanContent.toString() =" + scanContent.toString());
                    }

                }else{
                    toastErrors( getResources().getString(string.QR_again) );
                }
                // 掃QR有資料

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, intent);
            Toast.makeText(getApplicationContext(),getResources().getString(string.QR_error),Toast.LENGTH_LONG).show();
        }
    }




    private void setConnState(String url){
        AppData.clearConnState();
        AppData.setConnState(url);
    }


    private void alert( String msg){
        Manager.DialogSet ds = Manager.getInst().newDialogSet();
        AlertDialog alert = ds.getDialog();
        alert.setCancelable(false);
        alert.setMessage(msg);
        alert.show();
    }

    // 商品列表  update Food List -----  實體化項目UI
    @SuppressLint("NewApi")
    public void updateFoodInfoList(){

        try{
            if(AppData.fmList.size() <= 0) return;
            clearFoodInfoList();

            int allCount =  AppData.fmList.size();
            if( allCount > 20 ) allCount = 20; //先抓 20

            for( int i = 0; i < allCount; i ++ ){
                //for( int i = 0; i < 5; i ++ ){
                LayoutInflater inflater = LayoutInflater.from(this);
                View foodItem = inflater.inflate(layout.fragment_check, null);

                final ImageView projectImg = (ImageView) foodItem.findViewById ( id.p_img );

                TextView food = (TextView) foodItem.findViewById(R.id.food);
                TextView total = (TextView) foodItem.findViewById(R.id.total);
                TextView remain = (TextView) foodItem.findViewById(R.id.remain);
                TextView convert = (TextView) foodItem.findViewById(R.id.convert);

                final FragmentData fmData = AppData.fmList.get(i);
               // Log.e( "tony", "fmData =" + fmData );
               // Log.e( "tony", "fmData =" + fmData.imgUrl );



                if( AppData.itemImgCash.containsKey( fmData.name ) ){
                    Bitmap pImg = AppData.itemImgCash.get(fmData.name);
                    projectImg.setImageBitmap( pImg );

                }else{
                    //Glide.with(MainActivity.this).load(fmData.imgUrl).into(projectImg);
                    Glide.with(getApplicationContext())
                            .load(fmData.imgUrl)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    //image.setImageBitmap(resource); // Possibly runOnUiThread()
                                    projectImg.setImageBitmap( resource  );
                                    AppData.itemImgCash.put( fmData.name, resource );

                                 //   Log.e( "tony", "fmData put Cash=" +  resource);
                                }
                            });
                }

                //food.setText(String.format( getResources().getString(R.string.food_name), fmData.name) );
                food.setText( fmData.name );
                total.setText(String.format( getResources().getString(R.string.food_total), Integer.toString(fmData.total)) );
                remain.setText(String.format( getResources().getString(R.string.food_remain), Integer.toString(fmData.remainingCount)) );
                convert.setText(String.format( getResources().getString(R.string.food_convert), Integer.toString(fmData.convertedCount)) );

                if( i % 2 != 0 ){
                    Drawable itemBg = this.getResources().getDrawable(R.drawable.goods00);
                    LinearLayout foodInfo = (LinearLayout)foodItem.findViewById(id.food_item);
                    foodInfo.setBackground(itemBg);
                }
                foodInfoList.addView(foodItem);
            }

        }catch (Exception e){

        }


    }


    // 商品列表  update recoard List -----  實體化項目UI
    @SuppressLint({"NewApi", "ResourceAsColor"})
    public void updateRecoardInfoList(){

        if(AppData.recordList.size() <= 0) return;
        clearFoodInfoList();


        for( int i = 0; i < AppData.recordList.size(); i ++ ){
        //for( int i = 0; i < 30; i ++ ){
            LayoutInflater inflater = LayoutInflater.from(this);
            View coardItem = inflater.inflate(layout.fragment_coard, null);

            TextView date = (TextView) coardItem.findViewById(id.date);
            TextView name = (TextView) coardItem.findViewById(id.name);
            TextView project = (TextView) coardItem.findViewById(id.project);


            RecordData recordData = AppData.recordList.get(i);
            Log.e( "tony", "fmData =" + recordData );

            date.setText(  recordData.date);
            name.setText( recordData.sourceId );
            project.setText( recordData.projectName );

            if( i % 2 != 0 ){
               // Drawable itemBg = this.getResources().getDrawable(R.drawable.goods00);
                LinearLayout coardInfo = (LinearLayout)coardItem.findViewById(id.coard_item);
                coardInfo.setBackgroundColor(Color.WHITE);
            }


            final int index = i;
            final RecordData singleRecordData = recordData;
            coardItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Log.e("tony", "cccccc click project item = " + index);
                    showSingleExchange(singleRecordData);
                }
            });
            foodInfoList.addView(coardItem);
        }
    }


    private void clearFoodInfoList(){
        if(foodInfoList.getChildCount() <= 0) return;
        foodInfoList.removeAllViews();
    }



    // 自訂義 Toast
    String projectObj;
    public void setProject( String proName){
        projectObj = proName;
    }


    public void toastSuccess(){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater inflater = getLayoutInflater();
                //View layout = inflater.inflate(R.layout.success, (ViewGroup)MainActivity.this.findViewById(id.toast_success));
                View layout = inflater.inflate(R.layout.success2, (ViewGroup)MainActivity.this.findViewById(id.toast_success2));

                TextView successMsg = (TextView)layout.findViewById(id.success_tv2);

                String successStr = String.format( "兌換 %s 一份", projectObj );
                successMsg.setText( successStr  );
                Log.e( "tony" , "projectObj = " + projectObj);


                Toast toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    public void toastErrors(final String message ){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.errors, (ViewGroup)MainActivity.this.findViewById(id.toast_errors));

                TextView errorsMsg = (TextView)layout.findViewById(id.errors_tv);
                errorsMsg.setText( message );

                Toast toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.setView(layout);
                toast.show();
            }
        });
    }


    // 自訂義 alert
    private AlertDialog.Builder builder;

    private void showSettingSetting(){
        builder=new AlertDialog.Builder(this);


        builder.setTitle("設定");

        LinearLayout settingsView = (LinearLayout) getLayoutInflater().inflate(layout.alert_url_settings,null);
        final EditText server = (EditText) settingsView.findViewById(id.ipEd);
        final EditText login = (EditText) settingsView.findViewById(id.loginEd);
        final EditText info = (EditText) settingsView.findViewById(id.infoEd);
        final EditText barcode = (EditText) settingsView.findViewById(id.barcodeEd);
        final EditText covert = (EditText) settingsView.findViewById(id.covertEd);
        final EditText record = (EditText) settingsView.findViewById(id.recordEd);
        final EditText daily = (EditText) settingsView.findViewById(id.dailyEd);

        Button serverBtn = (Button) settingsView.findViewById( id.setOkBtn );
       // Button okBtn = (Button) settingsView.findViewById( id.setDefaultBtn );

        final SharedPreferences setting = getSharedPreferences("new_url", MODE_PRIVATE);
        //SharedPreferences setting = getSharedPreferences("login", MODE_PRIVATE);
        /// username.setText(setting.getString("username", ""));
        // password.setText(setting.getString("password", ""));

        server.setText( setting.getString( "SERVER", "13.124.140.159:12084"));
        login.setText( setting.getString( "LOGIN", "/partner/login"));
        info.setText( setting.getString( "INFO", "/partner/data") );
        barcode.setText( setting.getString( "BARCODE", "/partner/exchange_prepare"));
        covert.setText( setting.getString( "COVERT", "/partner/exchange_confirm") );
        record.setText( setting.getString( "RECORD", "/partner/exchange_record") );
        daily.setText( setting.getString( "DAILYRECORD", "/partner/exchange_daily_record") );




        serverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.SERVER = server.getText().toString().trim();
                AppData.URl_LOGIN= login.getText().toString().trim();
                AppData.URl_INFO= info.getText().toString().trim();
                AppData.URl_BARCODE= barcode.getText().toString().trim();
                AppData.URl_COVERT= covert.getText().toString().trim();
                AppData.URl_RECORD= record.getText().toString().trim();
                AppData.URl_DAILYRECORD= daily.getText().toString().trim();

                setting.edit().putString("SERVER", server.getText().toString().trim() ) .commit();
                setting.edit().putString("LOGIN", login.getText().toString().trim() ) .commit();
                setting.edit().putString("INFO", info.getText().toString().trim() ) .commit();
                setting.edit().putString("BARCODE", barcode.getText().toString().trim() ) .commit();
                setting.edit().putString("COVERT", covert.getText().toString().trim() ) .commit();
                setting.edit().putString("RECORD", record.getText().toString().trim() ) .commit();
                setting.edit().putString("DAILYRECORD", daily.getText().toString().trim() ) .commit();
            }
        });


        builder.setView(settingsView);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showSuccess(){
        builder=new AlertDialog.Builder(this);
        //builder.setTitle("設定");

        LinearLayout successView = (LinearLayout) getLayoutInflater().inflate(layout.alert_qr_success,null);

        TextView successMsg = (TextView)successView.findViewById(id.alertSuccessTv);
        //ImageView successImg = (ImageView)successView.findViewById(id.alertSuccessImg);

        String successStr = String.format( "兌換 %s 一份", projectObj );
        successMsg.setText( successStr  );

        //Glide.with(MainActivity.this).load(drawable.corners_success).into(successImg);

        builder.setView(successView);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private void showSingleExchange( RecordData recordData  ){
        builder=new AlertDialog.Builder(this);
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("兌換明細");

        LinearLayout singleRecordView= (LinearLayout) getLayoutInflater().inflate(layout.alert_single_exchange,null);

        ImageView errorNd = (ImageView) singleRecordView.findViewById(id.errorNd);
        LinearLayout successNd = (LinearLayout) singleRecordView.findViewById( id.successNd );
        //TextView convertName = (TextView) singleRecordView.findViewById(id.convertName);

        if( recordData != null && !recordData.equals("") ){
            TextView converId = (TextView) singleRecordView.findViewById(id.converId);
            TextView convertName = (TextView) singleRecordView.findViewById(id.convertName);
            TextView convertSource = (TextView) singleRecordView.findViewById(id.convertSource);
            TextView convertTime = (TextView) singleRecordView.findViewById(id.convertTime);

            converId.setText( recordData.convertId );
            convertName.setText(recordData.projectName);
            convertSource.setText(recordData.sourceId);
            convertTime.setText(recordData.detail_date);

            errorNd.setVisibility(View.GONE);
            successNd.setVisibility( View.VISIBLE );

        }else{
            errorNd.setVisibility(View.VISIBLE);
            successNd.setVisibility( View.GONE );
        }

        builder.setView(singleRecordView);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDailyRecord(){
        builder=new AlertDialog.Builder(this);
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("本日結算");

        LinearLayout dailyRecordView= (LinearLayout) getLayoutInflater().inflate(layout.alert_daily_recoard,null);
        TextView date  = (TextView)dailyRecordView.findViewById( id.dailyDate);
        TextView totalCount  = (TextView)dailyRecordView.findViewById( id.totalCount);

        LinearLayout convertList = (LinearLayout)dailyRecordView.findViewById(id.convertList);

        date.setText( (String)AppData.dailyRecordData.get("date"));
        totalCount.setText( (Integer) AppData.dailyRecordData.get("total") +"" );


        Log.e("tony ", "ddddd dailyData =" + AppData.dailyRecordList.size());
        if( AppData.dailyRecordList.size() > 0){

            for( int i = 0; i < AppData.dailyRecordList.size(); i ++ ){
                //for( int i = 0; i < 30; i ++ ){
                LayoutInflater inflater = LayoutInflater.from( MainActivity.this );
                View dailyItem = inflater.inflate(layout.fragment_daily_item, null);

                TextView nameTv = (TextView) dailyItem.findViewById(id.convertName);
                TextView countTv = (TextView) dailyItem.findViewById(id.convertCount);

                DailyRecordData dailyData = AppData.dailyRecordList.get(i);

                nameTv.setText( dailyData.projectName);
                countTv.setText( dailyData.projectCount + "");

                convertList.addView(dailyItem);
            }
        }

        builder.setView(dailyRecordView);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }











    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu(){
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION// hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    protected void hideButtonUi(){
        if (Build.VERSION.SDK_INT > 14) {
            Window window = getWindow();

            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION// hide nav bar
                          | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                          | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);

            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); //透明 **
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    // 获取ActionBar的高度
     public int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){  // 如果资源是存在的、有效的
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    // 點兩次退出 !!!
    private long firstPressedTime;
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }

    private void logoutApp(){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("確定要登出嗎?")//設定顯示的文字
                .setNegativeButton( "取消", null)
                .setPositiveButton("確定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.finish();
                    }
                })
                .show();
    }


}

package com.wz.tony.chick_vendor.Conn;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.wz.tony.chick_vendor.DataInfo.AppData;
import com.wz.tony.chick_vendor.DataInfo.DailyRecordData;
import com.wz.tony.chick_vendor.DataInfo.FragmentData;
import com.wz.tony.chick_vendor.DataInfo.RecordData;
import com.wz.tony.chick_vendor.MainActivity;
import com.wz.tony.chick_vendor.Public.Manager;
import com.wz.tony.chick_vendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AnalysisJson {

    MainActivity main;
    String resData;


    public AnalysisJson(String resData){
        this.main = Manager.getInst().mainActivity;
        this.resData = resData;
    }

    public void loginData() throws JSONException {
        String toastMsg = "";
        JSONObject jsonData = new JSONObject(resData);
        int code = jsonData.getInt("Code");
        Log.e("tony", " resData =" + resData);
        Log.e("tony", " code =" + code);

        if(code == 0){
            String data = jsonData.getString("Data");
            String token = new JSONObject(data).getString("Token");
            Boolean isSuccess  = AppData.setToken(token);
            Log.e("tony", " isSuccess =" + isSuccess);

            if(isSuccess){

                AppData.VENDORNAME = new JSONObject(data).getString("Name");
                Manager.getInst().changeMain();

                toastMsg = Manager.getInst().mainActivity.getResources().getString(R.string.post_200_success);
            }else{
                toastMsg = Manager.getInst().mainActivity.getResources().getString(R.string.post_200_errors);
            }

        }else{
            //String data = jsonData.getString("Data");
            //String reason = new JSONObject(data).getString("Reason");
            String reason = jsonData.getString("Reason");
            Log.e("tony", " reason =" + reason);
            toastMsg = reason;
        }

        Manager.getInst().myToast(toastMsg);
    }

    //所有兌換商品資訊 vendor food data
    public void voderData() throws JSONException {
        String toastMsg = "";
        JSONObject jsonData = new JSONObject(resData);
        int code = jsonData.getInt("Code");
        //Log.e("tony", " code =" + code);
        //Log.e("tony", " vvvvvvvvvvvvvcode =" + resData);

        if(code == 0){
            String data = jsonData.getString("Data");
            JSONArray jsonItems = new JSONObject(data).getJSONArray("Item");
            //JSONArray jsonItems = new JSONObject(data).getJSONArray("Items");
            AppData.fmList = new ArrayList<FragmentData>();

            for( int i = 0; i < jsonItems.length(); i ++){
                FragmentData fmData = new FragmentData();

                JSONObject jsonItem = new JSONObject();
                jsonItem = jsonItems.getJSONObject(i);

                String name = jsonItem.getString("Name");
                int total = jsonItem.getInt("Total");

                int remainingCount = jsonItem.getInt("RemainingCount"); //剩餘
                int unconvertedCount = jsonItem.getInt("UnconvertedCount"); //未換的
                int convertedCount = jsonItem.getInt("ConvertedCount"); //已換的
                String imgUrl = jsonItem.getString("ImageUrl");

                Log.e( "tony", "vvvvv Items =  "  + name + "-" + total + "-" + convertedCount + "-" + remainingCount);
                Log.e( "tony", "vvvvv Items =  "  + imgUrl);


                //fmData.loadData( name, total, convertedCount, remainingCount );
                fmData.loadData( name, remainingCount, convertedCount, unconvertedCount, imgUrl );
                AppData.fmList.add(fmData);
            }

            //Log.e( "tony", "vvvvvvvvvvvvvcode  jsonItems =  "  + jsonItems.length());
            //Log.e( "tony", "vvvvvvvvvvvvvcode  jsonItems =  "  + jsonItems);
            toastMsg = Manager.getInst().mainActivity.getResources().getString(R.string.voder_update);

        }else{

            //String data = jsonData.getString("Data");
            //String reason = new JSONObject(data).getString("Reason");

            String reason = jsonData.getString("Reason");
            Log.e("tony", " reason =" + reason);
            toastMsg = reason;
        }

        Manager.getInst().myToast(toastMsg);
    }


    //Record 兌換記錄
    public void recordData() throws JSONException {
        String toastMsg = "";
        JSONObject jsonData = new JSONObject(resData);
        int code = jsonData.getInt("Code");
        Log.e("tony", " recordData code =" + code);
        Log.e("tony", " vvvvvvvvvvvvvcode =" + resData);

        if(code == 0){
            String data = jsonData.getString("Data");
            JSONArray jsonRecords = new JSONObject(data).getJSONArray("Records");
            //JSONArray jsonItems = new JSONObject(data).getJSONArray("Items");

            Log.e("tony", " recordData length =" + jsonRecords.length());
            AppData.recordList = new ArrayList<RecordData>();

            for( int i = 0; i < jsonRecords.length(); i ++){
                RecordData recordData = new RecordData();

                JSONObject jsonRecord = new JSONObject();
                jsonRecord = jsonRecords.getJSONObject(i);


                String convertId = jsonRecord.getString("ConvertId");
                String sourceId= jsonRecord.getString("SourceId");
                String sourceName= jsonRecord.getString("SourceName");
                String projectName = jsonRecord.getString("Name");
                int projectCount = jsonRecord.getInt("Count");

                //String date = jsonRecord.getString("Date");
                String date = jsonRecord.getString("Time");
                int findIndex = date.indexOf( "T" );
                String rDate = "日 "+ date.substring(5, findIndex) + " 時 "+ date.substring(findIndex+1, date.length());
                date = date.substring(0, findIndex) + " " + date.substring(findIndex+1, date.length());



                //String date = "test time" ;
                Log.e( "tony", "record =  "  + convertId + "-" + sourceId + "-" + sourceName + "-" + projectName + "-" + projectCount);
                recordData.loadData( convertId, rDate, date, sourceId, sourceName, projectName, projectCount );
                AppData.recordList.add(recordData);
            }

           // Log.e( "tony", "vvvvvvvvvvvvvcode  jsonItems =  "  + jsonItems.length());
            //Log.e( "tony", "vvvvvvvvvvvvvcode  jsonItems =  "  + jsonItems);
            toastMsg = Manager.getInst().mainActivity.getResources().getString(R.string.voder_update);

        }else{

            //String data = jsonData.getString("Data");
            //String reason = new JSONObject(data).getString("Reason");

            String reason = jsonData.getString("Reason");
            Log.e("tony", " reason =" + reason);
            toastMsg = reason;
        }

        Manager.getInst().myToast(toastMsg);
    }


    //dailyRecordData  每日結算
    public Map<String, Object> dailyRecordData() throws JSONException {

        Map<String, Object> map = new HashMap<String, Object>();

        String toastMsg = "";
        JSONObject jsonData = new JSONObject(resData);
        int code = jsonData.getInt("Code");
        Log.e("tony", " dailyRecordData code =" + code);
        Log.e("tony", " vvvvvvvvvvvvvcode =" + resData);

        if(code == 0){

            String data = jsonData.getString("Data");
            String date = new JSONObject(data).getString("Date");

            int findIndex = date.indexOf( "T" );
            if ( findIndex > -1 ) date = date.substring(0, findIndex);

            int total = 0;


            JSONArray jsonItems = new JSONObject(data).getJSONArray("Items");
            AppData.dailyRecordList = new ArrayList<DailyRecordData>();

            Log.e("tony", " vvvvvvvvvvvvvcode DailyRecordData-jsonItems=" + jsonItems.length());


            for( int i = 0; i < jsonItems.length(); i ++){
                DailyRecordData dailyRecordData = new DailyRecordData();

                JSONObject jsonDailyRecord = new JSONObject();
                jsonDailyRecord = jsonItems.getJSONObject(i);

                String projectName = jsonDailyRecord.getString("Name");
                int projectTotal = jsonDailyRecord.getInt("Total");
                total += projectTotal;

                dailyRecordData.loadData( projectName, projectTotal );
                AppData.dailyRecordList.add(dailyRecordData);
            }
            Log.e("tony", " vvvvvvvvvvvvvcode total=" + total);
            Log.e("tony", " vvvvvvvvvvvvvcode dailyRecordData=" + AppData.dailyRecordList.size());

            map.put("date", date);
            map.put("total", total);
            AppData.dailyRecordData = map;

            return map;
        }
        return null;
    }







    //barcodeData  掃碼 是否有這QR
    public void barcodeData() throws JSONException {
        String toastMsg = "";
        JSONObject jsonData = new JSONObject(resData);



        int code = jsonData.getInt("Code");
        Log.e("tony", " bbbbbbarcodeData =" + code);
        //Log.e("tony", " bbbbbbbbb bbbbbbarcodeData =" + code);
        Log.e("tony", " bbbbbbbbb bbbbbbarcodeData =" + resData);

        if(code == 0){
            String data = jsonData.getString("Data");

            String foodName = new JSONObject(data).getString("Name");
            String source = new JSONObject(data).getString("Source");
            String transKey = new JSONObject(data).getString("TransKey");
            int count = new JSONObject(data).getInt("Count");

            Log.e("tony", " bbbbbbbbb  foodName = " +foodName  );
            Log.e("tony", " bbbbbbbbb  source = " +source  );
            Log.e("tony", " bbbbbbbbb  transKey = " +transKey  );
            Log.e("tony", " bbbbbbbbb  count = " +count  );

            //updateConvertFood("success");
            AppData.setTransKey( transKey );
            updateConvertFood(
                    String.format(Manager.getInst().mainActivity.getResources().getString(R.string.main_convert), foodName ) );


            //　設定兌換產品
            Manager.getInst().mainActivity.setProject(foodName);

            // 成功直接兌換 _____convertFoodAction
            Manager.getInst().mainActivity.convertFoodAction();

        }else{


            //String data = jsonData.getString("Data");
            //String reason = new JSONObject(data).getString("Reason");

            String reason = jsonData.getString("Reason");
            Log.e("tony", " reason =" + reason);


            updateConvertFood(reason);
            //失敗錯誤原因
            Manager.getInst().mainActivity.toastErrors(reason);
        }
    }


    //covertData food
    public void covertData()throws JSONException {

        JSONObject jsonData = new JSONObject(resData);
        int code = jsonData.getInt("Code");
        Log.e("tony", " ccccc  codeData =" + code);
        Log.e("tony", " ccccc  codeData =" + resData);
        Log.e("tony", " bbbbbbbbb  codeData =" + resData);

        if(code == 0){
            //Manager.getInst().myToast(reason);
            AppData.clearTransKey();

            final String converSuccess = main.getResources().getString(R.string.convert_success);

            Manager.getInst().mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    main.setBarcodeLb("");
                    main.setConvertFood(converSuccess);
                    //main.updateFoodInfoList();
                }
            });



            //兌換成功
            // 自訂義toast兌換成功
            //Manager.getInst().mainActivity.toastSuccess();

            // 自訂義alert兌換成功
            Manager.getInst().mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Manager.getInst().mainActivity.showSuccess();
                }
            });


        }else{
            //String data = jsonData.getString("Data");
            //String reason = new JSONObject(data).getString("Reason");

            String reason = jsonData.getString("Reason");
            updateConvertFood(reason);
            //Manager.getInst().myToast(reason);
            Manager.getInst().mainActivity.toastErrors(reason);
        }
    }







    //update ui -----
    private void updateConvertFood(final String msg){
        Manager.getInst().mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Manager.getInst().mainActivity.setConvertFood(msg);
            }
        });
    }

    private void updateBarcode(final String msg){
        Manager.getInst().mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Manager.getInst().mainActivity.setBarcodeLb(msg);
            }
        });
    }



}

package com.wz.tony.chick_vendor.Conn;

import android.util.Log;

import com.wz.tony.chick_vendor.DataInfo.AppData;
import com.wz.tony.chick_vendor.DataInfo.BarcodeData;
import com.wz.tony.chick_vendor.DataInfo.ConvertData;
import com.wz.tony.chick_vendor.DataInfo.LoginData;
import com.wz.tony.chick_vendor.Public.Manager;
import com.wz.tony.chick_vendor.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ConnServ extends Thread {

    String httpUrl;
    LoginData loginData;
    List<NameValuePair> params;


    public ConnServ(String url){

        Log.e( "tony", " cccccc ConnServ " + url );

        this.httpUrl = String.format( "http://%s", AppData.SERVER + url );
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Token", AppData.getToken()));
        //Log.e( "tony", "token =" + AppData.getToken() );
    }

    public ConnServ(String url, ConvertData convertData){
        this.httpUrl = String.format( "http://%s", AppData.SERVER + url );

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Token", convertData.token));
        params.add(new BasicNameValuePair("TransKey", convertData.transKey));
    }


    public ConnServ(String url, BarcodeData barcodeData){
        this.httpUrl = String.format( "http://%s", AppData.SERVER + url );

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Token", barcodeData.token));
        params.add(new BasicNameValuePair("Key", barcodeData.key));
        Log.e( "tony", "SUCESS");
    }

    public ConnServ(String url, LoginData loginData) {
        this.httpUrl = String.format( "http://%s", AppData.SERVER + url );
        this.loginData = loginData;
        //Log.e( "tony", "httpUrl =" + httpUrl );

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Username", loginData.user));
        params.add(new BasicNameValuePair("Password", loginData.pass));
    }

    public void run(){
        Log.e("tony" , "threa is run");
         //* Returns true if the string is null or 0-length.
        if(httpUrl.isEmpty()) return;

        int postCode = 400;

        HttpPost httpPost = new HttpPost(httpUrl);
        HttpClient httpClient = new DefaultHttpClient();
        UrlEncodedFormEntity entity;
        HttpResponse response;

        try {
            /*List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Username", loginData.user));
            //params.add(new BasicNameValuePair("Password", "12345678"));
            params.add(new BasicNameValuePair("Password", loginData.pass));
            */

            entity = new UrlEncodedFormEntity(params, "utf-8");
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            postCode = response.getStatusLine().getStatusCode();

            String resData = EntityUtils.toString(response.getEntity());

            Log.e(  "tony" ,  "test postCode= "+ postCode   );
            Log.e(  "tony" ,  "test response= "+ response   );
            Log.e(  "tony" ,  "test resData= "+ resData   );

            Manager.getInst().mainActivity.reqCallBack(postCode, resData);

        } catch (UnsupportedEncodingException e) {
            Manager.getInst().cancelAlertDialog();
            Manager.getInst().myToast(Manager.getInst().mainActivity.getResources().getString(R.string.http_errors));
            e.printStackTrace();

        } catch (ClientProtocolException e) {
            Manager.getInst().cancelAlertDialog();
            Manager.getInst().myToast(Manager.getInst().mainActivity.getResources().getString(R.string.http_errors));
            e.printStackTrace();

        } catch (IOException e) {
            Manager.getInst().cancelAlertDialog();
            Manager.getInst().myToast(Manager.getInst().mainActivity.getResources().getString(R.string.http_errors));
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    private void responseJSONData(String result){
        try{
            JSONObject jsonData = new JSONObject(result);

            /*
            size.width = jsonData.getInt("width");
            size.height = jsonData.getInt("height");
*/
        }catch (Exception e){}
    }


}

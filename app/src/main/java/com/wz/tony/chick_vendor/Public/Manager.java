package com.wz.tony.chick_vendor.Public;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wz.tony.chick_vendor.MainActivity;
import com.wz.tony.chick_vendor.R;

import java.util.ArrayList;

public class Manager {


    private static  Manager _inst;
    private static Object _syncInst = new Object();

    public static Manager getInst(){
        if( _inst == null){
            synchronized ( _syncInst ){
                if( _inst == null ){
                    _inst = new Manager();
                }
            }
        }
        return _inst;
    }


    private MainActivity _context;
    public MainActivity mainActivity;

    public void setContext(MainActivity context) {
        _context = context;
        mainActivity = context;
    }


    public void changeMain(){

        _context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.changeMain();
            }
        });
    }




    //Toast-----------------------------------------
    public void myToast(final String message){
        _context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }




    //alert-----------------------------------------

    public AlertDialog msgDialog(DialogInterface.OnClickListener okHandler) {

        DialogSet dialogSet = newDialogSet();
        if(okHandler != null){
            dialogSet.builder.setPositiveButton("Ok", okHandler);
        }
        AlertDialog dialog = dialogSet.getDialog();
        return dialog;
    }


    private ArrayList<DialogSet> _alertQueue = new ArrayList<DialogSet>();
    private int _lastDialogId = 0;


    public static class DialogSet{
        public int id = -1;
        public AlertDialog.Builder builder;
        private AlertDialog _dialog;

        public DialogSet(int id, AlertDialog.Builder builder){
            this.id = id;
            this.builder = builder;
        }

        public AlertDialog getDialog() {
            if(_dialog == null){
                _dialog = builder.create();
            }
            return _dialog;
        }
    }

    public DialogSet newDialogSet() {
        DialogSet dialogSet = null;

        synchronized (_alertQueue) {
            AlertDialog.Builder builder = new AlertDialog.Builder(_context);

            int id = _lastDialogId + 1;
            _lastDialogId = id;
            dialogSet = new DialogSet(id, builder);

            _alertQueue.add(dialogSet);
        }

        return dialogSet;
    }


    public DialogSet popAlertDialog() {
        synchronized (_alertQueue) {
            if(_alertQueue.isEmpty() == false){
                int index = _alertQueue.size() - 1;
                DialogSet item = _alertQueue.get(index);
                _alertQueue.remove(index);
                return item;
            }
        }
        return null;
    }


    public boolean cancelAlertDialog() {

        MainActivity activity = (MainActivity)_context;
        DialogSet dialogSet = popAlertDialog();

        // System.out.println("Cancel DialogSet=" +  dialogSet.id);

        if(dialogSet != null){
            if(activity != null) {
                activity.handler.post(new AlterDismiss(activity, dialogSet.getDialog()));
                return true;
            }
        }        //activity.runOnUiThread(new AlterDismiss(activity));

        return false;
    }



    class AlterDismiss implements Runnable {

        MainActivity activity;
        AlertDialog dialog;

        public AlterDismiss(MainActivity activity, AlertDialog dialog){
            this.activity = activity;
            this.dialog = dialog;
        }

        @Override
        public void run() {
            System.out.println("!!!!!!!!!!!!!! cancelAlter activity=" + activity);
            dialog.cancel();
        }
    }

}

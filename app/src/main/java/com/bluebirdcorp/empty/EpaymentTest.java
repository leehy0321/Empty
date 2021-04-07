package com.bluebirdcorp.empty;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import co.kr.bluebird.android.epayment.serial.BBReader;

public class EpaymentTest extends android.app.Activity implements Button.OnClickListener{
    private static final String TAG = "SQA_Test";

    private RadioGroup radioGroup;
    private Button btn_set;
    private TextView txt_currentValue;

    private BBReader mReader;
    private ProgressDialog mDialog;
    int nowMode;

    private static final int MSG_UPDATE_TEXT = 1000;
    private static final int MSG_UPDATE_DATA = 1001;
    private static final int MSG_UPDATE_DESCRIPTION = 1002;
    private static final int MSG_SHOW_TOAST = 1003;
    private static final int MSG_UPDATE_CID_UI = 1004;
    private static final int MSG_SHOW_DIALOG = 1005;
    private static final int MSG_CLOSE_DIALOG = 1006;
    private static final int MSG_SHOW_PERCENT_DIALOG = 1007;
    private static final int MSG_SHOW_BUTTON_DIALOG = 1008;
    private static final int NORMAL_DIALOG = 0;
    private static final int PERCENT_DIALOG = 1;
    private static final int BUTTON_DIALOG = 2;
    private static final int REQUEST_STORAGE = 5555;

    //MSRTest, ICTest
    private static final int STATE_ASYNC_CMD = 4;

    //STATE_WILD_CMD
    //Reset, Deinit, StopTransaction
    private static final int STATE_WILD_CMD = 8;
    private int mState;

    private BBReader.ReaderCmdResult r1;
    private BBReader.TransactionCmdResult r2;
    private String mStartTransactionData;
    private String mRequestKeyInformationData;
    TextView tv;
    private boolean testing;
    Context mContext;
    int index = 1;

    testTread t;

    final Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.arg2 == 0) { // start
                tv.setText(index + " start transaction ");
            }else if(msg.arg2 == 1){
                tv.setText(index + " complete transaction ");
            }else if(msg.arg2 == 2){
                tv.setText(index + " Error Find   ERR_IPK_APPLY");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(TAG, "onCreate");

        // storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
            }
        }

        mReader = new BBReader(this, mHandler);

        mStartTransactionData = null;
        mRequestKeyInformationData = null;

        Button btn = findViewById(R.id.start_button);
        btn.setOnClickListener(this);

        tv = findViewById(R.id.paymenttext_textview);
        mContext = this;

        t = new testTread();

        Size bestChoice = new Size(1,2);

    }

    public class testTread extends Thread{
        boolean result = false;
        @Override
        public void run() {
            while(!result) {
                tv = findViewById(R.id.paymenttext_textview);
                Message msg = new Message();
                msg.arg1 = index;
                msg.arg2 = 0;
                handler.sendMessage(msg);
                runStartTransation(index);

                Message msg2 = new Message();
                msg2.arg1 = index ;
                msg2.arg2 = 1;
                handler.sendMessage(msg2);
                result = runCompleteTransation(index);
                index++;
            }

            Message msg2 = new Message();
            msg2.arg1 = index ;
            msg2.arg2 = 2;
            handler.sendMessage(msg2);
        }
    }

    private void runStartTransation(int i){
        Log.d(TAG, "[ "+ i +" ] :: "+ "StartTransaction start!");
        byte[] payload = getBytesFromString(mStartTransactionData);
        r2 = mReader.StartTransaction(payload);
        if (r2.getResult() == BBReader.TransactionCmdResult.LIB_SUCCESS) {
            Log.d(TAG, "[ "+ i +" ] :: "+ "StartTransaction finish - LIB_SUCCESS");
        } else{
            Log.d(TAG, "[ "+ i +" ] :: "+ "StartTransaction finish - failed = " + getErrorCodeName(r2.getResult()) + "  0x" + Integer.toHexString(r2.getResult()).toUpperCase());
        }
    }

    private void runStopTransation(int i){
        Log.d(TAG, "[ "+ i +" ] :: "+ "runStopTransation start!");
        BBReader.TransactionCmdResult r = mReader.StopTransaction();
        if (r.getResult() == BBReader.SecureCmdResult.LIB_SUCCESS) {
            Log.d(TAG, "[ "+ i +" ] :: "+ "runStopTransation finish - LIB_SUCCESS");
        } else
            Log.d(TAG, "[ "+ i +" ] :: "+ "runStopTransation finish -  failed = " + getErrorCodeName(r.getResult()) + "  0x" + Integer.toHexString(r.getResult()).toUpperCase());
    }
    private boolean runCompleteTransation(int i){
        Log.d(TAG, "[ "+ i +" ] :: "+ "runCompleteTransation start!");
        byte[] payload2 = getBytesFromString(mRequestKeyInformationData);
        BBReader.TransactionCmdResult r = mReader.CompleteTransaction(null);
        if (r.getResult() == BBReader.SecureCmdResult.LIB_SUCCESS) {
            Log.d(TAG, "[ "+ i +" ] :: "+ "runCompleteTransation finish - LIB_SUCCESS");
        } else {
            Log.d(TAG, "[ " + i + " ] :: " + "runCompleteTransation finish -  failed = " + getErrorCodeName(r.getResult()) + "  0x" + Integer.toHexString(r.getResult()).toUpperCase() + " + // BBReader.ReaderTasimCmdResult.ERR_IPEK_APPLY :  " + BBReader.ReaderTasimCmdResult.ERR_IPEK_APPLY);
            Log.d(TAG, "[ " + i + " ] :: " + "runCompleteTransation finish -  failed = " + r.getResult());
            if(r.getResult() == 182) return true;
        }
        return false;
    }
    private void runInit(int i){
        Log.d(TAG, "[ "+ i +" ] :: "+ "runInit start!");
        r1 = mReader.Init(BBReader.CID_MODA);
        if (r1.getResult() == BBReader.SecureCmdResult.LIB_SUCCESS) {
            Log.d(TAG, "[ "+ i +" ] :: "+ "runInit finish - LIB_SUCCESS");
        } else
            Log.d(TAG, "[ "+ i +" ] :: "+ "runInit finish -  failed = " + getErrorCodeName(r1.getResult()) + "  0x" + Integer.toHexString(r1.getResult()).toUpperCase());
    }

    @Override
    protected void onDestroy() {
        if (mReader != null) {
            Log.d(TAG,"Destroy");
            BBReader.ReaderCmdResult r = mReader.Deinit();
        }
        super.onDestroy();
    }


    private byte[] getBytesFromString(String str) {
        byte[] b = null;
        if (!(str == null || str.length() <= 0)) {
            b = new byte[str.length()];
            for (int i = 0; i < str.length(); i++)
                b[i] = (byte) str.charAt(i);
        }
        return b;
    }
    private String getErrorCodeName(int errorCode) {
        Field[] constants = BBReader.class.getDeclaredFields();
        for (Field field : constants) {
            Class type = field.getType();
            if (Modifier.isStatic(field.getModifiers()) && type == int.class) {
                try {
                    int value = field.getInt(null);
                    if (value == errorCode) {
                        // Found!!
                        return field.getName();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (type == BBReader.ReaderCmdResult.class ||
                    type == BBReader.SecureCmdResult.class ||
                    type == BBReader.TransactionCmdResult.class ||
                    type == BBReader.ReaderTasimCmdResult.class ||
                    type == BBReader.DeviceContactCmdResult.class ||
                    type == BBReader.UpdateCmdResult.class ||
                    type == BBReader.ReaderTestCmdResult.class) {
                Field[] subFields = type.getDeclaredFields();
                for (Field subField : subFields) {
                    if (Modifier.isStatic(subField.getModifiers()) && subField.getType() == int.class) {
                        try {
                            int value = subField.getInt(null);
                            if (value == errorCode) {
                                // Found in Inner class
                                return subField.getName();
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // msg.arg1, msg.arg2, msg.obj, msg.what
            switch (msg.what) {
                case BBReader.MSG_WHAT_BBREADER:
                    Log.d(TAG,"MSG_WHAT_BBREADER");
                    if (msg.arg1 == BBReader.MSG_ARG1_UPDATE_PROGRESS_PERCENT) {
                        Log.d(TAG,"1");
                    } else if (msg.arg1 == BBReader.MSG_ARG1_MSR_EVENT) {
                        if (msg.arg2 == BBReader.TransactionCmdResult.LIB_SUCCESS) {
                            byte[] data = (byte[]) msg.obj;
                            Log.d(TAG,"2");
                            if (data != null && data.length > 0) {
                                Log.d(TAG,"3");
                            }
                        } else {
                            Log.d(TAG,"4");
                        }
                    }else if(msg.arg1 == BBReader.MSG_ARG1_IC_EVENT){
                        Log.d(TAG,"MSG_ARG1_IC_EVENT");
                        if (msg.arg2 == BBReader.TransactionCmdResult.LIB_SUCCESS) {
                            Log.d(TAG,"MSG_ARG1_IC_EVENT - LIB_SUCCESS");
                            byte[] data = (byte[]) msg.obj;
                            mHandler.obtainMessage(MSG_UPDATE_TEXT, "# IC_CARD_INSERTED").sendToTarget();
                        }else {
                            Log.d(TAG,"MSG_ARG1_IC_EVENT - not LIB_SUCCESS");
                        }
                    }
                    break;
                case MSG_UPDATE_TEXT:
                    Log.d(TAG,"MSG_UPDATE_TEXT");
                    if (msg.obj != null) {
                        Log.d(TAG,"MSG_UPDATE_TEXT - not null");
                    }
                    break;
                case MSG_UPDATE_DATA:
                    Log.d(TAG,"MSG_UPDATE_DATA");
                    if (msg.obj != null) {
                        Log.d(TAG,"MSG_UPDATE_DATA - not null");
                    }
                    break;
                case MSG_UPDATE_DESCRIPTION:
                    Log.d(TAG,"MSG_UPDATE_DESCRIPTION");
                case MSG_SHOW_TOAST:
                    Log.d(TAG,"MSG_SHOW_TOAST");
                    break;
                case MSG_UPDATE_CID_UI:
                    Log.d(TAG,"MSG_UPDATE_CID_UI");
                    if (msg.arg1 == 1) {
                        Log.d(TAG,"MSG_UPDATE_CID_UI - 1");
                    } else {
                        Log.d(TAG,"MSG_UPDATE_CID_UI - 2");
                    }
                    break;
                case MSG_SHOW_DIALOG:
                    Log.d(TAG,"MSG_SHOW_DIALOG");
                    break;
                case MSG_SHOW_PERCENT_DIALOG:
                    Log.d(TAG,"MSG_SHOW_PERCENT_DIALOG");
                    break;
                case MSG_SHOW_BUTTON_DIALOG:
                    Log.d(TAG,"MSG_SHOW_BUTTON_DIALOG");
                    break;
                case MSG_CLOSE_DIALOG:
                    Log.d(TAG,"MSG_CLOSE_DIALOG");
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:

                Log.d(TAG,"Start Test");
                test();
                break;

        }
    }

    private void test(){
        testing = true;
        Log.d(TAG, "[ main ] :: "+ "Init start!");

        r1 = mReader.Init(BBReader.CID_MODA);
        tv = findViewById(R.id.paymenttext_textview);
        if (r1.getResult() == BBReader.ReaderCmdResult.LIB_SUCCESS) {
            Log.d(TAG, "[ main ] :: "+"Init finish - LIB_SUCCESS");
        } else {
            Log.d(TAG, "[ main ] :: " + "Init finish - failed = " + getErrorCodeName(r1.getResult()) + "  0x" + Integer.toHexString(r1.getResult()).toUpperCase());
        }

        t.start();
    }
}
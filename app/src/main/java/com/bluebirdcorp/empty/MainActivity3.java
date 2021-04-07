package com.bluebirdcorp.empty;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity3 extends android.app.Activity {
    private static final String TAG = "MainActivity3";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    /*    int MODE_POWER_MASK  = 0x01;
        int MODE_POWER_SINK   = 0x00;
        int MODE_DATA_NONE   = 0x00 << 1;
        int MODE_POWER_SOURCE = 0x01;
        int MODE_DATA_MTP    = 0x01 << 1;
        int MODE_DATA_PTP    = 0x02 << 1;
        int MODE_DATA_MIDI   = 0x03 << 1;
        int MODE_DATA_PTP2    = 0x04 << 1;

        int MODE_GADGET_SERIAL   = 0x05 << 1;
        int gadget = MODE_POWER_SINK | MODE_GADGET_SERIAL;
        Log.d(TAG,""+(MODE_POWER_SINK|MODE_DATA_NONE));
        Log.d(TAG,""+(MODE_POWER_SOURCE|MODE_DATA_NONE));
        Log.d(TAG,""+(MODE_POWER_SINK|MODE_DATA_MTP));
        Log.d(TAG,""+(MODE_POWER_SINK|MODE_DATA_PTP));
        Log.d(TAG,""+(MODE_POWER_SINK|MODE_DATA_MIDI));
        Log.d(TAG,""+(MODE_POWER_SINK|MODE_DATA_PTP2));
        Log.d(TAG,""+(MODE_POWER_SOURCE|MODE_GADGET_SERIAL));

        if(0x09 == (MODE_POWER_SOURCE|MODE_DATA_PTP2))  Log.d(TAG,"sssss");

        Log.d(TAG,"gadget : "+ gadget + " // "+ (gadget ==( MODE_POWER_SOURCE|MODE_GADGET_SERIAL)));

        Hashtable<Integer, Boolean> m5g_channel_numbers = new Hashtable<Integer, Boolean>();
        m5g_channel_numbers.put(1,true);
        m5g_channel_numbers.put(5,false);
        m5g_channel_numbers.put(2,true);
        m5g_channel_numbers.put(10,true);
        m5g_channel_numbers.put(6,true);
        m5g_channel_numbers.put(3,true);
        m5g_channel_numbers.put(13,true);
        m5g_channel_numbers.put(17,true);
        m5g_channel_numbers.put(15,true);
        m5g_channel_numbers.put(16,true);
        m5g_channel_numbers.put(7,true);
        m5g_channel_numbers.put(8,true);
*/

/*
        List<Integer> keyList = new ArrayList<Integer>(m5g_channel_numbers.keySet());
        List<Integer> keyList2 = new ArrayList<Integer>();
        String printArray = "";
        for(int i : keyList){
            printArray += (" " + i);
        }
        Log.d(TAG,"origin :: " + printArray);

        Collections.sort(keyList);
        printArray = "";
        for(int i : keyList){
            printArray += (" " + i);
        }
        Log.d(TAG,"after sort :: " + printArray);

        Log.d(TAG,"after cal :: " + makeSummary(keyList));

        for (int channel : keyList) {
            Log.d(TAG,"channel :: " + channel);
        }

        printArray = "aaa";
        if(printArray.isEmpty())
            Log.d(TAG,"SAME");
*/

        /*
        String originChannels ="1 2 3 4 5 7 8 9 10 12";
        List<Integer> origin_channel_number = new ArrayList<Integer>();
        String[] origin_channel_array = null;
        origin_channel_array = originChannels.split(" ");
        int max = Integer.parseInt(origin_channel_array[origin_channel_array.length-1]);
        Log.d(TAG, "max"+max);

        String targetChannel = "3 4";
        List<Integer> target_channel_number = new ArrayList<Integer>();
        String[] target_channel_array = null;
        target_channel_array = targetChannel.split(" ");
        int max2 = Integer.parseInt(target_channel_array[target_channel_array.length-1]);
        Log.d(TAG, "max2 "+max2);

        target_channel_number.get(0);
        target_channel_number.get(target_channel_number.size() -1);
        //String array -> int list
        for (String channel : origin_channel_array) {
            origin_channel_number.add(Integer.parseInt(channel));
        }
        if(target_channel_number.size)
        for (String channel : target_channel_array) {
            target_channel_number.add(Integer.parseInt(channel));
        }
        Collections.sort(target_channel_number);

        for(int targetChannelTemp : target_channel_number){
            if(origin_channel_number.contains(targetChannelTemp)){
                target_channel_number.remove(targetChannelTemp);
            }
        }


        //for log
        String result = "";
        for(int resultChannel : result_channel_number){
            result += resultChannel + " ";
        }
        Log.d(TAG, "result : " + result   );
        // for log

        Log.d(TAG, "result Summary  : " +makeSummary(result_channel_number));
    }

    private String makeSummary(List<Integer> keyList){
        String printSummary = "";

        int start, end, prev, next;
        String temp = "";
        int keyListSize = keyList.size();
        start = end = prev = next = keyList.get(0);

        for(int i=1; i<keyListSize; i++){
            next = keyList.get(i);
            if(i == keyListSize-1) {
                if(end == start){
                    printSummary += end;
                } else{
                    if(end+1 == next){
                        printSummary += "(" + start + "-" + next + ")";
                    } else{
                        printSummary += "(" + start + "-" + end + "),"+next;
                    }
                }
            }else if(prev+1 == next){
                end = next;
                prev = next;
            }else{
                if(end == start){
                    printSummary += end +",";
                } else{
                    printSummary += "("+start+"-"+end+"),";
                }
                start = prev = end = next;
            }
        }

        return printSummary;*/

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(getLynxFirmwareVersion());

        String aa = "left[.5W],back[1WC];home;recent[1WC],right[.5W]";

        String[] sets = aa.split(";", 3);

        for(String set:sets){
            Log.d("hayoung",set);
        }

        String[] start = sets[0].split(",");
        for(String set:start){
            Log.d("hayoung", "start : " + set);
        }
        String[] center = sets[1].split(",");
        for(String set:center){
            Log.d("hayoung", "center : " + set);
        }
        String[] end = sets[2].split(",");
        for(String set:end){
            Log.d("hayoung", "end : " + set);
        }

    }

    private static final String LYNX_FIRMWARE_PROPERTY = "ro.vendor.bluebird.lynx.aai.version";
    private String getLynxFirmwareVersion() {
        String def = ""; // default 값
        String ret = def;

        try {
            Class<?> SystemProperties = Class.forName("android.os.SystemProperties");

            //Parameters Types
            Class[] paramTypes = {String.class, String.class};
            Object[] params = {LYNX_FIRMWARE_PROPERTY, def};
            ret = (String)  SystemProperties.getMethod("get", paramTypes).invoke(SystemProperties, params);

        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
        }

        return ret;
    }

    private static final int KEYCODE_SAN = 288;
    private static final int KEYCODE_PTT = 287;

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KEYCODE_PTT: // PTT
                Toast.makeText(this, "PTT KEY Click", Toast.LENGTH_SHORT).show();
                return false;
            case KEYCODE_SAN: // SCAN
                Toast.makeText(this, "SCAN KEY Click", Toast.LENGTH_SHORT).show();
                return false;
        }
        return false;
    }*/
}
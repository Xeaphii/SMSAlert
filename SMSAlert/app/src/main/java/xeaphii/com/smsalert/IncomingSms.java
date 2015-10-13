package xeaphii.com.smsalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

/**
 * Created by Xeaphii on 9/25/2015.
 */
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    private final String MY_PREFS_NAME ="Prefs";

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    String senderNum =  currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    if(senderNum.trim().equals("+254710156646")){

                        if(message.trim().equals("#ss01#")){
                            abortBroadcast();
                            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
                            editor.putString("isEnabled", "1");
                            editor.commit();
                        }else if(message.trim().equals("#ss00#")) {
                            abortBroadcast();
                            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
                            editor.putString("isEnabled", "0");
                            editor.commit();

                        }else if(message.trim().contains("#Tagline#")){
                            abortBroadcast();
                            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
                            editor.putString("tagline", message.trim().split("#")[2]);
                            editor.commit();
                        }
                    }

//                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
//
//
//                    // Show Alert
//                    int duration = Toast.LENGTH_LONG;
//                    Toast toast = Toast.makeText(context,
//                            "senderNum: "+ senderNum + ", message: " + message, duration);
//                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
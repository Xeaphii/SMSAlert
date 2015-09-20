package xeaphii.com.smsalert;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by Xeaphii on 9/19/2015.
 */
public class DesignSms extends Activity {
    Button SendMessage,AddColumn;
    EditText MessageBody;
    Spinner ColumnsForExcel;
    Sheet TempSheet = null;
    String PhoneNumberIndex = "";
    private int mMessageSentParts;
    private int mMessageSentTotalParts;
    private int mMessageSentCount;
    List<String> SmsList;
    List<String> numbersList;
    private final String MY_PREFS_NAME ="Prefs";
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    String Tagline = "";
    private ProgressDialog dialogSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_sms);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String PrefTagline = prefs.getString("tagline", null);
        if (PrefTagline != null) {
            Tagline = (PrefTagline);

        }else{
            Tagline = ("Tagline");
        }
        MessageBody = (EditText) findViewById(R.id.sms_body);
        SendMessage = (Button) findViewById(R.id.bt_send);
        AddColumn = (Button) findViewById(R.id.add_col);
        numbersList= new ArrayList<>();

        ColumnsForExcel = (Spinner) findViewById(R.id.number_col);
        AddColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageBody.setText(MessageBody.getText().toString()+"{"+ColumnsForExcel.getSelectedItem().toString()+"}");
                MessageBody.setSelection(MessageBody.getText().length());
            }
        });
        SendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new BackgroundTask(DesignSms.this).execute();
            }
        });

        List<String> resultSet = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String FilePath = extras.getString("FilePath");
            PhoneNumberIndex = extras.getString("numberColumn");
            File inputWorkbook = new File(FilePath);
                if (inputWorkbook.exists()) {
                    Workbook w;
                    try {
                        w = Workbook.getWorkbook(inputWorkbook);
                        // Get the first sheet
                        TempSheet = w.getSheet(0);
                        // Loop over column and lines
                        // for (int j = 0; j < sheet.getRows(); j++) {
                        Cell cell = TempSheet.getCell(0, 0);
                        //if(cell.getContents().equalsIgnoreCase(key)){
                        for (int i = 0; i < TempSheet.getColumns(); i++) {
                            if(i != Integer.parseInt(PhoneNumberIndex)) {
                                Cell cel = TempSheet.getCell(i, 0);
                                resultSet.add(cel.getContents() + "");
                            }

                        }

                        //}
                        //   continue;
                        // }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, resultSet);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ColumnsForExcel.setAdapter(dataAdapter);
                    } catch (BiffException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultSet.add("File not found..!");
                }
            if(resultSet.size()==0){
                resultSet.add("Data not found..!");
            }
        }

    }
    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTask(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Preparing sms, ");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
//            for(int i = 0; i < SmsList.size();i++){
//                Toast.makeText(getApplicationContext(),SmsList.get(i),Toast.LENGTH_LONG).show();
//            }
            startSendMessages();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (MessageBody.getText().toString().trim().length() > 0) {
                int count =0;
                for (int i = 1; i < TempSheet.getRows(); i++) {

                    Cell cell = TempSheet.getCell(Integer.parseInt(PhoneNumberIndex), i);
                    String con = cell.getContents();
                    if (con != null && con.length() != 0) {
                        count++;
                        numbersList.add(con);
                    }

                }
                if (count > 0) {

                    String SmsBody = MessageBody.getText().toString().trim();
                    SmsList = new ArrayList<>(Collections.nCopies(count, SmsBody));

                    int currentIndex = 0;


//                 for (int j = 0; j < TempSheet.getRows(); j++) {
//                Cell cell = TempSheet.getCell(0, 0);
//                //if(cell.getContents().equalsIgnoreCase(key)){
//                for (int i = 0; i < TempSheet.getColumns(); i++) {
//                    if(i != Integer.parseInt(PhoneNumberIndex)) {
//                        Cell cel = TempSheet.getCell(i, 0);
////                        resultSet.add(cel.getContents() + "");
//
//                    }
//                }
//                }
                    int start = 0, end = 0,TempCounter = 0;
                    while (currentIndex < MessageBody.getText().toString().trim().length()) {
                        start = SmsBody.indexOf('{') + 1 + currentIndex;
                        end = SmsBody.indexOf('}') + currentIndex;
                        int index = -1;
                        if (SmsBody.indexOf('{') >= 0 && SmsBody.indexOf('}') >= 0) {
                            String columnName = MessageBody.getText().toString().substring(start, end);
                            for (int i = 0; i < TempSheet.getColumns(); i++) {
                                if (columnName.equals(TempSheet.getCell(i, 0).getContents())) {
//                            Cell cel = TempSheet.getCell(i, 0);
//                        resultSet.add(cel.getContents() + "");
                                    index = i;
                                    break;
                                }
                            }
                            if (index >= 0) {
                                for (int i = 0; i < SmsList.size(); i++) {

                                    SmsList.set(i, SmsList.get(i).substring(0, SmsList.get(i).indexOf("{", TempCounter)) + TempSheet.getCell(index, i + 1).getContents() + SmsList.get(i).substring(SmsList.get(i).indexOf("}", TempCounter) + 1, SmsList.get(i).length()));
                                }
                                //output = SmsBody.indexOf('}');
                                currentIndex += SmsBody.indexOf('}') + 1;
                                SmsBody = SmsBody.substring(SmsBody.indexOf('}') + 1, SmsBody.length());
                            }
                            TempCounter++;
                        }else{
                            break;
                        }
                    }

                }
            }else{
                //Toast.makeText(getApplicationContext(),"Sms body can't be empty",Toast.LENGTH_LONG).show();
            }

            return null;
        }

    }


    private void startSendMessages(){
        dialogSms  = new ProgressDialog(DesignSms.this);
        dialogSms.setMessage("Sending sms, ");
        dialogSms.show();
        registerBroadCastReceivers();

        mMessageSentCount = 0;
        sendSMS(numbersList.get(mMessageSentCount), SmsList.get(mMessageSentCount)+" "+Tagline);
    }

    private void sendNextMessage(){
        if(thereAreSmsToSend()){
            sendSMS(numbersList.get(mMessageSentCount), SmsList.get(mMessageSentCount)+" "+Tagline);
        }else{
            if (dialogSms.isShowing()) {
                dialogSms.dismiss();
            }
            Toast.makeText(getBaseContext(), "All SMS have been sent",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean thereAreSmsToSend(){
        return mMessageSentCount < SmsList.size();
    }

    private void sendSMS(final String phoneNumber, String message) {


        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);
        mMessageSentTotalParts = parts.size();


        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        for (int j = 0; j < mMessageSentTotalParts; j++) {
            sentIntents.add(sentPI);
            deliveryIntents.add(deliveredPI);
        }

        mMessageSentParts = 0;
        sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
    }

    private void registerBroadCastReceivers(){

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        mMessageSentParts++;
                        if ( mMessageSentParts == mMessageSentTotalParts ) {
                          //  mMessageSentCount++;
                            sendNextMessage();
                        }

//                        Toast.makeText(getBaseContext(), "SMS sent",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

    }

}

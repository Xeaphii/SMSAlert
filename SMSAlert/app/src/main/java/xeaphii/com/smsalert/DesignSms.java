package xeaphii.com.smsalert;

import android.app.Activity;
import android.os.Bundle;
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
    List<String> SmsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_sms);

        MessageBody = (EditText) findViewById(R.id.sms_body);
        SendMessage = (Button) findViewById(R.id.bt_send);
        AddColumn = (Button) findViewById(R.id.add_col);

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

                String SmsBody = MessageBody.getText().toString().trim();
                SmsList = new ArrayList<>(Collections.nCopies(TempSheet.getRows(), SmsBody));

                int currentIndex =0 ;


                 for (int j = 0; j < TempSheet.getRows(); j++) {
                Cell cell = TempSheet.getCell(0, 0);
                //if(cell.getContents().equalsIgnoreCase(key)){
                for (int i = 0; i < TempSheet.getColumns(); i++) {
                    if(i != Integer.parseInt(PhoneNumberIndex)) {
                        Cell cel = TempSheet.getCell(i, 0);
//                        resultSet.add(cel.getContents() + "");

                    }
                }
                }
                int start = 0 , end = 0;
                while(currentIndex < MessageBody.getText().toString().trim().length()) {
                    start = SmsBody.indexOf('{') + 1+currentIndex;
                    end=SmsBody.indexOf('}')+currentIndex;
                    int index = 0;
                    String columnName = MessageBody.getText().toString().substring(start,end);
                    for(int i =0 ; i < TempSheet.getColumns();i++){
                        if(columnName.equals(TempSheet.getCell(i, 0).getContents())) {
//                            Cell cel = TempSheet.getCell(i, 0);
//                        resultSet.add(cel.getContents() + "");
                            index= i;
                            break;
                        }
                    }
                    for(int i = 1 ; i < SmsList.size();i++){
                        SmsList.set(i,SmsList.get(i).substring(start-1)+TempSheet.getCell(index, i).getContents()+SmsList.get(i).substring(end+1)) ;
                    }
                    //output = SmsBody.indexOf('}');
                    currentIndex += SmsBody.indexOf('}')+1;
                    SmsBody= SmsBody.substring(SmsBody.indexOf('}')+1,SmsBody.length());
                }
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
}

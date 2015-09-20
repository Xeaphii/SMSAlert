package xeaphii.com.smsalert;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
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

        List<String> resultSet = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String FilePath = extras.getString("FilePath");
            String PhoneNumberIndex = extras.getString("numberColumn");
            File inputWorkbook = new File(FilePath);
                if (inputWorkbook.exists()) {
                    Workbook w;
                    try {
                        w = Workbook.getWorkbook(inputWorkbook);
                        // Get the first sheet
                        Sheet sheet = w.getSheet(0);
                        // Loop over column and lines
                        // for (int j = 0; j < sheet.getRows(); j++) {
                        Cell cell = sheet.getCell(0, 0);
                        //if(cell.getContents().equalsIgnoreCase(key)){
                        for (int i = 0; i < sheet.getColumns(); i++) {
                            if(i != Integer.parseInt(PhoneNumberIndex)) {
                                Cell cel = sheet.getCell(i, 0);
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

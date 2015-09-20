package xeaphii.com.smsalert;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
public class SelectNumberCol extends Activity {
    Button ProceedNext;
    Spinner ColumnsForExcel;
    String FilePath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_number);
        ProceedNext = (Button) findViewById(R.id.proceed_next);
        ColumnsForExcel = (Spinner) findViewById(R.id.number_col);

        List<String> resultSet = new ArrayList<String>();
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                FilePath = extras.getString("FilePath");
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
                            Cell cel = sheet.getCell(i, 0);
                            resultSet.add(cel.getContents() + "");
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
                if (resultSet.size() == 0) {
                    resultSet.add("Data not found..!");
                }
            }

        ProceedNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!FilePath.trim().equals("")) {
                    Intent i = new Intent(SelectNumberCol.this, DesignSms.class);
                    i.putExtra("FilePath", FilePath);
                    i.putExtra("numberColumn", ColumnsForExcel.getSelectedItemPosition()+"");

                    startActivity(i);
                }else{
                    Toast.makeText(SelectNumberCol.this,"Empty File path",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}

package xeaphii.com.smsalert;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


/**
 * Created by Xeaphii on 9/17/2015.
 */
public class AdminUser extends Activity {
    EditText Tagline;
    private final String MY_PREFS_NAME ="Prefs";
    private  final int PICKFILE_RESULT_CODE = 9090;
    Button BtSave,BtBrowse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user);
        Tagline = (EditText) findViewById(R.id.et_tagline);
        BtSave = (Button) findViewById(R.id.save_tagline);
        BtBrowse = (Button) findViewById(R.id.bt_browse);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String PrefTagline = prefs.getString("tagline", null);
        if (PrefTagline != null) {
            Tagline.setText(PrefTagline);

        }else{
            Tagline.setText("Tagline");
        }

        BtBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });
        BtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Tagline.getText().toString().trim().equals("")) {
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("tagline", Tagline.getText().toString().trim());
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Tagline saved",Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(getApplicationContext(),"Tagline can't be empty",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();
                   // Toast.makeText(getApplicationContext(),FilePath,Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AdminUser.this,SelectNumberCol.class);
                    i.putExtra("FilePath",FilePath);
                    startActivity(i);
                    finish();
                //   Toast.makeText(getApplicationContext(),resultSet.toString(),Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}

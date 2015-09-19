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

/**
 * Created by Xeaphii on 9/17/2015.
 */
public class AdminUser extends Activity {
    EditText Tagline;
    private final String MY_PREFS_NAME ="Prefs";
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
            Tagline.setText("Tagline");
        }else{
            Tagline.setText(PrefTagline);
        }

        BtBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File file = new File(Environment.getExternalStorageDirectory()+ "");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                startActivity(intent);
            }
        });
        BtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Tagline.getText().toString().trim().equals("")) {
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("tagline", Tagline.getText().toString().trim());
                    editor.commit();
                }else{

                    Toast.makeText(getApplicationContext(),"Tagline can't be empty",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

package xeaphii.com.smsalert;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

/**
 * Created by Xeaphii on 9/17/2015.
 */
public class NormalUser extends Activity {
    EditText Tagline;
    private final String MY_PREFS_NAME ="Prefs";
    Button BtBrowse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_user);
        Tagline = (EditText) findViewById(R.id.et_tagline);
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

    }
}

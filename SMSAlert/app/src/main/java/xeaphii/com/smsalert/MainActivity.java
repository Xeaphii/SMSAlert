package xeaphii.com.smsalert;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
    EditText UserName, Password;
    Button BtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserName= (EditText) findViewById(R.id.user_name);
        Password = (EditText) findViewById(R.id.password);
        BtLogin = (Button) findViewById(R.id.bt_login);
        BtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserName.getText().equals("admin")&&Password.getText().equals("admin123")){

                }else  if(UserName.getText().equals("user")&&Password.getText().equals("user123")){
                }
            }
        });

    }

}
